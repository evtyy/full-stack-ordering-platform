package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.properties.StripeProperties;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.*;
import com.sky.websocket.WebSocketServer;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private StripeProperties stripeProperties;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * User submits order
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //Business exception handling: empty address, empty cart
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            //throw exception
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> cartList = shoppingCartMapper.list(shoppingCart);
        if (cartList == null || cartList.isEmpty()) {
            //throw exception
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //Insert new record into orders table
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setAddress(addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);

        orderMapper.insert(orders);

        //Insert multiple records into order detail table
        List<OrderDetail> orderDetailList = new ArrayList<>();
        cartList.forEach(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        });
        orderDetailMapper.insertBatch(orderDetailList);

        //Empty shopping cart
        shoppingCartMapper.deleteByUserId(userId);

        //Return VO
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();

        return orderSubmitVO;
    }

    /**
     * Order payment
     *
     * @param ordersPaymentDTO
     * @return
     */
    //TODO
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        //Directly call paySuccess to simulate successful payment
        paySuccess(ordersPaymentDTO.getOrderNumber());
        return null;
    }

    /**
     * Payment success, update order status
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        //look up order by order number
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        //update order's status, payment method, payment status and checkout time by order id
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

        //Notify merchant via websocket
        Map<String, Object> map = new HashMap<>();
        map.put("type", 1); // 1. new order notification
        map.put("orderId", ordersDB.getId());
        map.put("content", "Order number: " + outTradeNo);
        String msg = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(msg);
    }

    /**
     * Create a Stripe Checkout Session for an order and return its hosted payment URL.
     *
     * @param ordersPaymentDTO
     * @return
     */
    @Override
    public StripeCheckoutVO createCheckoutSession(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        String orderNumber = ordersPaymentDTO.getOrderNumber();
        Orders ordersDB = orderMapper.getByNumber(orderNumber);
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        Stripe.apiKey = stripeProperties.getSecretKey();

        //Stripe expects smallest currency unit (cents), order amount stored in dollars
        long amountInCents = ordersDB.getAmount().multiply(new BigDecimal(100)).longValue();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(stripeProperties.getSuccessUrl() + "&orderNumber=" + orderNumber)
                .setCancelUrl(stripeProperties.getCancelUrl())
                .putMetadata("orderNumber", orderNumber)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Order " + orderNumber)
                                                                .build())
                                                .build())
                                .build())
                .build();

        Session session = Session.create(params);

        return StripeCheckoutVO.builder()
                .sessionId(session.getId())
                .checkoutUrl(session.getUrl())
                .build();
    }

    /**
     * Confirm a Stripe Checkout Session and mark the order as paid if Stripe reports it paid.
     * Safe to call more than once for the same session (e.g. on page refresh) - paySuccess
     * only fires the first time the order is found unpaid.
     *
     * @param sessionId
     * @return true if the order is (now, or already was) paid
     */
    @Override
    public boolean confirmCheckoutSession(String sessionId) throws Exception {
        Stripe.apiKey = stripeProperties.getSecretKey();

        Session session = Session.retrieve(sessionId);
        String orderNumber = session.getMetadata().get("orderNumber");
        if (orderNumber == null) {
            throw new OrderBusinessException(MessageConstant.STRIPE_SESSION_NOT_FOUND);
        }

        if (!"paid".equals(session.getPaymentStatus())) {
            return false;
        }

        Orders ordersDB = orderMapper.getByNumber(orderNumber);
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        if (!Orders.PAID.equals(ordersDB.getPayStatus())) {
            paySuccess(orderNumber);
        }

        return true;
    }

    /**
     * Query order history
     *
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageResult<OrderVO> pageQueryByUser(int page, int pageSize, Integer status) {
        //Set up pagination
        PageHelper.startPage(page, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        //Paginated conditional query
        Page<Orders> pageOrders = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList<>();

        //Query order details and wrap them into OrderVO for response
        if (pageOrders != null && pageOrders.getTotal() > 0) {
            for (Orders orders : pageOrders) {
                Long orderId = orders.getId();//order id

                //Query order details
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        long total = Optional.ofNullable(pageOrders).map(Page::getTotal).orElse(0L);
        return new PageResult<>(total, list);
    }

    /**
     * Query order details
     *
     * @param id
     * @return
     */
    public OrderVO details(Long id) {
        //look up order by id
        Orders orders = orderMapper.getById(id);

        //Query dish/combo details for this order
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        //Wrap order and details into an OrderVO and return it
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * User cancels order
     *
     * @param id
     */
    public void userCancelById(Long id) throws Exception {
        //look up order by id
        Orders ordersDB = orderMapper.getById(id);

        //Verify order exists
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //Order status: 1=pending payment, 2=pending confirmation, 3=confirmed,
        //              4=our for delivery, 5=completed, 6=cancelled
        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        //If order is cancelled while pending confirmation, refund is required
        if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            //Call Wechat Pay refund API
            weChatPayUtil.refund(
                    ordersDB.getNumber(), //merchant order number
                    ordersDB.getNumber(), //merchant refund number
                    new BigDecimal(0.01), //refund amount, in yuan
                    new BigDecimal(0.01));//original order amount

            //Update payment status to "refunded"
            orders.setPayStatus(Orders.REFUND);
        }

        //Update order status, cancellation reason, and cancellation time
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * Reorder
     *
     * @param id
     */
    public void repetition(Long id) {
        //Query current user's id
        Long userId = BaseContext.getCurrentId();

        //Query current order's details by order id
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        //Convert order detail objects into shopping cart objects
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();

            //Copy dish info from original order details back into cart obj
            BeanUtils.copyProperties(x, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

        //Batch-insert cart objects into database
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * Search orders
     *
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult<OrderVO> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        //Some order statuses require also returning order's dish info, convert Orders into OrderVO
        List<OrderVO> orderVOList = getOrderVOList(page);

        return new PageResult<>(page.getTotal(), orderVOList);
    }

    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        //Need to return order's dish info, so build custom OrderVO response
        List<OrderVO> orderVOList = new ArrayList<>();

        List<Orders> ordersList = page.getResult();
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                //Copy shared fields onto OrderVO
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                String orderDishes = getOrderDishesStr(orders);

                //Attach order's dish info to orderVO and add it to orderVOList
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    /**
     * Build dish-info string for an order by order id
     *
     * @param orders
     * @return
     */
    private String getOrderDishesStr(Orders orders) {
        //Query order's dish details (dishes and quantities in the order)
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        //Concatenate each dish's info into a string (format: Beef Bibimbap*3;)
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());

        //Join dish info for this order together
        return String.join("", orderDishList);
    }

    /**
     * Count orders by status
     *
     * @return
     */
    public OrderStatisticsVO statistics() {
        //Query counts of orders pending confirmation, pending delivery and out for delivery, by status
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);

        //Wrap queried data into orderStatisticsVO for response
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        return orderStatisticsVO;
    }

    /**
     * Confirm an order
     *
     * @param ordersConfirmDTO
     */
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();

        orderMapper.update(orders);
    }

    /**
     * Reject an order
     *
     * @param ordersRejectionDTO
     */
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        //Look up order by id
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());

        //Order can only be rejected if it exists and is in status 2 (pending confirmation)
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //Rejecting order requires refund; update order's status, rejection reason, and cancellation time by id
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * Cancel an order
     *
     * @param ordersCancelDTO
     */
    public void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception {
        //Look up order by id
        Orders ordersDB = orderMapper.getById(ordersCancelDTO.getId());

        //Payment status
        Integer payStatus = ordersDB.getPayStatus();
        if (Orders.PAID.equals(payStatus)) {
            //Already paid by user, refund required
            String refund = weChatPayUtil.refund(
                    ordersDB.getNumber(),
                    ordersDB.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("Requesting refund: {}", refund);
        }

        //Cancelling an order from admin side requires refund; update order status, cancellation reason, and cancellation time by id
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * Dispatch order for delivery
     *
     * @param id
     */
    public void delivery(Long id) {
        //Look up order by id
        Orders ordersDB = orderMapper.getById(id);

        //Verify order exists and is in status 3
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        //Update order status to "out for delivery"
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);

        orderMapper.update(orders);
    }

    /**
     * Complete and order
     *
     * @param id
     */
    public void complete(Long id) {
        //Look up order by id
        Orders ordersDB = orderMapper.getById(id);

        //Verify order exists and is in status 4
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        //Update order status to "completed"
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * User rushes order
     *
     * @param id
     */
    public void reminder(Long id) {
        //Check if order exists
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //Implement "rush order" reminder via websocket
        Map<String, Object> map = new HashMap<>();
        map.put("type", 2);// 2=user rushing order
        map.put("orderId", id);
        map.put("content", "Order number: " + orders.getNumber());
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }

}
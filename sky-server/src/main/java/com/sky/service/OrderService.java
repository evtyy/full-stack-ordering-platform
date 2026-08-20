package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.vo.StripeCheckoutVO;

public interface OrderService {
    /**
     * User submit order
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * Order payment
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * Payment success, update order status
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * Create a Stripe Checkout Session for an order and return its hosted payment URL
     * @param ordersPaymentDTO
     * @return
     */
    StripeCheckoutVO createCheckoutSession(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * Confirm a Stripe Checkout Session and mark the order as paid if Stripe reports it paid
     * @param sessionId
     * @return true if the order is (now, or already was) paid
     */
    boolean confirmCheckoutSession(String sessionId) throws Exception;

    /**
     * Customer-side paginated order query
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult<OrderVO> pageQueryByUser(int page, int pageSize, Integer status);

    /**
     * Check order details
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * User cancel order
     * @param id
     */
    void userCancelById(Long id) throws Exception;

    /**
     * Reorder
     * @param id
     */
    void repetition(Long id);

    /**
     * Conditional order search
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult<OrderVO> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * Order count statistics by status
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * Confirm order
     *
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * Reject order
     *
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * Admin cancel order
     *
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * Deliver order
     *
     * @param id
     */
    void delivery(Long id);

    /**
     * Complete order
     *
     * @param id
     */
    void complete(Long id);

    /**
     * User rushes order
     * @param id
     */
    void reminder(Long id);
}

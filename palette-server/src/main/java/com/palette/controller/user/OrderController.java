package com.palette.controller.user;

import com.palette.dto.OrdersPaymentDTO;
import com.palette.dto.OrdersSubmitDTO;
import com.palette.result.PageResult;
import com.palette.result.Result;
import com.palette.service.OrderService;
import com.palette.vo.OrderPaymentVO;
import com.palette.vo.OrderSubmitVO;
import com.palette.vo.OrderVO;
import com.palette.vo.StripeCheckoutVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@ApiOperation("Order API")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("User order submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO dto) {
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(dto);
        return Result.success(orderSubmitVO);
    }

    /**
     * Order payment
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("Order payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("Order payment: {}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("Generate pre-payment transaction: {}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * Create a Stripe Checkout Session for an order (customer-web payment flow)
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PostMapping("/checkout-session")
    @ApiOperation("Create Stripe checkout session")
    public Result<StripeCheckoutVO> createCheckoutSession(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        StripeCheckoutVO stripeCheckoutVO = orderService.createCheckoutSession(ordersPaymentDTO);
        return Result.success(stripeCheckoutVO);
    }

    /**
     * Confirm a Stripe Checkout Session after the customer is redirected back from Stripe
     *
     * @param sessionId
     * @return
     */
    @GetMapping("/checkout-session/confirm")
    @ApiOperation("Confirm Stripe checkout session")
    public Result<Boolean> confirmCheckoutSession(@RequestParam String sessionId) throws Exception {
        boolean paid = orderService.confirmCheckoutSession(sessionId);
        return Result.success(paid);
    }

    /**
     * Query order details
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("Query order details")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    @GetMapping("/historyOrders")
    @ApiOperation("Paginated query of order history")
    public Result<PageResult<OrderVO>> historyOrders(int page, int pageSize, Integer status) {
        PageResult<OrderVO> pageResult = orderService.pageQueryByUser(page, pageSize, status);
        return Result.success(pageResult);
    }

    /**
     * User cancel order
     *
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("User cancel order")
    public Result<String> cancel(@PathVariable Long id) throws Exception {
        orderService.userCancelById(id);
        return Result.success();
    }

    /**
     * Reorder
     *
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("Reorder")
    public Result<String> repetition(@PathVariable Long id) {
        orderService.repetition(id);
        return Result.success();
    }

    /**
     * Rush order
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("Rush order")
    public Result reminder(@PathVariable Long id) {
        orderService.reminder(id);
        return Result.success();
    }
}

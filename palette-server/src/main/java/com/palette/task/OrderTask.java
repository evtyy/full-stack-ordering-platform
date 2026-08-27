package com.palette.task;

import com.palette.entity.Orders;
import com.palette.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * Method to handle timed-out orders
     */
    @Scheduled(cron = "0 * * * * ? ")   // Triggered every minute
    public void processTimeoutOrders() {
        log.info("Processing timed-out orders");
        // Get timed-out orders: current time - 15 minutes
        List<Orders> orderList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));
        if (orderList != null && !orderList.isEmpty()) {
            orderList.forEach(order -> {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("Order timed out, automatically cancelled");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            });
        }
    }

    /**
     * Handle orders stuck in delivery
     */
    @Scheduled(cron = "0 0 1 * * ? ")   // Triggered at 1am every day
    public void processDeliveryOrders() {
        log.info("Processing orders in delivery");
        // Get orders that are out for delivery
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orderList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if (orderList != null && !orderList.isEmpty()) {
            orderList.forEach(order -> {
                order.setStatus(Orders.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(order);
            });
        }
    }
}

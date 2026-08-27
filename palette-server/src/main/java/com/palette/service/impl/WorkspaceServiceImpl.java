package com.palette.service.impl;

import com.palette.constant.StatusConstant;
import com.palette.entity.Orders;
import com.palette.mapper.DishMapper;
import com.palette.mapper.OrderMapper;
import com.palette.mapper.SetmealMapper;
import com.palette.mapper.UserMapper;
import com.palette.service.WorkspaceService;
import com.palette.vo.BusinessDataVO;
import com.palette.vo.DishOverViewVO;
import com.palette.vo.OrderOverViewVO;
import com.palette.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * Aggregate business data over a time period
     *
     * @param begin
     * @param end
     * @return
     */
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        /**
         * Turnover: total amount of completed orders for the day
         * Valid orders: number of completed orders for the day
         * Order completion rate: valid order count / total order count
         * Average order value: turnover / valid order count
         * New users: number of new users for the day
         */

        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);

        // Query the total order count
        Integer totalOrderCount = orderMapper.countByMap(map);

        map.put("status", Orders.COMPLETED);
        // Turnover
        Double turnover = orderMapper.sumByMap(map);
        turnover = turnover == null ? 0.0 : turnover;

        // Valid order count
        Integer validOrderCount = orderMapper.countByMap(map);

        Double unitPrice = 0.0;

        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0 && validOrderCount != 0) {
            // Order completion rate
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            // Average order value
            unitPrice = Double.parseDouble(String.format("%.2f", turnover / validOrderCount));
        }

        // New user count
        Integer newUsers = userMapper.countByMap(map);

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }

    /**
     * Query order management data
     *
     * @return
     */
    public OrderOverViewVO getOrderOverView() {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
        map.put("status", Orders.TO_BE_CONFIRMED);

        // Pending confirmation
        Integer waitingOrders = orderMapper.countByMap(map);

        // Pending delivery
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countByMap(map);

        // Completed
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);

        // Cancelled
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);

        // All orders
        map.put("status", null);
        Integer allOrders = orderMapper.countByMap(map);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * Query dish overview
     *
     * @return
     */
    public DishOverViewVO getDishOverView() {
        Map<String, Integer> map = new HashMap<>();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * Query combo meal overview
     *
     * @return
     */
    public SetmealOverViewVO getSetmealOverView() {
        Map<String, Integer> map = new HashMap<>();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
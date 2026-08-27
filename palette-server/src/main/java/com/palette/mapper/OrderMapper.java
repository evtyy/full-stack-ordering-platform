package com.palette.mapper;

import com.github.pagehelper.Page;
import com.palette.dto.GoodsSalesDTO;
import com.palette.dto.OrdersPageQueryDTO;
import com.palette.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * Insert order data
     * @param orders
     */
    void insert(Orders orders);

    /**
     * Query order by order number and user id
     * @param orderNumber
     * @param userId
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * Update order information
     * @param orders
     */
    void update(Orders orders);

    /**
     * Paginated conditional query sorted by order time
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * Query order by id
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * Count orders by status
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * Query orders by order status and order time
     * @param status
     * @param orderTime
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    /**
     * Aggregate turnover data based on dynamic conditions
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * Count orders based on dynamic conditions
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * Get the top 10 sales rankings within a specified time range
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin,LocalDateTime end);

    /**
     * Query order by order number
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);
}

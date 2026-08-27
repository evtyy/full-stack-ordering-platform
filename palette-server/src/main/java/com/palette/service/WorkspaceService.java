package com.palette.service;

import com.palette.vo.BusinessDataVO;
import com.palette.vo.DishOverViewVO;
import com.palette.vo.OrderOverViewVO;
import com.palette.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {

    /**
     * Aggregate business data over a time period
     * @param begin
     * @param end
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * Query order management data
     * @return
     */
    OrderOverViewVO getOrderOverView();

    /**
     * Query dish overview
     * @return
     */
    DishOverViewVO getDishOverView();

    /**
     * Query combo meal overview
     * @return
     */
    SetmealOverViewVO getSetmealOverView();

}

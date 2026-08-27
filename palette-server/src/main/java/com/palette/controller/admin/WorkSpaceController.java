package com.palette.controller.admin;

import com.palette.result.Result;
import com.palette.service.WorkspaceService;
import com.palette.vo.BusinessDataVO;
import com.palette.vo.DishOverViewVO;
import com.palette.vo.OrderOverViewVO;
import com.palette.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Workspace
 */
@RestController
@RequestMapping("/admin/workspace")
public class WorkSpaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * Query today's workspace data
     *
     * @return
     */
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData() {
        // Get the start time of today
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        // Get the end time of today
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);
        return Result.success(businessDataVO);
    }

    /**
     * Query order management data
     *
     * @return
     */
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> orderOverView() {
        return Result.success(workspaceService.getOrderOverView());
    }

    /**
     * Query dish overview
     *
     * @return
     */
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> dishOverView() {
        return Result.success(workspaceService.getDishOverView());
    }

    /**
     * Query combo meal overview
     *
     * @return
     */
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> setmealOverView() {
        return Result.success(workspaceService.getSetmealOverView());
    }
}
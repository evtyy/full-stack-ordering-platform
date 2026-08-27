package com.palette.controller.admin;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.palette.result.Result;
import com.palette.service.ReportService;
import com.palette.vo.OrderReportVO;
import com.palette.vo.SalesTop10ReportVO;
import com.palette.vo.TurnoverReportVO;
import com.palette.vo.UserReportVO;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("Turnover statistics: begin={}, end={}", begin, end);
        return Result.success(reportService.getTurnoverReport(begin, end));
    }

    /**
     * User data statistics
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        return Result.success(reportService.getUserStatistics(begin, end));
    }

    /**
     * Order data statistics
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> orderStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        return Result.success(reportService.getOrderStatistics(begin, end));
    }

    /**
     * Sales ranking statistics
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        return Result.success(reportService.getSalesTop10(begin, end));
    }

    /**
     * Export operations report
     *
     * @param resp
     */
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse resp) {
        reportService.exportExcel(resp);
    }
}
package com.palette.service;

import java.time.LocalDate;

import com.palette.vo.OrderReportVO;
import com.palette.vo.SalesTop10ReportVO;
import com.palette.vo.TurnoverReportVO;
import com.palette.vo.UserReportVO;
import javax.servlet.http.HttpServletResponse;
public interface ReportService {

    /**
     * Turnover statistics
     *
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverReport(LocalDate begin, LocalDate end);

    /**
     * Count users within a time range
     *
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * Count orders within a time range
     *
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * Query the top 10 sales rankings within a specified time range
     *
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /**
     * Export excel
     *
     * @param resp
     */
    void exportExcel(HttpServletResponse resp);

}
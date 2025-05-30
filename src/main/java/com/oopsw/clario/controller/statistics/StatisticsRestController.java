package com.oopsw.clario.controller.statistics;

import com.oopsw.clario.dto.statistics.*;
import com.oopsw.clario.dto.statistics.MonthlyExpenseComparisonDTO;
import com.oopsw.clario.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsRestController {
    private final StatisticsService statisticsService;

    @GetMapping("/monthly-expense")
    public List<MonthlyExpenseTotalDTO> getMonthlyExpenseTotal(
            @RequestParam Long memberId,
            @RequestParam Long year,
            @RequestParam Long month) {
        return statisticsService.getMonthlyExpenseTotal(memberId, year, month); // ⚠️ 3개 전달
    }

    @GetMapping("/monthly-income")
    public ResponseEntity<MonthlyIncomeDTO> getMonthlyIncome(
            @RequestParam Long memberId,
            @RequestParam Long year,
            @RequestParam Long month) {

        MonthlyIncomeDTO result = statisticsService.getMonthlyIncome(memberId, year, month);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top3")
    public List<Top3CategoriesDTO> getTop3Categories(@RequestParam Long memberId,
                                                     @RequestParam Long year,
                                                     @RequestParam Long month) {
        return statisticsService.getTop3Categories(memberId, year, month);
    }


    @GetMapping("/monthly-card-trade")
    public List<MonthlyCardTradeTotalDTO> getMonthlyCardTradeTotal(
            @RequestParam Long memberId,
            @RequestParam Long year) {  // year 파라미터 추가
        return statisticsService.getMonthlyCardTradeTotal(memberId, year);
    }

    @GetMapping("/yearly-total-expense")
    public ResponseEntity<List<YearlyExpenseDTO>> getYearlyTotalExpense(@RequestParam Long memberId) {
        return ResponseEntity.ok(statisticsService.getYearlyTotalExpense(memberId));
    }

    @GetMapping("/yearly-income")
    public List<YearlyIncomeDTO> getYearlyIncome(@RequestParam Long memberId) {
        return statisticsService.getYearlyTotalIncome(memberId);
    }

    @GetMapping("/monthly-income-average")
    public ResponseEntity<List<MonthlyIncomeAverageDTO>> getMonthlyAverageIncome(@RequestParam Long memberId) {
        return ResponseEntity.ok(statisticsService.getMonthlyAverageIncome(memberId));
    }

    @GetMapping("/monthly-expense-average")
    public List<MonthlyExpenseAverageDTO> getMonthlyExpenseAverage(@RequestParam Long memberId) {
        return statisticsService.getMonthlyExpenseAverage(memberId);
    }

    @GetMapping("/top-category-stats")
    public ResponseEntity<CategoryStatisticsDTO> getTopCategoryStats(
            @RequestParam Long memberId,
            @RequestParam Long year,
            @RequestParam Long month) {
        return ResponseEntity.ok(statisticsService.getCategoryStatistics(memberId, year, month));
    }

    @GetMapping("/expense/growth")
    public ResponseEntity<MonthlyExpenseComparisonDTO> getMonthlyExpenseGrowth(
            @RequestParam Long memberId,
            @RequestParam Long year,
            @RequestParam Long month) {
        return ResponseEntity.ok(statisticsService.getMonthlyExpenseComparison(memberId, year, month));
    }

    @GetMapping("/income-vs-expense")
    public ResponseEntity<IncomeVsExpenseDTO> getIncomeVsExpense(
            @RequestParam Long memberId,
            @RequestParam Long year,
            @RequestParam Long month) {
        return ResponseEntity.ok(statisticsService.getIncomeVsExpense(memberId, year, month));
    }

}


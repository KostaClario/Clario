package com.oopsw.clario.controller.statistics;

import com.oopsw.clario.dto.statistics.MonthlyCardTradeTotalDTO;
import com.oopsw.clario.dto.statistics.MonthlyExpenseTotalDTO;
import com.oopsw.clario.dto.statistics.Top3CategoriesDTO;
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
    public List<MonthlyExpenseTotalDTO> getMonthlyExpense(@RequestParam Long memberId) {
        return statisticsService.getMonthlyExpenseTotal(memberId);
    }

    @GetMapping("/top3")
    public List<Top3CategoriesDTO> getTop3Categories(@RequestParam Long memberId) {
        return statisticsService.getTop3Categories(memberId);
    }

    @GetMapping("/monthly-card-trade")
    public List<MonthlyCardTradeTotalDTO> getMonthlyCardTradeTotal(@RequestParam Long memberId) {
        return statisticsService.getMonthlyCardTradeTotal(memberId);
    }
}


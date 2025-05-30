package com.oopsw.clario.controller;

import com.oopsw.clario.dto.dashboard.MemberDateDTO;
import com.oopsw.clario.dto.dashboard.Top3CategoryDTO;
import com.oopsw.clario.dto.dashboard.TradeDTO;
import com.oopsw.clario.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @PostMapping("/monthly-income")
    public Long getMonthlyIncome(@RequestBody MemberDateDTO memberDateDTO) {
        log.info("memberDateDTO : " + memberDateDTO.getMemberId());
        log.info("memberDateDTO : " + memberDateDTO.getMonthDate());
        log.info("memberDateDTO : " + memberDateDTO.getYearDate());
        return dashboardService.getMonthlyIncome(memberDateDTO);
    }

    @PostMapping("/monthly-expense")
    public Long getMonthlyExpense(@RequestBody MemberDateDTO memberDateDTO) {
        return dashboardService.getMonthlyExpense(memberDateDTO);
    }

    @GetMapping("/target-assets/{memberId}")
    public Long getTargetAssets(@PathVariable int memberId) {
        return dashboardService.getTargetAssets(memberId);
    }

    @GetMapping("/total-assets/{memberId}")
    public Long getTotalAssets(@PathVariable int memberId) {
        return dashboardService.getTotalAssets(memberId);
    }

    @PostMapping("/target-assets")
    public boolean addTargetAssets(@RequestBody HashMap<String, Object> map) {
        return dashboardService.addTargetAssets(map);
    }

    @PostMapping("/today-expense")
    public List<TradeDTO> getTodayExpense(@RequestBody MemberDateDTO memberDateDTO) {
        return dashboardService.getTodayExpense(memberDateDTO);
    }

    @PostMapping("/today-income")
    public List<TradeDTO> getTodayIncome(@RequestBody MemberDateDTO memberDateDTO) {
        return dashboardService.getTodayIncome(memberDateDTO);
    }

    @PostMapping("/top3-category")
    public List<Top3CategoryDTO> getTop3Category(@RequestBody MemberDateDTO memberDateDTO) {
        return dashboardService.getTop3Category(memberDateDTO);
    }

    @PostMapping("/year-expenses")
    public List<TradeDTO> getYearsExpenses(@RequestBody MemberDateDTO memberDateDTO) {
        return dashboardService.getYearsExpenses(memberDateDTO);
    }

    @PostMapping("/year-incomes")
    public List<TradeDTO> getYearsIncomes(@RequestBody MemberDateDTO memberDateDTO) {
        return dashboardService.getYearsIncomes(memberDateDTO);
    }
}

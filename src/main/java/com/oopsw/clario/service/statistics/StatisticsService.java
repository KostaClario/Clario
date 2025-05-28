package com.oopsw.clario.service.statistics;

import com.oopsw.clario.dto.statistics.MonthlyCardTradeTotalDTO;
import com.oopsw.clario.dto.statistics.MonthlyExpenseTotalDTO;
import com.oopsw.clario.dto.statistics.Top3CategoriesDTO;
import com.oopsw.clario.repository.statistics.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StatisticsService {
    @Autowired
    private StatisticsRepository statisticsRepository;

    public List<MonthlyExpenseTotalDTO> getMonthlyExpenseTotal(Long memberId) {
        List<MonthlyExpenseTotalDTO> result = statisticsRepository.getMonthlyExpenseTotal(memberId);
        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + " 에 대한 소비 내역이 없습니다.");
            return Collections.emptyList();
        }
        return result;
    }

    public List<Top3CategoriesDTO> getTop3Categories(Long memberId) {
        List<Top3CategoriesDTO> result = statisticsRepository.getTop3Categories(memberId);
        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + " 에 대한 소비 내역이 없습니다.");
            return Collections.emptyList();
        }
        return result;
    }

    public List<MonthlyCardTradeTotalDTO> getMonthlyCardTradeTotal(Long memberId) {
        List<MonthlyCardTradeTotalDTO> result = statisticsRepository.getMonthlyCardTradeTotal(memberId);
        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + " 에 대한 소비 내역이 없습니다.");
            return Collections.emptyList();
        }
        return result;
        }
    }



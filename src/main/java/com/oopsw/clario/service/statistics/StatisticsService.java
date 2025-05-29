package com.oopsw.clario.service.statistics;

import com.oopsw.clario.dto.statistics.*;
import com.oopsw.clario.repository.statistics.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    @Autowired
    private StatisticsRepository statisticsRepository;

//    public List<MonthlyExpenseTotalDTO> getMonthlyExpenseTotal(Long memberId) {
//        List<MonthlyExpenseTotalDTO> result = statisticsRepository.getMonthlyExpenseTotal(memberId);
//        if (result == null || result.isEmpty()) {
//            System.out.println("⚠ memberId=" + memberId + " 에 대한 소비 내역이 없습니다.");
//            return Collections.emptyList();
//        }
//        return result;
//    }

    public List<MonthlyExpenseTotalDTO> getMonthlyExpenseTotal(Long memberId, Long year, Long month) {
        List<MonthlyExpenseTotalDTO> result = statisticsRepository.getMonthlyExpenseTotal(memberId, year, month);
        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + ", year=" + year + ", month=" + month + " 에 대한 소비 내역이 없습니다.");
            return Collections.emptyList();
        }
        return result;
    }



    public List<Top3CategoriesDTO> getTop3Categories(Long memberId, Long year, Long month) {
        List<Top3CategoriesDTO> result = statisticsRepository.getTop3Categories(memberId, year, month);

        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + ", year=" + year + ", month=" + month + " 에 대한 소비 내역이 없습니다.");
            return Collections.emptyList();
        }

        return result;
    }


    public List<MonthlyCardTradeTotalDTO> getMonthlyCardTradeTotal(Long memberId, Long year) {
        List<MonthlyCardTradeTotalDTO> result = statisticsRepository.getMonthlyCardTradeTotal(memberId, year);
        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + " 에 대한 소비 내역이 없습니다.");
            return Collections.emptyList();
        }
        return result;
        }

    public List<YearlyExpenseDTO> getYearlyTotalExpense(Long memberId) {
        List<YearlyExpenseDTO> result = statisticsRepository.getYearlyTotalExpense(memberId);
        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + " 에 대한 연간 소비 내역이 없습니다.");
            return Collections.emptyList();
        }
        return result;
    }

    public List<YearlyIncomeDTO> getYearlyTotalIncome(Long memberId) {
        List<YearlyIncomeDTO> cardList = statisticsRepository.getYearlyIncomeFromCard(memberId);
        List<YearlyIncomeDTO> accountList = statisticsRepository.getYearlyIncomeFromAccount(memberId);

        Map<Integer, Long> yearToTotalIncome = new HashMap<>();

        for (YearlyIncomeDTO dto : cardList) {
            yearToTotalIncome.merge(dto.getYear(), dto.getTotalIncome(), Long::sum);
        }
        for (YearlyIncomeDTO dto : accountList) {
            yearToTotalIncome.merge(dto.getYear(), dto.getTotalIncome(), Long::sum);
        }

        return yearToTotalIncome.entrySet().stream()
                .map(entry -> new YearlyIncomeDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(YearlyIncomeDTO::getYear))
                .collect(Collectors.toList());
    }
    public List<MonthlyIncomeAverageDTO> getMonthlyAverageIncome(Long memberId) {
        return statisticsRepository.getMonthlyAverageIncome(memberId);
    }

    public List<MonthlyExpenseAverageDTO> getMonthlyExpenseAverage(Long memberId) {
        return statisticsRepository.getMonthlyExpenseAverage(memberId);
    }

    public MonthlyIncomeDTO getMonthlyIncome(Long memberId, Long year, Long month) {
        return statisticsRepository.getMonthlyIncome(memberId, year, month);
    }
    }



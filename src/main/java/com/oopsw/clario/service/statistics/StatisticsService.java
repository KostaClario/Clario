package com.oopsw.clario.service.statistics;

import com.oopsw.clario.dto.statistics.*;
import com.oopsw.clario.repository.statistics.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    public List<MonthlyExpenseTotalDTO> getMonthlyExpenseTotal(Integer memberId, Long year, Long month) {
        List<MonthlyExpenseTotalDTO> result = statisticsRepository.getMonthlyExpenseTotal(memberId, year, month);
        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + ", year=" + year + ", month=" + month + " 에 대한 소비 내역이 없습니다.");
            return Collections.emptyList();
        }
        return result;
    }


    public List<Top3CategoriesDTO> getTop3Categories(Integer memberId, Long year, Long month) {
        List<Top3CategoriesDTO> result = statisticsRepository.getTop3Categories(memberId, year, month);

        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + ", year=" + year + ", month=" + month + " 에 대한 소비 내역이 없습니다.");
            return Collections.emptyList();
        }

        return result;
    }


    public List<MonthlyCardTradeTotalDTO> getMonthlyCardTradeTotal(Integer memberId, Long year) {
        List<MonthlyCardTradeTotalDTO> result = statisticsRepository.getMonthlyCardTradeTotal(memberId, year);
        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + " 에 대한 소비 내역이 없습니다.");
            return Collections.emptyList();
        }
        return result;
    }

    public List<YearlyExpenseDTO> getYearlyTotalExpense(Integer memberId) {
        List<YearlyExpenseDTO> result = statisticsRepository.getYearlyTotalExpense(memberId);
        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + " 에 대한 연간 소비 내역이 없습니다.");
            return Collections.emptyList();
        }
        return result;
    }

    public List<YearlyIncomeDTO> getYearlyTotalIncome(Integer memberId) {
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

    public List<MonthlyIncomeAverageDTO> getMonthlyAverageIncome(Integer memberId) {
        return statisticsRepository.getMonthlyAverageIncome(memberId);
    }

    public List<MonthlyExpenseAverageDTO> getMonthlyExpenseAverage(Integer memberId) {
        return statisticsRepository.getMonthlyExpenseAverage(memberId);
    }

    public MonthlyIncomeDTO getMonthlyIncome(Integer memberId, Long year, Long month) {
        return statisticsRepository.getMonthlyIncome(memberId, year, month);
    }

    public CategoryStatisticsDTO getCategoryStatistics(Integer memberId, Long year, Long month) {
        List<TopCategoryByCountDTO> countBased = statisticsRepository.getTopCategoriesByCount(memberId, year, month);
        List<TopCategoryByAmountDTO> amountBased = statisticsRepository.getTopCategoriesByAmount(memberId, year, month);

        CategoryStatisticsDTO dto = new CategoryStatisticsDTO();
        dto.setCountBased(countBased);
        dto.setAmountBased(amountBased);
        return dto;
    }

    public Long getMonthlyExpenseSum(Integer memberId, Long year, Long month) {
        return statisticsRepository.getMonthlyExpenseSum(memberId, year, month);
    }

    public Double getMonthlyExpenseGrowthRate(Integer memberId, Long year, Long month) {
        // 현재 월 출금 합계
        Long current = statisticsRepository.getMonthlyExpenseSum(memberId, year, month);
        if (current == null) current = 0L;

        // 전월 계산
        Long previousMonth = month - 1;
        Long previousYear = year;
        if (previousMonth == 0) {
            previousMonth = 12L;
            previousYear = year - 1;
        }

        Long previous = statisticsRepository.getMonthlyExpenseSum(memberId, previousYear, previousMonth);
        if (previous == null) previous = 0L;

        // 증감률 계산
        if (previous == 0L) {
            return current > 0L ? 100.0 : 0.0;
        }

        double growthRate = ((double) (current - previous) / previous) * 100.0;
        return Math.round(growthRate * 100.0) / 100.0;
    }

    public MonthlyExpenseComparisonDTO getMonthlyExpenseComparison(Integer memberId, Long year, Long month) {
        Long current = statisticsRepository.getMonthlyExpenseSum(memberId, year, month);
        if (current == null) current = 0L;

        Long previousMonth = month - 1;
        Long previousYear = year;
        if (previousMonth == 0) {
            previousMonth = 12L;
            previousYear -= 1;
        }

        Long previous = statisticsRepository.getMonthlyExpenseSum(memberId, previousYear, previousMonth);
        if (previous == null) previous = 0L;

        double rate;
        if (previous == 0L) {
            rate = current > 0L ? 100.0 : 0.0;
        } else {
            rate = ((double)(current - previous) / previous) * 100.0;
        }

        // 반올림
        rate = Math.round(rate * 100.0) / 100.0;

        return new MonthlyExpenseComparisonDTO(current, previous, rate);
    }

    public IncomeVsExpenseDTO getIncomeVsExpense(Integer memberId, Long year, Long month) {
        Long income = statisticsRepository.getMonthlyIncomeSum(memberId, year, month);
        Long expense = statisticsRepository.getMonthlyExpenseSum(memberId, year, month);

        double rate = (income == 0) ? 0.0 : (double) expense / income * 100.0;
        rate = Math.round(rate * 100.0) / 100.0;

        IncomeVsExpenseDTO dto = new IncomeVsExpenseDTO();
        dto.setIncome(income);
        dto.setExpense(expense);
        dto.setRate(rate);
        return dto;
    }

}




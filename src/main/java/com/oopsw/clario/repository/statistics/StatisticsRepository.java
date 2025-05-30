package com.oopsw.clario.repository.statistics;

import com.oopsw.clario.dto.statistics.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatisticsRepository {
    // ✅ 이미 올바름 - 월별 특정 데이터
    List<MonthlyExpenseTotalDTO> getMonthlyExpenseTotal(@Param("memberId") Long memberId,
                                                        @Param("year") Long year,
                                                        @Param("month") Long month);

    // ✅ 이미 올바름 - Top3 카테고리
    List<Top3CategoriesDTO> getTop3Categories(@Param("memberId") Long memberId,
                                              @Param("year") Long year,
                                              @Param("month") Long month);

    // 🔄 수정 필요 - year 파라미터 추가
    List<MonthlyCardTradeTotalDTO> getMonthlyCardTradeTotal(@Param("memberId") Long memberId,
                                                            @Param("year") Long year);

    // ✅ 현재 구조 유지 - 연간 데이터
    List<YearlyExpenseDTO> getYearlyTotalExpense(@Param("memberId") Long memberId);

    List<YearlyIncomeDTO> getYearlyIncomeFromCard(@Param("memberId") Long memberId);

    List<YearlyIncomeDTO> getYearlyIncomeFromAccount(@Param("memberId") Long memberId);

    // ✅ 현재 구조 유지 - 평균 데이터
    List<MonthlyIncomeAverageDTO> getMonthlyAverageIncome(@Param("memberId") Long memberId);

    List<MonthlyExpenseAverageDTO> getMonthlyExpenseAverage(@Param("memberId") Long memberId);

    MonthlyIncomeDTO getMonthlyIncome(@Param("memberId") Long memberId,
                                      @Param("year") Long year,
                                      @Param("month") Long month);

    List<TopCategoryByCountDTO> getTopCategoriesByCount(@Param("memberId") Long memberId,
                                                        @Param("year") Long year,
                                                        @Param("month") Long month);

    List<TopCategoryByAmountDTO> getTopCategoriesByAmount(@Param("memberId") Long memberId,
                                                          @Param("year") Long year,
                                                          @Param("month") Long month);

    Long getMonthlyExpenseSum(@Param("memberId") Long memberId,
                              @Param("year") Long year,
                              @Param("month") Long month);


    Long getMonthlyIncomeSum(@Param("memberId") Long memberId,
                             @Param("year") Long year,
                             @Param("month") Long month);

}



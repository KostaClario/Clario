package com.oopsw.clario.repository.statistics;

import com.oopsw.clario.dto.statistics.MonthlyCardTradeTotalDTO;
import com.oopsw.clario.dto.statistics.MonthlyExpenseTotalDTO;
import com.oopsw.clario.dto.statistics.Top3CategoriesDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatisticsRepository {
    public List<MonthlyExpenseTotalDTO> getMonthlyExpenseTotal(Long memberId);
    public List<Top3CategoriesDTO> getTop3Categories(Long memberId);
    public List<MonthlyCardTradeTotalDTO> getMonthlyCardTradeTotal(Long memberId);

//    public String getCustomerLogin(String customerId, String pw);
//    public int addCustomer(Customer customer);
//    public int updatePw(Map<String, String> param);
//    public int deleteCustomer(String customerId);
}

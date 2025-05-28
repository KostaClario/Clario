package com.oopsw.clario.repository;

import com.oopsw.clario.dto.dashboard.MemberDateDTO;
import com.oopsw.clario.dto.dashboard.TradeDTO;
import com.oopsw.clario.dto.dashboard.Top3CategoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface DashboardRepository {
    public String getMonthlyIncome(MemberDateDTO memberDateDTO);
    public String getMonthlyExpense(MemberDateDTO memberDateDTO);
    public int getTargetAssets(int memberId);
    public int getTotalAssets(int memberId);
    public boolean addTargetAssets(HashMap<String, Integer> targetAssets);
    public List<TradeDTO> getTodayExpense(MemberDateDTO memberDateDTO);
    public List<TradeDTO> getTodayIncome(MemberDateDTO memberDateDTO);
    public List<Top3CategoryDTO> getTop3Category(MemberDateDTO memberDateDTO);
    public List<TradeDTO> getYearsExpenses(MemberDateDTO memberDateDTO);
    public List<TradeDTO> getYearsIncomes(MemberDateDTO memberDateDTO);

}

package com.oopsw.clario.repository.history;

import com.oopsw.clario.dto.history.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface HistoryRepository {
    List<Map<String, Object>> accountList(@Param("memberId") Integer memberId);

    List<Map<String, Object>> cardList(Integer memberId);

    List<CardDetailDTO> cardDetail(Integer memberId);

    List<ExpenseHistoryDTO> expenseHistory(Map<String, Object> params);

    List<IncomeHistoryDTO> incomeHistory(@Param("memberId") Integer memberId);



    int income(IncomeDTO dto);

    int expense(ExpenseDTO dto);

    List<Map<String, Object>> categoryList();
}

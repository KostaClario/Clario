package com.oopsw.clario.repository.history;

import com.oopsw.clario.dto.history.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface HistoryRepository {
    List<Map<String, Object>> accountList(int memberId);

    List<Map<String, Object>> cardList(int memberId);

    List<CardDetailDTO> cardDetail(int memberId);

    List<ExpenseHistoryDTO> expenseHistory(Map<String, Object> params);

    List<IncomeHistoryDTO> incomeHistory(int memberId, String date);

    int income(IncomeDTO dto);

    int expense(ExpenseDTO dto);

    List<Map<String, Object>> categoryList();
}

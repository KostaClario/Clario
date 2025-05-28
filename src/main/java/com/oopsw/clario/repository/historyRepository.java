package com.oopsw.clario.repository;

import com.oopsw.clario.dto.expenseDto;
import com.oopsw.clario.dto.expenseHistoryDto;
import com.oopsw.clario.dto.incomeHistoryDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface historyRepository {
   List<String> accountList(int memberId);

   List<String> cardList(int memberId);

   List<Map<String, Object>> cardDetail(int cardTradeId);

   List<Map<String, Object>> expenseHistory(int memberId);

   List<Map<String, Object>> incomeHistory(int accountTradeId);

   int income(incomeHistoryDto dto);

   int expense(expenseDto dto);
}

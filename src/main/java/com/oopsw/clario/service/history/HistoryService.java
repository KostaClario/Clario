package com.oopsw.clario.service.history;


import com.oopsw.clario.dto.history.*;
import com.oopsw.clario.repository.history.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.smartcardio.Card;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;

    public List<Map<String, Object>> accountList(Integer memberId) {
        System.out.println("------계좌 서비스------");
        return historyRepository.accountList(memberId);
    }
    public List<Map<String, Object>> cardList(Integer memberId) {
        System.out.println("------카드 서비스-------");
        return historyRepository.cardList(memberId);
    }
    public CardDetailDTO cardDetail(int cardTradeId) {
        CardDetailDTO dto = historyRepository.cardDetail(cardTradeId);
        System.out.println("📦 DB 조회 결과: " + dto);
        return dto;
    }
    public List<IncomeHistoryDTO> incomeHistory(int memberId, String date) {
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("date", date);
        return historyRepository.incomeHistory(map);
    }

    public List<ExpenseHistoryDTO> expenseHistory(Map<String, Object> params) {
        return historyRepository.expenseHistory(params);
    }
    public int income(IncomeDTO dto){
        return historyRepository.income(dto);
    }
    public int expense(ExpenseDTO dto){
        System.out.println("결제 진행중"+dto);
        return historyRepository.expense(dto);
    }
    public List<Map<String,Object>> categoryList(){
        return historyRepository.categoryList();
    }
}


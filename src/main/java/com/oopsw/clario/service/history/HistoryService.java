package com.oopsw.clario.service.history;


import com.oopsw.clario.dto.history.*;
import com.oopsw.clario.repository.history.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;

    public List<Map<String, Object>> accountList(int memberId) {
        return historyRepository.accountList(memberId);
    }
    public List<Map<String, Object>> cardList(int memberId) {
        System.out.println("------카드 서비스-------");
        return historyRepository.cardList(memberId);
    }
    public List<CardDetailDTO> cardDetail(int memberId) {
        System.out.println("--------서비스--------");
        return historyRepository.cardDetail(memberId);
    }
    public List<IncomeHistoryDTO> incomeHistory(int memberId, String date) {
        return historyRepository.incomeHistory(memberId, date);
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


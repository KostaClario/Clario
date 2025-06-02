package com.oopsw.clario.service;

import com.oopsw.clario.dto.history.CardDetailDTO;
import com.oopsw.clario.dto.history.ExpenseDTO;
import com.oopsw.clario.service.history.HistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class HistroyServiceTest {
    @Autowired
    HistoryService historyService;

    @Test
    public void cardDetailsTest() {
        int cardTradeId = 1600;
        System.out.println(historyService.cardDetail(cardTradeId));
    }
    @Test
    public void expenseTest() {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setCardDay("2025-06-01");
        dto.setCardName("노리체크");
        dto.setCardStoreName("적금");
        dto.setCardMoney(500000L);
        dto.setCategoryName("기타");
        System.out.println(historyService.expense(dto));
    }

}

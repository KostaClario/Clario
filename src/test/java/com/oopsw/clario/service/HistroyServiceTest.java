package com.oopsw.clario.service;

import com.oopsw.clario.dto.history.*;
import com.oopsw.clario.service.history.HistoryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
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
    @Test
    public void testAccountList() {
        int testMemberId = 1; // 테스트용 memberId
        List<AccountDTO> result = historyService.accountList(testMemberId);
        System.out.println("✅ 계좌 목록:");
        result.forEach(System.out::println);
    }

    @Test
    public void testCardList() {
        int testMemberId = 1;
        List<CardDTO> result = historyService.cardList(testMemberId);
        System.out.println("✅ 카드 목록:");
        result.forEach(System.out::println);
    }
    @Test
    void income_정상입력_저장확인() {
        IncomeDTO dto = new IncomeDTO();
        dto.setMemberId(1); // 실제 회원 ID
        dto.setAccountDay("2025-06-03");
        dto.setSource("테스트입금");
        dto.setAccountMoney(10000L);
        dto.setAccountType("입금");
        dto.setBankAccountNum("123-456789-01234"); // 🔸 MyBank에 존재하는 계좌번호로 설정

        int result = historyService.income(dto);

        assertEquals(1, result); // 저장 성공 여부 확인
    }

}

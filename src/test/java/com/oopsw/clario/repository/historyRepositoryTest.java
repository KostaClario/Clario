package com.oopsw.clario.repository;


import com.oopsw.clario.dto.accountDto;
import com.oopsw.clario.dto.expenseDto;
import com.oopsw.clario.dto.incomeHistoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class historyRepositoryTest {
@Autowired
private historyRepository historyRepository;

    @Test
    public void acccountTest() {
        int memberId = 1;
        System.out.println(historyRepository.accountList(memberId));
    }
    @Test
    public void cardListTest() {
        int memberId = 1;
        System.out.println(historyRepository.cardList(memberId));
    }
    @Test
    public void cardDetailTest() {
        int cardTradeId = 1500;
        System.out.println(historyRepository.cardDetail(cardTradeId));
    }
    @Test
    public void expenseHistoryTest() {
        int memberId = 1;
        System.out.println(historyRepository.expenseHistory(memberId));
    }
    @Test
    public void incomeHistoryTest() {
        int accountTradeId = 1;
        System.out.println(historyRepository.incomeHistory(accountTradeId));
    }
    @Test
    public void incomeTest() {
        incomeHistoryDto dto = new incomeHistoryDto();
        dto.setAccountDay("2025-05-27");
        dto.setAccountMoney(3000000);
        dto.setSource("삼성(급여)");
        dto.setAccountType("입금");
        dto.setBankAccountNum("110-28-587412");
        System.out.println(dto);
    }
    @Test
    public void expenseTest() {
        expenseDto dto = new expenseDto();
        dto.setCardDay("2025-05-28");
        dto.setCardMoney(2000000);
        dto.setCardStoreName("삼성갤릭서 북5 pro");
        dto.setBusinessNum(null);
        dto.setIndustry(null);
        dto.setCardType("신용");
        dto.setCategoryId(6L);
        dto.setCardNum("4581-3097-2456-8721");
        System.out.println(dto);
        int result = historyRepository.expense(dto); 
        System.out.println("Insert 결과: " + result);
    }
}

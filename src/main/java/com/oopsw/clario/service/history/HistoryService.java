package com.oopsw.clario.service.history;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.oopsw.clario.dto.history.AccountDTO;
import com.oopsw.clario.dto.history.*;
import com.oopsw.clario.repository.history.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    public List<AccountDTO> accountList(Integer memberId) {
        System.out.println("------계좌 서비스 (JSON)------");
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new ClassPathResource("data/accounts.json").getInputStream();
            JsonNode root = mapper.readTree(is);
            JsonNode list = root.get("account_list");
            System.out.println("JSON 파싱 시작 (계좌)");
            return mapper.readValue(list.toString(), new TypeReference<List<AccountDTO>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<CardDTO> cardList(Integer memberId) {
        System.out.println("------카드 서비스 (JSON)-------");
        try {
            ObjectMapper mapper = new ObjectMapper();

            // ✅ 핵심: snake_case → camelCase 자동 변환 설정 추가
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

            InputStream is = new ClassPathResource("data/cards.json").getInputStream();
            JsonNode root = mapper.readTree(is);
            JsonNode list = root.get("card_list");

            System.out.println("JSON 파싱 시작 (카드)");

            return mapper.readValue(list.toString(), new TypeReference<List<CardDTO>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
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

    public int income(IncomeDTO dto) {
        System.out.println("💰 [입금 요청] " + dto);  // DTO 확인
        int result = historyRepository.income(dto);
        System.out.println("✅ [입금 처리 결과] result = " + result);  // 처리 결과 확인
        return result;
    }

    public int expense(ExpenseDTO dto) {
        System.out.println("결제 진행중" + dto);
        return historyRepository.expense(dto);
    }

    public List<Map<String, Object>> categoryList() {
        return historyRepository.categoryList();
    }
}
package com.oopsw.clario.controller.history;


import com.oopsw.clario.dto.history.*;
import com.oopsw.clario.service.history.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HistoryController {
    @Autowired
    HistoryService historyService;

    @GetMapping("/account/{memberId}")
    public List<Map<String, Object>> accountList(@PathVariable int memberId) {
        return historyService.accountList(memberId);
    }

    @GetMapping("/card/{memberId}")
    public List<Map<String, Object>> cardList(@PathVariable int memberId) {
        System.out.println("------카드 컨트롤러-------");
        return historyService.cardList(memberId);
    }

    @GetMapping("/cardDetail/{memberId}")
    public List<CardDetailDTO> cardDetail(@PathVariable int memberId) {
        System.out.println("------컨트롤러------");
        return historyService.cardDetail(memberId);
    }

    @GetMapping("/incomeHistory/{memberId}")
    public List<IncomeHistoryDTO> incomeHistory(@PathVariable int memberId,
                                                @RequestParam(required = false) String date) {
        return historyService.incomeHistory(memberId, date);
    }

    @GetMapping("/expenseHistory/{memberId}")
    public List<ExpenseHistoryDTO> expenseHistory(
            @PathVariable int memberId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String card
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("date", date);
        params.put("category", category);
        params.put("card", card);
        return historyService.expenseHistory(params);
    }

    @PostMapping("/income")
    public ResponseEntity<String> income(@RequestBody IncomeDTO dto) {
        int result = historyService.income(dto);

        if (result == 1) {
            return ResponseEntity.ok("입금 성공");
        } else {
            return ResponseEntity.status(500).body("입금 실패");
        }
    }

    @PostMapping("/expense")
    public ResponseEntity<ExpenseDTO> expense(@RequestBody ExpenseDTO dto) {
        System.out.println("✅ 컨트롤러 도착: " + dto);

        try {
            int result = historyService.expense(dto);
            System.out.println("🟢 저장 결과: " + result);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("❌ 저장 중 예외 발생");
            e.printStackTrace(); // 이게 IntelliJ 콘솔에 나옴
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/category")
    public List<Map<String, Object>> categoryList() {
        return historyService.categoryList();
    }
}

package com.oopsw.clario.controller.card;

import com.oopsw.clario.dto.card.AllCardsDTO;
import com.oopsw.clario.service.card.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardRestController {
    private final CardService cardService;

    @GetMapping("/all") //모든 카드 조회
    public ResponseEntity<List<AllCardsDTO>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @PostMapping("/filter") //상위 카테고리 리스트 받아서 필터링된 카드 반환
    public ResponseEntity<List<AllCardsDTO>> getCardsByFilter(
            @RequestBody List<String> parentNames) {
        return ResponseEntity.ok(cardService.getCardsByParentCategories(parentNames));
    }
}

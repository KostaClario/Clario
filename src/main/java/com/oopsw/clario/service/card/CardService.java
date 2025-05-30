package com.oopsw.clario.service.card;

import com.oopsw.clario.dto.card.AllCardsDTO;
import com.oopsw.clario.repository.card.CardRepository;
import com.oopsw.clario.util.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    public List<AllCardsDTO> getAllCards() {
        List<AllCardsDTO> result = cardRepository.getAllCards();
        return result;
    }

//    public List<AllCardsDTO> getCardsByParentCategories(List<String> parentCategories) {
//        List<String> subCategories = CategoryMapper.getChildCategories(parentCategories);
//        return cardRepository.getCardsByCategoryNames(subCategories);
//    }

    public List<AllCardsDTO> getCardsByParentCategoriesAndType(List<String> parentCategories, String cardType) {
        List<String> subCategories = new ArrayList<>();
        if (parentCategories != null && !parentCategories.isEmpty()) {
            subCategories = CategoryMapper.getChildCategories(parentCategories);
        }
        return cardRepository.getCardsByCategoryNamesAndType(subCategories, cardType);
    }








}

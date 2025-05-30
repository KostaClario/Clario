package com.oopsw.clario.service.card;

import com.oopsw.clario.dto.card.AllCardsDTO;
import com.oopsw.clario.dto.statistics.CategoryStatisticsDTO;
import com.oopsw.clario.dto.statistics.MonthlyExpenseComparisonDTO;
import com.oopsw.clario.repository.card.CardRepository;
import com.oopsw.clario.service.statistics.StatisticsService;
import com.oopsw.clario.util.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    private final StatisticsService statisticsService;

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

    public CategoryStatisticsDTO getCategoryStatistics(Integer memberId, Long year, Long month) {
        return statisticsService.getCategoryStatistics(memberId, year, month);
    }

    public Long getMonthlyExpenseSum(Integer memberId, Long year, Long month) {
        return statisticsService.getMonthlyExpenseSum(memberId, year, month);
    }

    public MonthlyExpenseComparisonDTO getMonthlyExpenseComparison(Integer memberId, Long year, Long month) {
        return statisticsService.getMonthlyExpenseComparison(memberId, year, month);
    }








}

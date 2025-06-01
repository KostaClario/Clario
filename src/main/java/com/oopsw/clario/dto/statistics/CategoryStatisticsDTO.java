package com.oopsw.clario.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryStatisticsDTO {
    private List<TopCategoryByCountDTO> countBased;
    private List<TopCategoryByAmountDTO> amountBased;
}

package com.oopsw.clario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class expenseHistoryDto {
    private String cardDay;
    private String cardStoreName;
    private int cardMoney;
    private String businessNum;
    private String categoryName;
    private String cardName;
}

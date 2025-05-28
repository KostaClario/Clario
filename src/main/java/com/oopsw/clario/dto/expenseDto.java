package com.oopsw.clario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class expenseDto {
    private String cardDay;
    private int cardMoney;
    private String cardStoreName;
    private String businessNum;
    private String industry;
    private String cardType;
    private Long categoryId;
    private String cardNum;
}

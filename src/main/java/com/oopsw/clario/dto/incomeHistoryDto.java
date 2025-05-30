package com.oopsw.clario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class incomeHistoryDto {
    private String accountDay;
    private int accountMoney;
    private String source;
    private String bankAccountNum;
    private String bankName;
    private String accountType;
}

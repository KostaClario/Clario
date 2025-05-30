package com.oopsw.clario.dto.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IncomeDto {
    private String accountDay;
    private String accountName;
    private String source;
    private int accountMoney;
    private String accountType;
    private String bankName;
    private String bankAccountNum;
}

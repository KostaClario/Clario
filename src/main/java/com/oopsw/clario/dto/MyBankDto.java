package com.oopsw.clario.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyBankDto {
    private String bankAccountNum;
    private int balance;
    private String bankAccountName;
    private String bankName;
}

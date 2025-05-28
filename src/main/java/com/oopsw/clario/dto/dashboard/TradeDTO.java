package com.oopsw.clario.dto.dashboard;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
public class TradeDTO {
    private int memberId;
    private String cardStoreName;
    private int cardTradeMoney;
    private String accountSource;
    private int accountTradeMoney;
    private String cardTradeMonth;
    private String accountTradeMonth;
}

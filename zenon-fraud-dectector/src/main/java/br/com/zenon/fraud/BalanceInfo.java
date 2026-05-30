package br.com.zenon.fraud;

import java.math.BigDecimal;

public record BalanceInfo(String customer,
                          BigDecimal oldAmount,
                          BigDecimal newAmount) {

}

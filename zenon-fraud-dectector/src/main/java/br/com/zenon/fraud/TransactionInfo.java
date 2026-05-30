package br.com.zenon.fraud;

import java.math.BigDecimal;

public record TransactionInfo(Integer step,
                              TransactionType type,
                              BigDecimal amount,
                              Boolean isFraud,
                              Boolean isFlaggedFraud) {

}

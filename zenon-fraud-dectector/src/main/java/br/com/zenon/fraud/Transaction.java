package br.com.zenon.fraud;

public record Transaction(TransactionInfo info,
                          BalanceInfo origin,
                          BalanceInfo destination) {

}

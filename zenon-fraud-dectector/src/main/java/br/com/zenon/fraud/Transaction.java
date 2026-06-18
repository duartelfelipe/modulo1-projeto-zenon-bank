package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(Integer step,
                          TransactionType type,
                          BigDecimal amount,
                          Boolean isFraud,
                          Boolean isFlaggedFraud,
                          TransactionCustomer origin,
                          TransactionCustomer destination) {

    public Transaction {
        Objects.requireNonNull(step);
        Objects.requireNonNull(type);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(isFraud);
        Objects.requireNonNull(isFlaggedFraud);
        Objects.requireNonNull(origin);
        Objects.requireNonNull(destination);

        if (step <= 0) throw new IllegalArgumentException("Step should be positive.");
        if (amount.signum() < 0) throw new IllegalArgumentException("Invalid amount.");
    }

}

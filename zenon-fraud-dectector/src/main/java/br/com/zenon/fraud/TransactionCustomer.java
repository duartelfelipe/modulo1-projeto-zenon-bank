package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Objects;

public record TransactionCustomer(String customer,
                                  BigDecimal oldAmount,
                                  BigDecimal newAmount) {

    public TransactionCustomer {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(oldAmount);
        Objects.requireNonNull(newAmount);

        if (customer.trim().isEmpty()) throw new IllegalArgumentException("Invalid customer name.");
        if (oldAmount.signum() < 0) throw new IllegalArgumentException("Invalid old amount.");
        if (newAmount.signum() < 0) throw new IllegalArgumentException("Invalid new amount.");
    }
}

package br.com.zenon.fraud;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> findByOriginCustomer(String name);

}

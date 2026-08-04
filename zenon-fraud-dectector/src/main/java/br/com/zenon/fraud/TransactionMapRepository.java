package br.com.zenon.fraud;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {

    private final Map<String, Transaction> transactionsByOriginName;

    public TransactionMapRepository(List<Transaction> transactions ) {
        Objects.requireNonNull(transactions);
        this.transactionsByOriginName =
                transactions
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        trx -> trx.origin().customer(),
                                        Function.identity()
                                )
                        );
    }

    @Override
    public Optional<Transaction> findByOriginCustomer(String name) {
        long start = System.nanoTime();

        Optional<Transaction> optTrx = Optional.ofNullable(
                transactionsByOriginName.get(name)
        );

        long end = System.nanoTime();
        IO.println("Tempo <Map> (ms): " + (end - start) / 1_000_000.0);

        return optTrx;
    }

    @Override
    public void save(Transaction transaction) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void saveBatch(List<Transaction> transactions) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void saveThreadBatch(List<Transaction> transactions) {
        throw new UnsupportedOperationException("");
    }

}

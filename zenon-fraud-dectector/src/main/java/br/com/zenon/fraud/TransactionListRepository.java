package br.com.zenon.fraud;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransactionListRepository implements  TransactionRepository{

    private final List<Transaction> transactions;

    public TransactionListRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactions = transactions;
    }

    @Override
    public Optional<Transaction> findByOriginCustomer(String name) {
        long start = System.nanoTime();

        Optional<Transaction> optTrx =  transactions
                .stream()
                .filter(trx -> trx.origin().customer().equals(name))
                .findFirst();

        long end = System.nanoTime();
        IO.println("Tempo <List> (ms): " + (end-start) / 1_000_000.0);

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

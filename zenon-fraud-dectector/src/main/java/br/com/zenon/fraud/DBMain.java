package br.com.zenon.fraud;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class DBMain {


    static void main() throws IOException {

        var database = new TransactionSQLRepository();
        database.findByOriginCustomer("C1280821776").ifPresent(IO::println);

        long start = System.nanoTime();
        List<Transaction> transactions = new TransactionIngestor().loadTransactions();
        long end = System.nanoTime();
        IO.println("Tempo carga ingestao (ms): " + (end - start) / 1_000_000.0);

//        start = System.nanoTime();
//        transactions.forEach(
//                database::save
//        );
//        end = System.nanoTime();
//        IO.println("Tempo insert sequencial (ms): " + (end - start) / 1_000_000.0);

        start = System.nanoTime();
        database.saveBatch(transactions);
        end = System.nanoTime();
        IO.println("Tempo insert batch (ms): " + (end - start) / 1_000_000.0);

        start = System.nanoTime();
        database.saveThreadBatch(transactions);
        end = System.nanoTime();
        IO.println("Tempo insert thread batch (ms): " + (end - start) / 1_000_000.0);
    }
}

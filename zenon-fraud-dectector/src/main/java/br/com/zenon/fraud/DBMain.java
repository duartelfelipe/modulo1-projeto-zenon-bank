package br.com.zenon.fraud;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class DBMain {


    static void main() throws IOException {

        var database = new TransactionSQLRepository();
        database.findByOriginCustomer("C1280821776").ifPresent(IO::println);

        long start = System.nanoTime();
        new TransactionIngestor().loadTransactions()
                .forEach(
                        database::save
                );
        long end = System.nanoTime();
        IO.println("Tempo (ms): " + (end - start) / 1_000_000.0);
    }


}

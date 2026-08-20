package br.com.zenon.fraud;

import java.io.IOException;
import java.util.List;

public class IngertorMain {


    static void main() throws IOException {

        var database = new TransactionSQLRepository();

        long start = System.nanoTime();
        new EfficientTransactionIngestor().loadTransactions(database::saveBatch);
        long end = System.nanoTime();
        IO.println("Tempo carga ingestao (ms): " + (end - start) / 1_000_000.0);
    }
}

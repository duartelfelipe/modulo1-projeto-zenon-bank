package br.com.zenon.fraud;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EfficientTransactionIngestor {

    private static final String FILE_NAME = "data/paysim_log.csv";
    //    private static final int BATCH_SIZE = 10;
    private static final int BATCH_SIZE = 10_000;
    private static final int JDBC_BATCH_SIZE = 5000;

    public void loadTransactions(Consumer<List<Transaction>> consumer) {
        Path path = Path.of(FILE_NAME);


        try (ExecutorService executor = Executors.newFixedThreadPool(10);
             Stream<String> lines = Files.lines(path).skip(1)/*.limit(BATCH_SIZE)*/) {

            var iterator = lines.iterator();
            List<String> linesBatch = new ArrayList<>(JDBC_BATCH_SIZE);

            while (iterator.hasNext()) {
                String line = iterator.next();
                linesBatch.add(line);

                if (linesBatch.size() >= JDBC_BATCH_SIZE) {
                    List<String> cloneBatch = List.copyOf(linesBatch);
                    executor.submit(() -> executeBatch(cloneBatch, consumer));
                    linesBatch.clear();
                }
            }

            if (!linesBatch.isEmpty()) {
                List<String> cloneBatch = List.copyOf(linesBatch);
                executor.submit(() -> executeBatch(cloneBatch, consumer));
            }

        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error reading file: " + FILE_NAME, ex);
        }
    }

    private void executeBatch(List<String> linesBatch, Consumer<List<Transaction>> consumer) {
        List<Transaction> transactionsBatch = linesBatch.stream()
                .map(TransactionConverter::parseTransaction)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        consumer.accept(transactionsBatch);
    }
}

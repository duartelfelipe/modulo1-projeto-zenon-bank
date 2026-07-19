package br.com.zenon.fraud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {

    private static final String FILE_NAME = "data/paysim_log.csv";
    private static final int BATCH_SIZE = 100_000;

    public List<Transaction>  loadTransactions() {
        Path path = Path.of(FILE_NAME);

        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
//                    .limit(BATCH_SIZE)
                    .map(TransactionConverter::parseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error reading file: " + FILE_NAME, ex);
        }
    }


}

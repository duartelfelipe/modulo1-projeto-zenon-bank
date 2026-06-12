package br.com.zenon.fraud;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TransactionIngestor {

    private final String fileName;

    TransactionIngestor(String fileName) {
        this.fileName = fileName;
    }

    public List<Transaction> ingest() {
        try {

            Path path = Path.of(fileName);
            return Files.readAllLines(path).stream()
                    .skip(1)
                    .limit(1000)
                    .map(TransactionConverter::csvLineToTransaction)
                    .toList();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

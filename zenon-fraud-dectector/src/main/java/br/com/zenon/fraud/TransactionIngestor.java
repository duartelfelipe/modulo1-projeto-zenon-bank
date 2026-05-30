package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TransactionIngestor {

    private final String fileName;

    TransactionIngestor(String fileName) {
        this.fileName = fileName;
    }

    public List<Transaction> ingest() {
        List<Transaction> transactions = new ArrayList<>();

        try {
            Path path = Paths.get(fileName);
            BufferedReader br = new BufferedReader(new FileReader(path.toFile()));

            int count = 0;
            while ((br.readLine() != null) && (count <= 1000)) {
                if (count > 0) {
                    transactions.add(
                            TransactionConverter.csvLineToTransaction(br.readLine())
                    );
                }
                count++;
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return transactions;
    }
}

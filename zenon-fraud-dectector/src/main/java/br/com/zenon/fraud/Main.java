package br.com.zenon.fraud;

import java.io.IOException;
import java.util.List;

public class Main {

    private static final String FILE_NAME = "data/paysim_log.csv";
    private static final int BATCH_SIZE = 100000;

    void main() throws IOException {
        var ingestor = new TransactionIngestor(FILE_NAME, BATCH_SIZE);
        List<Transaction> transactions = ingestor.loadTransactions();
        FraudAnalyzer.analyze(transactions);
    }
}

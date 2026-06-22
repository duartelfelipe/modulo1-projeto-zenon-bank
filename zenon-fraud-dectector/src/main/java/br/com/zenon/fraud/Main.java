package br.com.zenon.fraud;

import java.io.IOException;
import java.util.List;

public class Main {



    void main() throws IOException {
        var ingestor = new TransactionIngestor();
        List<Transaction> transactions = ingestor.loadTransactions();

        var fraudAnalyzer = new FraudAnalyzer(transactions);
        fraudAnalyzer.analyze();;
    }
}

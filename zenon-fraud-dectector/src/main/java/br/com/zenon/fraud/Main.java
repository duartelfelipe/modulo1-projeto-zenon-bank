package br.com.zenon.fraud;

import java.util.List;

public class Main {


    void main() {
        var ingestor = new TransactionIngestor();
        List<Transaction> transactions = ingestor.loadTransactions();

        var fraudAnalyzer = new FraudAnalyzer(transactions);
        fraudAnalyzer.analyze();

        TransactionRepository repository = new TransactionListRepository(transactions);


        repository
                .findByOriginCustomer("C1231006815")
                .ifPresentOrElse(IO::println, () -> IO.println("Cliente não encontrado"));
        repository
                .findByOriginCustomer("C1868032458")
                .ifPresentOrElse(IO::println, () -> IO.println("Cliente não encontrado"));
        repository
                .findByOriginCustomer("C12")
                .ifPresentOrElse(IO::println, () -> IO.println("Cliente não encontrado"));


        repository = new TransactionMapRepository(transactions);
        repository
                .findByOriginCustomer("C1231006815")
                .ifPresentOrElse(IO::println, () -> IO.println("Cliente não encontrado"));
        repository
                .findByOriginCustomer("C1868032458")
                .ifPresentOrElse(IO::println, () -> IO.println("Cliente não encontrado"));
        repository
                .findByOriginCustomer("C12")
                .ifPresentOrElse(IO::println, () -> IO.println("Cliente não encontrado"));




    }
}

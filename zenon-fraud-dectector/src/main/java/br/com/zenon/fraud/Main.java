package br.com.zenon.fraud;

import java.util.List;

public class Main {

    private static final String FILE_NAME = "data/paysim_log.csv";

    void main() {
        new TransactionIngestor(FILE_NAME)
                .ingest()
                .forEach(IO::println);
    }
}

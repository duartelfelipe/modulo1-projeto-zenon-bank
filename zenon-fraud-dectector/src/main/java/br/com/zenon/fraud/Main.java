package br.com.zenon.fraud;

import java.io.IOException;

public class Main {

//    private static final String FILE_NAME = "data/paysim_log.csv";
    private static final String FILE_NAME = "data/paysim_with_bad_data.csv";

    void main() throws IOException {
        var trx = new TransactionIngestor(FILE_NAME);
        trx.process();
        trx.report();
    }
}

package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {

    private final String fileName;
    private final int batchSize;
    private final List<Transaction> transactions;
    private final List<String> loadErrors;



    TransactionIngestor(String fileName, int batchSize) {
        this.fileName = fileName;
        this.batchSize = batchSize;
        this.transactions = new ArrayList<>();
        this.loadErrors = new ArrayList<>();
    }

    public List<Transaction> loadTransactions() throws IOException {
        FileReader fr = getFileReader();

        try (BufferedReader br = new BufferedReader(fr)) {

            int count = 0;
            String line;

            while (((line = br.readLine()) != null) && (count <= batchSize)) {
                if (count > 0) {
                    try {
                        transactions.add(
                                processLine(line)
                                        .orElseThrow(() -> new RuntimeException("Unexpected error processing line."))
                        );
                    } catch (Exception ex) {
                        String error = String.format("Error: %s -> %s", ex.getMessage(), line);
                        System.err.println(error);
                        loadErrors.add(error);
                    }
                }
                count++;
            }

            return transactions;

        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error loading transactions.");
        }
    }

    private FileReader getFileReader() throws IOException {
        return new FileReader(
                Paths.get(fileName).toFile()
        );
    }

    private Optional<Transaction> processLine(String line) {
        String[] lineChunks = TransactionConverter.strLineToArray(line);
        TransactionValidator.validateLineChunksSize(lineChunks);

        int i = 0;
        String strStep = lineChunks[i++];
        String strType = lineChunks[i++];
        String strAmount = lineChunks[i++];
        String strNameOrig = lineChunks[i++];
        String strOldBalanceOrig = lineChunks[i++];
        String strNewBalanceOrig = lineChunks[i++];
        String strNameDest = lineChunks[i++];
        String strPldBalanceDest = lineChunks[i++];
        String strNewBalanceDest = lineChunks[i++];
        String strIsFraud = lineChunks[i++];
        String strIsFlaggedFraud = lineChunks[i++];

        TransactionValidator.validateFields(
                strStep,
                strType,
                strAmount,
                strNameOrig,
                strOldBalanceOrig,
                strNewBalanceOrig,
                strNameDest,
                strPldBalanceDest,
                strNewBalanceDest,
                strIsFraud,
                strIsFlaggedFraud
        );

        return TransactionConverter.toTransaction(
                strStep,
                strType,
                strAmount,
                strNameOrig,
                strOldBalanceOrig,
                strNewBalanceOrig,
                strNameDest,
                strPldBalanceDest,
                strNewBalanceDest,
                strIsFraud,
                strIsFlaggedFraud);
    }

//    public void report() {
//        IO.println("---Errors---");
//        loadErrors.forEach(IO::println);
//        IO.println("Total: " + loadErrors.size());
//        IO.println();
//        IO.println("---Success---");
//        transactions.forEach(trx -> IO.println(trx.toString()));
//        IO.println("Total: " + transactions.size());
//    }
}

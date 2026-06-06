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
    private final List<Transaction> success;
    private final List<String> fail;

    TransactionIngestor(String fileName) {
        this.fileName = fileName;
        this.success = new ArrayList<>();
        this.fail = new ArrayList<>();
    }

    public void process() throws IOException {
        FileReader fr = getFileReader();

        try (BufferedReader br = new BufferedReader(fr)) {

            int count = 0;
            String line;
            while (((line = br.readLine()) != null)) {
                if (count > 0) {
                    try {
                        success.add(
                                processLine(line)
                                        .orElseThrow(() -> new RuntimeException("Unexpected error."))
                        );
                    } catch (Exception ex) {
                        String error = String.format("Error: %s -> %s", ex.getMessage(), line);
                        System.err.println(error);
                        fail.add(error);
                    }
                }
                count++;
            }
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

    public void report() {
        IO.println("---Errors---");
        fail.forEach(IO::println);
        IO.println("Total: " + fail.size());
        IO.println();
        IO.println("---Success---");
        success.forEach(trx -> IO.println(trx.toString()));
        IO.println("Total: " + success.size());
    }
}

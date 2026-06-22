package br.com.zenon.fraud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {

    private static final String FILE_NAME = "data/paysim_log.csv";
    private static final int BATCH_SIZE = 50_000;

    public List<Transaction>  loadTransactions() throws IOException {
        Path path = Path.of(FILE_NAME);

        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
                    .limit(BATCH_SIZE)
                    .map(this::processLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error reading file: " + FILE_NAME, ex);
        }
    }

    private Optional<Transaction> processLine(String line) {
        try {
            String[] lineChunks = TransactionConverter.strLineToArray(line);
            validateLineChunksSize(lineChunks);

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

            return TransactionConverter.parseTransaction(
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
        } catch (Exception ex) {
            System.err.println("Parse error: " + line + " | " + ex);
            return Optional.empty();
        }
    }

    private static void validateLineChunksSize(String[] line) {
        if (line.length != 11) {
            throw new IllegalArgumentException("Invalid line.");
        }
    }
}

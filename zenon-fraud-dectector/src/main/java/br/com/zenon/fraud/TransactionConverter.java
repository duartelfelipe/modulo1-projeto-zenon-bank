package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Optional;

public class TransactionConverter {

    public static Optional<Transaction> toTransaction(String strStep,
                                                      String strType,
                                                      String strAmount,
                                                      String strNameOrig,
                                                      String strOldbalanceOrg,
                                                      String strNewbalanceOrig,
                                                      String strNameDest,
                                                      String strPldbalanceDest,
                                                      String strNewbalanceDest,
                                                      String strIsFraud,
                                                      String strIsFlaggedFraud) {
        Integer step = Integer.parseInt(strStep);
        TransactionType type = TransactionType.valueOf(strType);
        BigDecimal amount = new BigDecimal(strAmount);
        Boolean isFraud = "1".equals(strIsFraud);
        Boolean isFlaggedFraud = "1".equals(strIsFlaggedFraud);

        BigDecimal oldAmountOrig = new BigDecimal(strOldbalanceOrg);
        BigDecimal newAmountOrig = new BigDecimal(strNewbalanceOrig);

        BigDecimal oldAmountDest = new BigDecimal(strPldbalanceDest);
        BigDecimal newAmountDest = new BigDecimal(strNewbalanceDest);

        TransactionInfo info = new TransactionInfo(step, type, amount, isFraud, isFlaggedFraud);
        BalanceInfo origin = new BalanceInfo(strNameOrig, oldAmountOrig, newAmountOrig);
        BalanceInfo destination = new BalanceInfo(strNameDest, oldAmountDest, newAmountDest);

        return Optional.of(new Transaction(info, origin, destination));
    }

    public static String[] strLineToArray(String line) {
        return line.split(",");
    }
}

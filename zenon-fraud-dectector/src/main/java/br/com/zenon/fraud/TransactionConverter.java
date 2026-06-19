package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Optional;

public class TransactionConverter {

    public static String[] strLineToArray(String line) {
        return line.split(",");
    }

    public static Optional<Transaction> parseTransaction(String strStep,
                                                         String strType,
                                                         String strAmount,
                                                         String strNameOrig,
                                                         String strOldBalanceOrg,
                                                         String strNewBalanceOrig,
                                                         String strNameDest,
                                                         String strOldBalanceDest,
                                                         String strNewBalanceDest,
                                                         String strIsFraud,
                                                         String strIsFlaggedFraud) {
        Integer step = parseStep(strStep);
        TransactionType type = parseTrxType(strType);
        BigDecimal amount = parseAmount(strAmount, "amount");
        Boolean isFraud = parseBoolean(strIsFraud, "isFraud");
        Boolean isFlaggedFraud = parseBoolean(strIsFlaggedFraud, "isFlaggedFraud");

        BigDecimal oldAmountOrig = parseAmount(strOldBalanceOrg, "oldAmountOrig");
        BigDecimal newAmountOrig = parseAmount(strNewBalanceOrig, "newAmountOrig");

        BigDecimal oldAmountDest = parseAmount(strOldBalanceDest, "oldAmountDest");
        BigDecimal newAmountDest = parseAmount(strNewBalanceDest, "newAmountDest");

        TransactionCustomer origin = new TransactionCustomer(strNameOrig, oldAmountOrig, newAmountOrig);
        TransactionCustomer destination = new TransactionCustomer(strNameDest, oldAmountDest, newAmountDest);

        return Optional.of(new Transaction(step, type, amount, isFraud, isFlaggedFraud, origin, destination));
    }

    private static Integer parseStep(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid step");
        }
    }

    private static TransactionType parseTrxType(String value) {
        try {
            return TransactionType.valueOf(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid trx type.");
        }
    }

    private static BigDecimal parseAmount(String value, String field) {
        try {
            return new BigDecimal(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Invalid %s", field));
        }
    }

    private static Boolean parseBoolean(String value, String field) {
        try {
//            if (!"1".equals(value) && !"0".equals(value)) {
//                throw new IllegalArgumentException();
//            }
            return "1".equals(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Invalid %s", field));
        }
    }

}

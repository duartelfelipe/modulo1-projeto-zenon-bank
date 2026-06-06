package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class TransactionValidator {

    public static void validateLineChunksSize(String[] line) {
        if (line.length != 11) {
            throw new IllegalArgumentException("Invalid line.");
        }
    }


    public static void validateFields(String strStep,
                                      String strType,
                                      String strAmount,
                                      String strNameOrig,
                                      String strOldBalanceOrig,
                                      String strNewBalanceOrig,
                                      String strNameDest,
                                      String strOldBalanceDest,
                                      String strNewBalanceDest,
                                      String strIsFraud,
                                      String strIsFlaggedFraud) {
        validateTrxStep(strStep);
        validateTrxType(strType);

        validateAmount(strAmount, "amount");

        validateString(strNameOrig, "nameOrig");
        validateAmount(strOldBalanceOrig, "oldBalanceOrig");
        validateAmount(strNewBalanceOrig, "newBalanceOrig");

        validateString(strNameDest, "nameDest");
        validateAmount(strOldBalanceDest, "oldBalanceDest");
        validateAmount(strNewBalanceDest, "newBalanceDest");

        validateBoolean(strIsFraud, "isFraud");
        validateBoolean(strIsFlaggedFraud, "isFlaggedFraud");
    }

    private static void validateTrxStep(String value) {
        try {
            if (Integer.parseInt(value) < 1)
                throw new IllegalArgumentException("Step < 1");
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid step");
        }
    }

    private static void validateTrxType(String value) {
        try {
            TransactionType.valueOf(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid trx type.");
        }
    }

    private static void validateAmount(String value, String field) {
        try {
            if ((new BigDecimal(value).compareTo(new BigDecimal(0)) < 0))
                throw new IllegalArgumentException();
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Invalid %s", field));
        }
    }

    private static void validateBoolean(String value, String field) {
        try {
            Boolean.parseBoolean(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Invalid %s", field));
        }
    }

    private static void validateString(String value, String field) {
        if ("".equals(value))
            throw new IllegalArgumentException(String.format("Invalid %s", field));
    }
}

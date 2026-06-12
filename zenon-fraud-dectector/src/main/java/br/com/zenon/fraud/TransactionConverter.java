package br.com.zenon.fraud;

import java.math.BigDecimal;

public class TransactionConverter {

    public static Transaction toTransaction(String strStep,
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

        return new Transaction(info, origin, destination);

    }

    public static Transaction csvLineToTransaction(String strLine) {
        String[] line = strLine.split(",");
        return toTransaction(
                line[0],
                line[1],
                line[2],
                line[3],
                line[4],
                line[5],
                line[6],
                line[7],
                line[8],
                line[9],
                line[10]
        );
    }

}

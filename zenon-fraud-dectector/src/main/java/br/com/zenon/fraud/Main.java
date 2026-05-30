package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    void main() {
        List.of(t1(), t2()).forEach(IO::println);
    }

    private Transaction t1() {
        return createTransaction(
                "1",
                "PAYMENT",
                "9839.64",
                "C1231006815",
                "170136.0",
                "160296.36",
                "M1979787155",
                "0.0",
                "0.0",
                "0",
                "0"
        );
    }

    private Transaction t2() {
        return createTransaction(
                "743",
                "CASH_OUT",
                "850002.52",
                "C1280323807",
                "850002.52",
                "0.0",
                "C873221189",
                "6510099.11",
                "7360101.63",
                "1",
                "0"
        );
    }

    private Transaction createTransaction(String strStep,
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
        Boolean isFraud = Boolean.parseBoolean(strIsFraud);
        Boolean isFlaggedFraud = Boolean.parseBoolean(strIsFlaggedFraud);

        BigDecimal oldAmountOrig = new BigDecimal(strOldbalanceOrg);
        BigDecimal newAmountOrig = new BigDecimal(strNewbalanceOrig);

        BigDecimal oldAmountDest = new BigDecimal(strPldbalanceDest);
        BigDecimal newAmountDest = new BigDecimal(strNewbalanceDest);

        TransactionInfo info = new TransactionInfo(step, type, amount, isFraud, isFlaggedFraud);
        BalanceInfo origin = new BalanceInfo(strNameOrig, oldAmountOrig, newAmountOrig);
        BalanceInfo destination = new BalanceInfo(strNameDest, oldAmountDest, newAmountDest);

        return new Transaction(info, origin, destination);
    }

}

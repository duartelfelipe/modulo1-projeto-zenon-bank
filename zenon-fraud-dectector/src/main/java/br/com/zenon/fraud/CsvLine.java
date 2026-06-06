package br.com.zenon.fraud;

import java.math.BigDecimal;

public record CsvLine(String strStep,
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
}

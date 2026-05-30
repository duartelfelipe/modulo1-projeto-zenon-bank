package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    void main() {
        List.of(t1(), t2()).forEach(IO::println);
    }

    private Transaction t1() {
        return TransactionConverter.toTransaction(
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
        return TransactionConverter.toTransaction(
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
}

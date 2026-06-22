package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FraudAnalyzer {

    private final List<Transaction> transactions;

    public FraudAnalyzer(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactions = transactions;
    }

    public void analyze() {
        DecimalFormat df = new DecimalFormat("#,##0.00");

        IO.println(String.format("1. Total  de Fraudes: %d", countFrauds()));

        IO.println("2. Top 3 Fraudes de Maior Valor:");
        List<BigDecimal> topAmountFrauds = getTopAmountFrauds(3);
        topAmountFrauds.stream().map(df::format).forEach(IO::println);

        IO.println("3. Clientes suspeitos:");
        getTopFraudulentCustomers(5).forEach(IO::println);

        IO.println("4. Prejuizo total: " + df.format(sumFraudsAmount()));

        IO.println("5. Fraudes por Tipo:");
        countFraudsByType().forEach((k, v) -> IO.println(" - " + k + ": " + v));
    }

    private long countFrauds() {
        return fraudsStream()
                .count();
    }

    private List<BigDecimal> getTopAmountFrauds(int limit) {
        return fraudsStream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(Transaction::amount)
                .limit(limit)
                .toList();
    }

    private List<String> getTopFraudulentCustomers(int limit) {
        return fraudsStream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(trx -> trx.origin().customer())
                .distinct()
                .limit(limit)
                .toList();
    }


    private BigDecimal sumFraudsAmount() {
//        return new BigDecimal(transactions.stream()
//                .filter(Transaction::isFraud)
//                .mapToDouble(trx -> trx.amount().doubleValue())
//                .sum());
        return fraudsStream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<TransactionType, Long> countFraudsByType() {
        return fraudsStream()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));
    }

    private Stream<Transaction> fraudsStream() {
        return transactions.stream()
                .filter(Transaction::isFraud);
    }

}

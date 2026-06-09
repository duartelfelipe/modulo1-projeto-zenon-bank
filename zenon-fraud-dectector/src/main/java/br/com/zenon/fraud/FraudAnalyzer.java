package br.com.zenon.fraud;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    public static void analyze(List<Transaction> transactions) {
        List<Transaction> frauds = transactions.stream()
                .filter(trx -> trx.info().isFraud())
                .toList();

        long totalFrauds = frauds.size();
        IO.println(String.format("1. Total de Fraudes: %d", totalFrauds));

        DecimalFormat df = new DecimalFormat("#,##0.00");
        List<String> top3MaxAmount = transactions.stream()
                .filter(trx -> trx.info().isFraud())
                .map(trx -> trx.info().amount().abs())
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .map(df::format)
                .toList();
        IO.println("2. Top 3 Fraudes de Maior Valor:");
        top3MaxAmount.forEach(IO::println);

        Set<String> topCustomers = transactions.stream()
                .filter(trx -> trx.info().isFraud())
                .sorted(Comparator.comparing(Transaction::info, Comparator.comparing(TransactionInfo::amount)).reversed())
                .map(trx -> trx.origin().customer())
                .limit(5)
                .collect(Collectors.toSet());
        IO.println("3. Clientes suspeitos:");
        topCustomers.forEach(IO::println);

        double sumFraudsAmount = transactions.stream()
                .filter(trx -> trx.info().isFraud())
                .mapToDouble(trx -> trx.info().amount().doubleValue())
                .sum();
        IO.println("4. Prejuizo total: " + df.format(sumFraudsAmount));

        IO.println("5. Fraudes por Tipo:");
        transactions.stream()
                .filter(trx -> trx.info().isFraud())
                .collect(Collectors.groupingBy(
                        trx -> trx.info().type(),
                        Collectors.counting()
                ))
                .forEach((k, v) -> IO.println(" - " + k + ": " + v));
    }

}

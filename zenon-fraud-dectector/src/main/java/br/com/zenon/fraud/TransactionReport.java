package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class TransactionReport {

    private static final String FILE_PATH = "data/paysim_log.csv";

    public record Summary(long totalLines, long totalFrauds, BigDecimal totalAmount) {
        private static final Summary ZERO = new Summary(0, 0, new BigDecimal(0));

        private Summary add(Transaction trx) {
            return new Summary(
                    totalLines + 1,
                    totalFrauds + (trx.isFraud() ? 1 : 0),
                    totalAmount.add(trx.amount())
            );
        }

        private Summary merge(Summary sum) {
            return new Summary(
                    totalLines + sum.totalLines,
                    totalFrauds + sum.totalFrauds,
                    totalAmount.add(sum.totalAmount)
            );
        }
    }

    public void
    report(Locale locale) {
        Path path = Path.of(FILE_PATH);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        NumberFormat integerFormat = NumberFormat.getIntegerInstance(locale);
        ResourceBundle msg = ResourceBundle.getBundle("mensagens", locale);

        long start = System.nanoTime();
        try (Stream<String> lines = Files.lines(path)) {

            Summary sum = lines.skip(1)
                    .map(TransactionConverter::parseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .reduce(
                            Summary.ZERO,
                            Summary::add,
                            Summary::merge
                    );


            IO.println(msg.getString("total.transacoes") + ": " + integerFormat.format(sum.totalLines()));
            IO.println(msg.getString("total.fraudes") + ": " + integerFormat.format(sum.totalFrauds()));
            IO.println(msg.getString("valor.total.transacionado") + ": " + numberFormat.format(sum.totalAmount()));

        } catch (Exception ex) {
            throw new RuntimeException("Error reading file.");
        } finally {
            long end = System.nanoTime();
            IO.println("Tempo (ms): " + (end - start) / 1_000_000.0);
        }
//        long start = System.nanoTime();
//
//        try (Stream<String> lines = Files.lines(path)) {
//            long count = lines.skip(1).count();
//            IO.println("Total de linhas: " + count);
//        } catch (Exception ex) {
//            throw new RuntimeException("Error reading file.");
//        }
//
//        try (Stream<String> lines = Files.lines(path)) {
//            long count = lines.skip(1)
//                    .map(TransactionConverter::parseTransaction)
//                    .filter(Optional::isPresent)
//                    .map(Optional::get)
//                    .filter(Transaction::isFraud)
//                    .count();
//            IO.println("Total de fraudes: " + count);
//        } catch (Exception ex) {
//            throw new RuntimeException("Error reading file.");
//        }
//
//        try (Stream<String> lines = Files.lines(path)) {
//            BigDecimal count = lines.skip(1)
//                    .map(TransactionConverter::parseTransaction)
//                    .filter(Optional::isPresent)
//                    .map(Optional::get)
//                    .map(Transaction::amount)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//            IO.println("Valor total transacionado: " + numberFormat.format(count));
//        } catch (Exception ex) {
//            throw new RuntimeException("Error reading file.");
//        }
//
//        long end = System.nanoTime();
//        IO.println("Tempo <List> (ms): " + (end - start) / 1_000_000.0);
    }
}

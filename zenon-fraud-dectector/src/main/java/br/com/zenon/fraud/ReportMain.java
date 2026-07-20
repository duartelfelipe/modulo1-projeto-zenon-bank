package br.com.zenon.fraud;

import java.io.IOException;
import java.util.Locale;

public class ReportMain {


    static void main() throws IOException {
        Locale locale = null;

        IO.println("=======PT-BR=======");
        locale = Locale.of("pt", "BR");
        new TransactionReport().report(locale);

        locale = Locale.US;
        IO.println("=======US=======");
        new TransactionReport().report(locale);
    }
}

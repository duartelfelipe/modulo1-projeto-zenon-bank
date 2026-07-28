package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository {

//    private final List<Transaction> transactions;

//    public TransactionSQLRepository(List<Transaction> transactions) {
//        Objects.requireNonNull(transactions);
//        this.transactions = transactions;
//    }

    @Override
    public Optional<Transaction> findByOriginCustomer(String name) {
        String sql = """
                SELECT step, type, amount, isFraud, isFlaggedFraud,
                nameOrigin, originOldAmount, originNewAmount,
                nameDestination, destinationOldAmount, destinationNewAmount 
                FROM transaction
                WHERE nameOrigin = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Integer step = rs.getInt("step");
                    TransactionType type = TransactionType.valueOf(rs.getString("type"));
                    BigDecimal amount = rs.getBigDecimal("amount");
                    Boolean isFraud = rs.getBoolean("isFraud");
                    Boolean isFlaggedFraud = rs.getBoolean("isFlaggedFraud");

                    TransactionCustomer origin = new TransactionCustomer(
                            rs.getString("nameOrigin"),
                            rs.getBigDecimal("originOldAmount"),
                            rs.getBigDecimal("originNewAmount")
                    );

                    TransactionCustomer destination = new TransactionCustomer(
                            rs.getString("nameDestination"),
                            rs.getBigDecimal("destinationOldAmount"),
                            rs.getBigDecimal("destinationNewAmount")
                    );

                    return Optional.of(
                            new Transaction(
                                    step, type, amount, isFraud, isFlaggedFraud, origin, destination
                            )
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public void save(Transaction transaction) {
        String sql = """
                INSERT INTO transaction
                    (step, type, amount, isFraud, isFlaggedFraud,
                     nameOrigin, originOldAmount, originNewAmount,
                     nameDestination, destinationOldAmount, destinationNewAmount)
                VALUES (?,?,?,?,?,?,?,?,?,?,?)
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, transaction.step());
            ps.setString(2, transaction.type().name());
            ps.setBigDecimal(3, transaction.amount());
            ps.setBoolean(4, transaction.isFraud());
            ps.setBoolean(5, transaction.isFlaggedFraud());
            ps.setString(6, transaction.origin().customer());
            ps.setBigDecimal(7, transaction.origin().oldAmount());
            ps.setBigDecimal(8, transaction.origin().newAmount());
            ps.setString(9, transaction.destination().customer());
            ps.setBigDecimal(10, transaction.destination().oldAmount());
            ps.setBigDecimal(11, transaction.destination().newAmount());

            ps.execute();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

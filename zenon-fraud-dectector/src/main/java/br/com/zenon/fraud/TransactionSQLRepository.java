package br.com.zenon.fraud;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    @Override
    public void saveBatch(List<Transaction> transactions) {

        int batchSize = 1000 ;
        int count = 0;

        String sql = """
                INSERT INTO transaction
                    (step, type, amount, isFraud, isFlaggedFraud,
                     nameOrigin, originOldAmount, originNewAmount,
                     nameDestination, destinationOldAmount, destinationNewAmount)
                VALUES (?,?,?,?,?,?,?,?,?,?,?)
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (Transaction t : transactions) {
                ps.setInt(1, t.step());
                ps.setString(2, t.type().name());
                ps.setBigDecimal(3, t.amount());
                ps.setBoolean(4, t.isFraud());
                ps.setBoolean(5, t.isFlaggedFraud());
                ps.setString(6, t.origin().customer());
                ps.setBigDecimal(7, t.origin().oldAmount());
                ps.setBigDecimal(8, t.origin().newAmount());
                ps.setString(9, t.destination().customer());
                ps.setBigDecimal(10, t.destination().oldAmount());
                ps.setBigDecimal(11, t.destination().newAmount());
                ps.addBatch();

                if (++count % batchSize == 0) {
                    int[] results = ps.executeBatch();
                    ps.clearBatch(); // Clear for next chunk
                }
            }

            ps.executeBatch();
            conn.commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveThreadBatch(List<Transaction> transactions) {


        String sql = """
                INSERT INTO transaction
                    (step, type, amount, isFraud, isFlaggedFraud,
                     nameOrigin, originOldAmount, originNewAmount,
                     nameDestination, destinationOldAmount, destinationNewAmount)
                VALUES (?,?,?,?,?,?,?,?,?,?,?)
                """;

        Executor executor = Executors.newFixedThreadPool(100);

        try (Connection conn = ConnectionFactory.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {


//            executor.execute(() -> {
                int batchSize = 1000;
                int count = 0;

                for (Transaction t : transactions) {
                    ps.setInt(1, t.step());
                    ps.setString(2, t.type().name());
                    ps.setBigDecimal(3, t.amount());
                    ps.setBoolean(4, t.isFraud());
                    ps.setBoolean(5, t.isFlaggedFraud());
                    ps.setString(6, t.origin().customer());
                    ps.setBigDecimal(7, t.origin().oldAmount());
                    ps.setBigDecimal(8, t.origin().newAmount());
                    ps.setString(9, t.destination().customer());
                    ps.setBigDecimal(10, t.destination().oldAmount());
                    ps.setBigDecimal(11, t.destination().newAmount());

                    ps.addBatch();

                    if (++count % batchSize == 0) {
                        ps.executeBatch();
                        ps.clearBatch(); // Clear for next chunk
                        conn.commit();
                    }
                }
//            });

                ps.executeBatch();
                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

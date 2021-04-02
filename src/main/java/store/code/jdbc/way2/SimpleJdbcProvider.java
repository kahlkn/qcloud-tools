//package store.code.jdbc.way2;
//
//import artoria.exception.ExceptionUtils;
//import artoria.jdbc.DatabaseClient;
//import artoria.logging.Logger;
//import artoria.logging.LoggerFactory;
//import artoria.util.Assert;
//import artoria.util.CloseUtils;
//
//import javax.sql.DataSource;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static artoria.common.Constants.ONE;
//import static artoria.common.Constants.ZERO;
//
//public class SimpleJdbcProvider implements JdbcProvider {
//    private static Logger log = LoggerFactory.getLogger(DatabaseClient.class);
//    private final ThreadLocal<Connection> threadConnection = new ThreadLocal<Connection>();
//    private DataSource dataSource;
//
//    public SimpleJdbcProvider(DataSource dataSource) {
//        Assert.notNull(dataSource, "Parameter \"dataSource\" must not null. ");
//        this.dataSource = dataSource;
//    }
//
//    public DataSource getDataSource() {
//
//        return dataSource;
//    }
//
//    private Connection getConnection() throws SQLException {
//        Connection connection = threadConnection.get();
//        if (connection == null) {
//            connection = dataSource.getConnection();
//        }
//        return connection;
//    }
//
//    private void closeConnection(Connection connection) {
//        if (threadConnection.get() == null) {
//            // Indicates that no transaction was executed.
//            CloseUtils.closeQuietly(connection);
//        }
//        // Ignore close if there is a transaction going on.
//    }
//
//    private void rollbackTransaction(Connection connection) {
//        if (connection == null) { return; }
//        try {
//            connection.rollback();
//        }
//        catch (Exception e) {
//            log.error("Execution \"rollbackTransaction\" error. ", e);
//        }
//    }
//
//    private void closeTransaction(Connection connection, Boolean autoCommit) {
//        if (connection == null) { return; }
//        try {
//            if (autoCommit != null) {
//                connection.setAutoCommit(autoCommit);
//            }
//            connection.close();
//        }
//        catch (Exception e) {
//            log.error("Execution \"closeTransaction\" error. ", e);
//        }
//        finally {
//            threadConnection.remove();
//        }
//    }
//
//    @Override
//    public void fillStatement(PreparedStatement prepStmt, Object... params) throws SQLException {
//        for (int i=0; i<params.length; i++) {
//            Object value = params[i];
//            if (value instanceof java.util.Date) {
//                if (value instanceof Date) {
//                    prepStmt.setDate(i + 1, (Date)value);
//                }
//                else if (value instanceof Timestamp) {
//                    prepStmt.setTimestamp(i + 1, (Timestamp)value);
//                }
//                else {
//                    // Oracle、SqlServer 中的 TIMESTAMP、DATE 支持 new Date() 给值
//                    java.util.Date d = (java.util.Date)value;
//                    prepStmt.setTimestamp(i + 1, new Timestamp(d.getTime()));
//                }
//            }
//            else {
//                prepStmt.setObject(i + 1, value);
//            }
//        }
//    }
//
//    @Override
//    public <T> T execute(JdbcCallback<T> jdbcCallback) throws SQLException {
//        Connection connection = null;
//        try {
//            connection = getConnection();
//            return jdbcCallback.call(connection);
//        }
//        finally {
//            closeConnection(connection);
//        }
//    }
//
//    @Override
//    public boolean transaction(JdbcAtom jdbcAtom, Integer transactionLevel) throws SQLException {
//        if (transactionLevel == null) {
//            transactionLevel = Connection.TRANSACTION_REPEATABLE_READ;
//        }
//        Connection connection = threadConnection.get();
//        // Nested transaction support.
//        if (connection != null) {
//            if (connection.getTransactionIsolation() < transactionLevel) {
//                connection.setTransactionIsolation(transactionLevel);
//            }
//            boolean result = jdbcAtom.run();
//            if (result) {
//                return true;
//            }
//            // Important: can not return false
//            throw new JdbcException("Notice the outer transaction that the nested transaction return false");
//        }
//        // Normal transaction support.
//        Boolean autoCommit = null;
//        try {
//            connection = getConnection();
//            autoCommit = connection.getAutoCommit();
//            threadConnection.set(connection);
//            connection.setTransactionIsolation(transactionLevel);
//            connection.setAutoCommit(false);
//            boolean result = jdbcAtom.run();
//            if (result) {
//                connection.commit();
//            }
//            else {
//                connection.rollback();
//            }
//            return result;
//        }
//        catch (Exception e) {
//            rollbackTransaction(connection);
//            throw ExceptionUtils.wrap(e);
//        }
//        finally {
//            closeTransaction(connection, autoCommit);
//        }
//    }
//
//    @Override
//    public int executeUpdate(String sql, Object... params) throws SQLException {
//        PreparedStatement prepStat = null;
//        Connection connection = null;
//        try {
//            connection = getConnection();
//            prepStat = connection.prepareStatement(sql);
//            for (int i = ZERO; i < params.length; i++) {
//                prepStat.setObject(i + ONE, params[i]);
//            }
//            return prepStat.executeUpdate();
//        }
//        finally {
//            CloseUtils.closeQuietly(prepStat);
//            closeConnection(connection);
//        }
//    }
//
//    @Override
//    public List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {
//        PreparedStatement prepStmt = null;
//        ResultSet resSet = null;
//        Connection conn = null;
//        try {
//            conn = getConnection();
//            prepStmt = conn.prepareStatement(sql);
//            for (int i = ZERO; i < params.length; i++) {
//                prepStmt.setObject(i + ONE, params[i]);
//            }
//            resSet = prepStmt.executeQuery();
//            // Handle column names.
//            ResultSetMetaData metaData = resSet.getMetaData();
//            int columnCount = metaData.getColumnCount();
//            String[] columnNames = new String[columnCount];
//            for (int i = ZERO; i < columnCount; i++) {
//                String key = metaData.getColumnName(i + ONE);
////                columnNames[i] = StringUtils.underlineToCamel(key);
//                columnNames[i] = key;
//            }
//            // Handle result to map.
//            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
//            while (resSet.next()) {
//                Map<String, Object> data = new HashMap<String, Object>(columnCount);
//                for (int i = ZERO; i < columnCount; i++) {
//                    data.put(columnNames[i], resSet.getObject(i + ONE));
//                }
//                result.add(data);
//            }
//            return result;
//        }
//        finally {
//            CloseUtils.closeQuietly(resSet);
//            CloseUtils.closeQuietly(prepStmt);
//            closeConnection(conn);
//        }
//    }
//
//}

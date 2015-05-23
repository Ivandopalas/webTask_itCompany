package com.epam.it_company.dao.pool;

import com.epam.it_company.dao.DaoPropertyFileParameters;
import com.epam.it_company.dao.util.DaoResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.*;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

/**
 * ConnectionPool provides getting already open connections from pool.
 */
public class ConnectionPool {
    private final static Logger logger = LogManager.getLogger(ConnectionPool.class);

    private BlockingQueue<Connection> connectionQueue;
    private BlockingQueue<Connection> givenAwayConnectionQueue;
    private String driver;
    private String url;
    private String user;
    private String password;
    private int poolSize;
    private final static ConnectionPool instance = new ConnectionPool();

    private ConnectionPool(){
        DaoResourceManager resourceManager = DaoResourceManager.getInstance();
        this.driver = resourceManager.getValue(DaoPropertyFileParameters.DB_DRIVER);
        this.url = resourceManager.getValue(DaoPropertyFileParameters.DB_URL);
        this.user = resourceManager.getValue(DaoPropertyFileParameters.DB_USER);
        this.password = resourceManager.getValue(DaoPropertyFileParameters.DB_PASSWORD);
        try {
            String poolSizeFromProperty;
            poolSizeFromProperty = resourceManager.getValue(DaoPropertyFileParameters.DB_POLL_SIZE);
            this.poolSize = Integer.parseInt(poolSizeFromProperty);
            initPool();
        }catch (NumberFormatException ex){
            logger.warn("Can't get pool size from property file, set default value = 5", ex);
            this.poolSize = 5;
        }catch (ConnectionPoolException ex) {
            logger.error("Didn't initialized pool", ex);
        }
    }

    public static ConnectionPool getInstance(){
        return instance;
    }

    /**
     * Get already open connection from pool.
     * @return Opened connection.
     * @throws ConnectionPoolException
     */
    public Connection takeConnection() throws ConnectionPoolException{
        Connection connection;
        try{
            connection = connectionQueue.take();
            givenAwayConnectionQueue.add(connection);
        }catch (InterruptedException ex){
            throw  new ConnectionPoolException("Can't take connection from queue", ex);
        }
        return connection;
    }

    /**
     * Actually this method just return connection to connection pool if it has taken,
     * but really close statement and other connections.
     * @param con taken connection from connection pool
     * @param st created statement.
     */
    public void closeConnection(Connection con, Statement st) {
        try {
            con.close();
        } catch (SQLException e) {
            logger.warn("Connection didn't get back to the pool.");
        }
        try {
            st.close();
        } catch (SQLException e) {
            logger.warn("Statement didn't close.");
        }
    }

    /**
     * Clear all connection queue and close all connections.
     */
    public void dispose() {
        clearConnectionQueue();
    }

     /**
     * Actually this method just return connection to connection pool if it has taken,
     * but really close statement and other connections.
     * @param con taken connection from connection pool
     * @param st created statement.
     */
    public void closeConnection(Connection con, Statement st, ResultSet rs) {
        try {
            con.close();
        } catch (SQLException e) {
            logger.warn("Connection didn't get back to the pool.");
        }
        try {
            rs.close();
        } catch (SQLException e) {
            logger.warn("Result set didn't close.");
        }
        try {
            st.close();
        } catch (SQLException e) {
            logger.warn("Statement didn't close.");
        }
    }

    private void clearConnectionQueue() {
        try {
            closeConnectionQueue(givenAwayConnectionQueue);
            closeConnectionQueue(connectionQueue);
        } catch (SQLException e) {
            // logger.error("Error closing the connection.", e);
        }
    }
    private void closeConnectionQueue(BlockingQueue<Connection> queue) throws SQLException{
        Connection connection;
        while ((connection = queue.poll()) != null){
            connection.commit();
            ((PooledConnection)connection).reallyclose();
        }
    }
    private void initPool() throws ConnectionPoolException{
        Locale.setDefault(Locale.ENGLISH);
        try{
            Class.forName(driver);
            connectionQueue = new ArrayBlockingQueue<>(this.poolSize);
            givenAwayConnectionQueue = new ArrayBlockingQueue<>(this.poolSize);
            for(int i = 0 ; i < this.poolSize ; i++){
                Connection connection = DriverManager.getConnection(this.url,this.user,this.password);
                PooledConnection pooledConnection = new PooledConnection(connection);
                connectionQueue.add(pooledConnection);
            }
        }catch (ClassNotFoundException ex){
            logger.error("Can't upload driver");
            throw new ConnectionPoolException("Driver loading error",ex);
        }catch (SQLException ex){
            logger.error("Sql exception while getting connection");
            throw  new ConnectionPoolException("Caught sql exception while init pool", ex);
        }
    }
    private class PooledConnection implements Connection{

        private Connection connection;
        public PooledConnection(Connection connection) throws SQLException{
            this.connection = connection;
            this.connection.setAutoCommit(true);
        }

        public void reallyclose() throws SQLException{
            connection.close();
        }
        @Override
        public void close() throws SQLException {
            if(connection.isClosed()){
                throw new SQLException("Attempting to close closed connection");
            }
            if(connection.isReadOnly()){
                connection.setReadOnly(false);
            }
            if(!givenAwayConnectionQueue.remove(this)){
                throw new SQLException("Can't delete connection from given away connections queue");
            }
            if (!connectionQueue.offer(this)) {
                throw new SQLException("Error allocating connection in the pool.");
            }
        }
        @Override
        public void clearWarnings() throws SQLException {
            connection.clearWarnings();
        }
        @Override
        public void commit() throws SQLException {
            connection.commit();
        }

        @Override
        public Array createArrayOf(String typeName, Object[] elements)
                throws SQLException {
            return connection.createArrayOf(typeName, elements);
        }

        @Override
        public Blob createBlob() throws SQLException {
            return connection.createBlob();
        }

        @Override
        public Clob createClob() throws SQLException {
            return connection.createClob();
        }

        @Override
        public NClob createNClob() throws SQLException {
            return connection.createNClob();
        }

        @Override
        public SQLXML createSQLXML() throws SQLException {
            return connection.createSQLXML();
        }

        @Override
        public Statement createStatement() throws SQLException {
            return connection.createStatement();
        }

        @Override
        public Statement createStatement(int resultSetType,
        int resultSetConcurrency) throws SQLException {
            return connection.createStatement(resultSetType,
                    resultSetConcurrency);
        }

        @Override
        public Statement createStatement(int resultSetType,
                                         int resultSetConcurrency, int resultSetHoldability)
                throws SQLException {
            return connection.createStatement(resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public Struct createStruct(String typeName, Object[] attributes)
                throws SQLException {
            return connection.createStruct(typeName, attributes);
        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return connection.getAutoCommit();
        }

        @Override
        public String getCatalog() throws SQLException {
            return connection.getCatalog();
        }

        @Override
        public Properties getClientInfo() throws SQLException {
            return connection.getClientInfo();
        }

        @Override
        public String getClientInfo(String name) throws SQLException {
            return connection.getClientInfo(name);
        }

        @Override
        public int getHoldability() throws SQLException {
            return connection.getHoldability();
        }

        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return connection.getMetaData();
        }

        @Override
        public int getTransactionIsolation() throws SQLException {
            return connection.getTransactionIsolation();
        }

        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return connection.getTypeMap();
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            return connection.getWarnings();
        }

        @Override
        public boolean isClosed() throws SQLException {
            return connection.isClosed();
        }

        @Override
        public boolean isReadOnly() throws SQLException {
            return connection.isReadOnly();
        }

        @Override
        public boolean isValid(int timeout) throws SQLException {
            return connection.isValid(timeout);
        }

        @Override
        public String nativeSQL(String sql) throws SQLException {
            return connection.nativeSQL(sql);
        }

        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return connection.prepareCall(sql);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType,
                                             int resultSetConcurrency) throws SQLException {
            return connection.prepareCall(sql, resultSetType,
                    resultSetConcurrency);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType,
                                             int resultSetConcurrency, int resultSetHoldability)
                throws SQLException {
            return connection.prepareCall(sql, resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql)
                throws SQLException {
            return connection.prepareStatement(sql);
        }

        @Override
        public PreparedStatement prepareStatement(String sql,
                                                  int autoGeneratedKeys) throws SQLException {
            return connection.prepareStatement(sql, autoGeneratedKeys);
        }

        @Override
        public PreparedStatement prepareStatement(String sql,
                                                  int[] columnIndexes) throws SQLException {
            return connection.prepareStatement(sql, columnIndexes);
        }

        @Override
        public PreparedStatement prepareStatement(String sql,
                                                  String[] columnNames) throws SQLException {
            return connection.prepareStatement(sql, columnNames);
        }

        @Override
        public PreparedStatement prepareStatement(String sql,
                                                  int resultSetType, int resultSetConcurrency)
                throws SQLException {
            return connection.prepareStatement(sql, resultSetType,
                    resultSetConcurrency);
        }

        @Override
        public PreparedStatement prepareStatement(String sql,
                                                  int resultSetType, int resultSetConcurrency,
                                                  int resultSetHoldability) throws SQLException {
            return connection.prepareStatement(sql, resultSetType,
                    resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public void rollback() throws SQLException {
            connection.rollback();
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            connection.setAutoCommit(autoCommit);
        }

        @Override
        public void setCatalog(String catalog) throws SQLException {
            connection.setCatalog(catalog);
        }

        @Override
        public void setClientInfo(String name, String value)
                throws SQLClientInfoException {
            connection.setClientInfo(name, value);
        }

        @Override
        public void setHoldability(int holdability) throws SQLException {
            connection.setHoldability(holdability);
        }

        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            connection.setReadOnly(readOnly);
        }

        @Override
        public Savepoint setSavepoint() throws SQLException {
            return connection.setSavepoint();
        }

        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return connection.setSavepoint(name);
        }

        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            connection.setTransactionIsolation(level);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return connection.isWrapperFor(iface);
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return connection.unwrap(iface);
        }

        @Override
        public void abort(Executor arg0) throws SQLException {
            connection.abort(arg0);
        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            return connection.getNetworkTimeout();
        }

        @Override
        public String getSchema() throws SQLException {
            return connection.getSchema();
        }

        @Override
        public void releaseSavepoint(Savepoint arg0) throws SQLException {
            connection.releaseSavepoint(arg0);
        }

        @Override
        public void rollback(Savepoint arg0) throws SQLException {
            connection.rollback(arg0);
        }

        @Override
        public void setClientInfo(Properties arg0)
                throws SQLClientInfoException {
            connection.setClientInfo(arg0);
        }

        @Override
        public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException {
            connection.setNetworkTimeout(arg0, arg1);
        }

        @Override
        public void setSchema(String arg0) throws SQLException {
            connection.setSchema(arg0);
        }

        @Override
        public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
            connection.setTypeMap(arg0);
        }
    }
}

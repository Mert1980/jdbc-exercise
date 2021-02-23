package be.infernalwhale.service.implementations;

import be.infernalwhale.service.ConnectionManager;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ConnectionManagerImpl implements ConnectionManager {
    private Connection connection;

    @Override
    public Connection createConnection(String url, String user, String pwd) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, pwd);
        return connection;
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void closeConnection() throws SQLException {
        this.connection.close();
    }
}

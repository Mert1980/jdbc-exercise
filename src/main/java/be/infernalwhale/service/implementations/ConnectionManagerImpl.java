package be.infernalwhale.service.implementations;

import be.infernalwhale.service.ConnectionManager;

import java.sql.*;

public class ConnectionManagerImpl implements ConnectionManager {
    private static Connection connection;

    @Override
    public Connection createConnection(String url, String user, String pwd) throws SQLException {
        connection = DriverManager.getConnection(url, user, pwd);
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

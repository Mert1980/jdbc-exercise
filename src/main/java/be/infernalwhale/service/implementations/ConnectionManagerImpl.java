package be.infernalwhale.service.implementations;

import be.infernalwhale.service.ConnectionManager;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ConnectionManagerImpl implements ConnectionManager {

    @Override
    public Connection createConnection(String url, String user, String pwd) throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public void closeConnection() throws SQLException {

    }
}

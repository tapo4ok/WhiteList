package mdk.whitelist.storge;

import mdk.whitelist.IL;

import java.sql.*;

public class Sqlite implements IData {

    private Connection connection;

    public Sqlite(IL il) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + "whitelist.db");
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS players (name TEXT PRIMARY KEY)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean removeUser(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM players WHERE name = ?");
            statement.setString(1, name);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean is(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM players WHERE name = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addUser(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO players (name) VALUES (?)");
            statement.setString(1, name);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
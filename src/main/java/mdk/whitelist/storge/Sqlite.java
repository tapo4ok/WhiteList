package mdk.whitelist.storge;

import mdk.mutils.lang.ILang;
import mdk.whitelist.IL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Sqlite implements IData<String> {

    public Connection connection;
    private IL il;
    private ILang lang;
    public Sqlite() {}

    public Sqlite(IL il, ILang lang) {
        this.il = il;
        this.lang = lang;
        try {
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s.db", il.getConfig0().getConfig().file));
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (user_name VARCHAR(255) PRIMARY KEY)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean removeUser(String name) {
        if (!is(name)) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE user_name = ?");
            statement.setString(1, name);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeUser(String name, ActionInfo info) {
        if (!is(name)) {
            info.addStackTrans("data.error.remove", ActionInfo.ERROR, lang, name);
            info.cancel = true;
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE user_name = ?");
            statement.setString(1, name);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            info.addStackTrans("data.db.error", ActionInfo.ERROR, lang);
            info.cancel = true;
            return false;
        }
    }

    @Override
    public boolean is(String name, ActionInfo info) {
        try {
            if (connection.isClosed()) {
                connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s.db", il.getConfig0().getConfig().file));
                info.addStackTrans("data.reconnect", ActionInfo.WARN, lang);
            }
            PreparedStatement statement = connection.prepareStatement("SELECT user_name FROM users WHERE user_name = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            info.addStackTrans("data.db.error", ActionInfo.ERROR, lang);
            info.cancel = true;
            return false;
        }
    }

    @Override
    public boolean is(String name) {
        try {
            if (connection.isClosed()) {
                connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s.db", il.getConfig0().getConfig().file));
            }
            PreparedStatement statement = connection.prepareStatement("SELECT user_name FROM users WHERE user_name = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addUser(String name, ActionInfo info) {
        if (is(name)) {
            info.addStackTrans("data.error.add", ActionInfo.ERROR, lang, name);
            info.cancel = true;
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (user_name) VALUES (?)");
            statement.setString(1, name);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            info.addStackTrans("data.db.error", ActionInfo.ERROR, lang);
            info.cancel = true;
            return false;
        }
    }

    @Override
    public boolean addUser(String name) {
        if (is(name)) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (user_name) VALUES (?)");
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
                if (!connection.isClosed()) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> toList() {

        List<String> players = new ArrayList<>();
        try {
            if (connection.isClosed()) {
                connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s.db", il.getConfig0().getConfig().file));
            }
            String sql = "SELECT user_name FROM users";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                players.add(rs.getString("name"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }
}
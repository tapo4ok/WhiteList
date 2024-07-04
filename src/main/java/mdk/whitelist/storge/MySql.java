package mdk.whitelist.storge;

import com.google.gson.JsonObject;
import mdk.mutils.Static;
import mdk.mutils.lang.ILang;
import mdk.whitelist.IL;

import java.sql.*;

@Deprecated
public class MySql extends Sqlite {
    private final IL il;
    private final ILang lang;
    public MySql(IL il, ILang lang) {
        this.il = il;
        this.lang = lang;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(il.getConfig0().getConfig().mysql_bd);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean is(String name, ActionInfo info) {
        try {
            if (connection.isClosed()) {
                connection = DriverManager.getConnection(il.getConfig0().getConfig().mysql_bd);
                info.addStackTrans("data.reconnect", ActionInfo.WARN, lang);
            }
            PreparedStatement statement = connection.prepareStatement("SELECT json FROM users WHERE user_name = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Static.GSON.fromJson(resultSet.getString("json"), JsonObject.class).get("accept").getAsInt() == 1;
            }
            return false;
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
                connection = DriverManager.getConnection(il.getConfig0().getConfig().mysql_bd);
            }
            PreparedStatement statement = connection.prepareStatement("SELECT json FROM users WHERE user_name = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Static.GSON.fromJson(resultSet.getString("json"), JsonObject.class).get("accept").getAsInt() == 1;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addUser(String name) {
        throw new RuntimeException();
    }
}

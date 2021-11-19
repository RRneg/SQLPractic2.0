package com.sasha.sqlpractic.utils;

import java.sql.*;

public class JdbcUtils {
    private static Connection connection;

    private static Connection getConnect() {
        String url = "";
        String user = "user";
        String password = "password";
        if (connection == null) {
            try {
                Driver driver = new com.mysql.jdbc.Driver();
                DriverManager.registerDriver(driver);
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                System.out.println("Unable to load driver class.");
                e.printStackTrace();
            }

        }
        return connection;

    }

    public static PreparedStatement getPrStatement(String sql) throws SQLException {
        return getConnect().prepareStatement(sql);
    }

    public static PreparedStatement getPrStatementBackId(String sql) throws SQLException{
        return getConnect().prepareStatement(sql, com.mysql.jdbc.Statement.RETURN_GENERATED_KEYS);
    }
}

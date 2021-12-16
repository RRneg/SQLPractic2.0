package com.sasha.sqlpractic.utils;

import java.sql.*;

public class JdbcUtils {
    private static Connection connection;

    private static Connection getConnect() {
        String url = "jdbc:mysql://localhost:3306/practice?serverTimezone=Europe/Kiev&useSSL=false";
        String user = "root";
        String password = "Rfvbgt79";
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

    public static void closeConnection(){
        try {
            getConnect().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PreparedStatement getPrStatement(String sql) throws SQLException {
        return getConnect().prepareStatement(sql);
    }

    public static PreparedStatement getPrStatementScroll(String sql) throws SQLException{
        return getConnect().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    }

    public static PreparedStatement getPrStatementBackId(String sql) throws SQLException{
        return getConnect().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public static PreparedStatement getPrStatmentBackIdCreated(String sql) throws SQLException{
        return getConnect().prepareStatement(sql,  new String[] { "ID", "CREATED" });
    }
}

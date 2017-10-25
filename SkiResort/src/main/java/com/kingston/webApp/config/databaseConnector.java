package com.kingston.webApp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class databaseConnector {

    private static final String urlTemplete = "jdbc:postgresql://%s:%s/%s";
    private static final String databaseHost = "skiresortdb.cd5eiatsdlfp.us-west-2.rds.amazonaws.com";
    private static final String port = "5432";
    private static final String databaseName = "SkiResortDB";
    private static final String username = "kingstonwen";
    private static final String password = "Wszat4244";

    public Connection connect() {
        Connection connection = null;
        String url = String.format(urlTemplete, databaseHost, port, databaseName);
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            System.out.println(connection.getMetaData());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        databaseConnector connector = new databaseConnector();
        Connection connection = connector.connect();
//        String test = "INSERT INTO lift_ride(resortid, daynum, time, skierid, liftid) VALUES(?, ?, ?, ?, ?)";
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(test);
//            preparedStatement.setString(1, "r12");
//            preparedStatement.setInt(2, 1);
//            preparedStatement.setString(3, "2");
//            preparedStatement.setString(4, "s123");
//            preparedStatement.setString(5, "l123");
//            preparedStatement.execute();
//            System.out.println("Done inserting");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

}

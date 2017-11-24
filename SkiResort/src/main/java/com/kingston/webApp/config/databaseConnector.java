package com.kingston.webApp.config;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private final String urlTemplete = "jdbc:postgresql://%s:%s/%s";
    private final String databaseHost = "skiresortdb.cd5eiatsdlfp.us-west-2.rds.amazonaws.com";
    private final String port = "5432";
    private final String databaseName = "SkiResortDB";
    private final String username = "kingstonwen";
    private final String password = "Wszat4244";

    private static DatabaseConnector instance;
    private BoneCP connectionPool;

    public DatabaseConnector() {
    }

    public static DatabaseConnector getInstance(){
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;

    }

    private void initializePool() {
        try {
            Class.forName("org.postgresql.Driver");
            try {
                String url = String.format(urlTemplete, databaseHost, port, databaseName);
                BoneCPConfig config = new BoneCPConfig();
                config.setJdbcUrl(url); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
                config.setUsername(username);
                config.setPassword(password);
                config.setMinConnectionsPerPartition(5);
                config.setMaxConnectionsPerPartition(10);
                config.setPartitionCount(1);
                connectionPool = new BoneCP(config); // setup the connection pool

//                String url = String.format(urlTemplete, databaseHost, port, databaseName);
//                database = DriverManager.getConnection(url, username, password);
//                if (database != null) {
//                    System.out.println("Connected to the PostgreSQL server successfully.");
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        if (connectionPool == null) {
            initializePool();
        }
        return connectionPool.getConnection();
    }

//    public static void main(String[] args){
//        DatabaseConnector connector = new DatabaseConnector();
//        Connection connection = connector.getConnection();
//        String test = "INSERT INTO lift_ride(resortid, daynum, timestamp, skierid, liftid) VALUES(?, ?, ?, ?, ?)";
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

//        try (Connection connection = connector.getConnection()){
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
}

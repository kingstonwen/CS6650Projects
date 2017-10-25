package com.kingston.webApp.DAO;

import com.kingston.webApp.config.databaseConnector;
import com.kingston.webApp.dataEntity.LiftRide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LiftRideDAO {

    private static final String INSERT_QUERY =
            "INSERT INTO lift_ride(resortid, daynum, time, skierid, liftid) VALUES(?, ?, ?, ?, ?)";

    private databaseConnector databaseConnector;

    public LiftRideDAO() {
        databaseConnector = new databaseConnector();
    }

    public void save(LiftRide liftRide) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = databaseConnector.connect();
            preparedStatement = connection.prepareStatement(INSERT_QUERY);
            entityToTuple(liftRide, preparedStatement);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void entityToTuple(LiftRide liftRide, PreparedStatement preparedStatement) throws SQLException{
        preparedStatement.setString(1, liftRide.getResortID());
        preparedStatement.setInt(2, liftRide.getDayNum());
        preparedStatement.setString(3, liftRide.getTimeStamp());
        preparedStatement.setString(4, liftRide.getSkierID());
        preparedStatement.setString(5, liftRide.getLiftID());
    }
}

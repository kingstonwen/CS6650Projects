package com.kingston.webApp.DAO;

import com.kingston.webApp.config.DatabaseConnector;
import com.kingston.webApp.dataEntity.LiftRide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class LiftRideDAO {

    private static final String INSERT_QUERY =
            "INSERT INTO lift_ride(resortid, daynum, timestamp, skierid, liftid) VALUES(?, ?, ?, ?, ?)";

    private static final String DELETE_ALL = "DELETE FROM lift_ride WHERE daynum = ?";

    private DatabaseConnector databaseConnector;

    public LiftRideDAO() {
        databaseConnector = DatabaseConnector.getInstance();
    }

    public void save(LiftRide liftRide) {
        try (Connection database = databaseConnector.getConnection()){
            if (database == null) {
                throw new SQLException();
            }
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = database.prepareStatement(INSERT_QUERY);
                preparedStatement.setString(1, liftRide.getResortID());
                preparedStatement.setInt(2, liftRide.getDayNum());
                preparedStatement.setString(3, liftRide.getTimeStamp());
                preparedStatement.setString(4, liftRide.getSkierID());
                preparedStatement.setString(5, liftRide.getLiftID());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void batchInsert(List<LiftRide> liftRideList) {
        try (Connection database = databaseConnector.getConnection()){
            if (database == null) {
                throw new SQLException();
            }
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = database.prepareStatement(INSERT_QUERY);
                for(LiftRide liftRide : liftRideList) {
                    preparedStatement.setString(1, liftRide.getResortID());
                    preparedStatement.setInt(2, liftRide.getDayNum());
                    preparedStatement.setString(3, liftRide.getTimeStamp());
                    preparedStatement.setString(4, liftRide.getSkierID());
                    preparedStatement.setString(5, liftRide.getLiftID());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllByDay(Integer dayNum) {
        try(Connection database = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = database.prepareStatement(DELETE_ALL);
                preparedStatement.setInt(1, dayNum);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    private void entityToTuple(LiftRide liftRide, PreparedStatement preparedStatement) throws SQLException{
//        preparedStatement.setString(1, liftRide.getResortID());
//        preparedStatement.setInt(2, liftRide.getDayNum());
//        preparedStatement.setString(3, liftRide.getTimeStamp());
//        preparedStatement.setString(4, liftRide.getSkierID());
//        preparedStatement.setString(5, liftRide.getLiftID());
//    }
}

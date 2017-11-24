package com.kingston.webApp.DAO;

import com.kingston.webApp.config.DatabaseConnector;
import com.kingston.webApp.dataEntity.LiftRide;

import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//@Singleton
public class LiftRideDAO {

    private static final String INSERT_QUERY =
            "INSERT INTO lift_ride(resortid, daynum, timestamp, skierid, liftid) VALUES(?, ?, ?, ?, ?)";

    private static final String TEST =
            "INSERT INTO lift_ride(resortid, daynum, timestamp, skierid, liftid) VALUES";

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

    public void multipleRowsInsert(List<LiftRide> liftRideList) {
        try (Connection database = databaseConnector.getConnection()){
            if (database == null) {
                throw new SQLException();
            }
            PreparedStatement preparedStatement = null;
            try {
                int listSize = liftRideList.size();
                StringBuffer sb = new StringBuffer(TEST);
                for(int i = 0; i < listSize; i++) {
                    sb.append("(?,?,?,?,?)");
                    if (i < liftRideList.size()-1) {
                        sb.append(",");
                    } else {
                        sb.append(";");
                    }
                }
                preparedStatement = database.prepareStatement(sb.toString());
                int i = 5;
                for(LiftRide liftRide : liftRideList) {
                        preparedStatement.setString(i-4, liftRide.getResortID());
                        preparedStatement.setInt(i-3, liftRide.getDayNum());
                        preparedStatement.setString(i-2, liftRide.getTimeStamp());
                        preparedStatement.setString(i-1, liftRide.getSkierID());
                        preparedStatement.setString(i, liftRide.getLiftID());
                        i+=5;
                }
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

    public List<LiftRide> batchInsert(List<LiftRide> liftRideList) {
        List<LiftRide> insertFailed = new ArrayList<>();
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
//                for(int i = 0 ; i < result.length; i++) {
//                    if (result[i] == PreparedStatement.EXECUTE_FAILED) {
//                        insertFailed.add(liftRideList.get(i));
//                    }
//                }
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
        return insertFailed;
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

package com.kingston.webApp.DAO;

import com.kingston.webApp.config.DatabaseConnector;
import com.kingston.webApp.dataEntity.LiftRide;
import com.kingston.webApp.dataEntity.SkierDayInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SkierDayInfoDao {
    private static final String GET_VERTICAL_AND_LIFT_NUM =
            "SELECT totalvertical,numoflift FROM skier_day_info WHERE skierid = ? AND daynum = ?" ;

    private static final String UPDATE =
            "INSERT INTO skier_day_info(skierid, daynum, totalvertical, numoflift) VALUES (?, ?, ?, ?)\n" +
                    "ON CONFLICT (skierid, daynum) DO UPDATE SET\n" +
                    "totalvertical = skier_day_info.totalvertical + EXCLUDED.totalvertical,\n" +
                    "numoflift = skier_day_info.numoflift + EXCLUDED.numoflift";

    private static final String INSERT =
            "INSERT INTO skier_day_info(skierid, daynum, totalvertical, numoflift) VALUES (?, ?, ?, ?)";

    private DatabaseConnector databaseConnector;

    public SkierDayInfoDao() {
        this.databaseConnector = DatabaseConnector.getInstance();
    }

    public void updateSkierDayInfo(LiftRide liftRide) {
        try (Connection database = databaseConnector.getConnection()){
            if (database == null) {
                throw new SQLException();
            }
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = database.prepareStatement(UPDATE);
                preparedStatement.setString(1, liftRide.getSkierID());
                preparedStatement.setInt(2, liftRide.getDayNum());
                preparedStatement.setInt(3, LiftRide.getVerticalByLiftId(liftRide.getLiftID()));
                preparedStatement.setInt(4, 1);
                preparedStatement.execute();
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

    public void batchUpdateSkierDayInfo(List<SkierDayInfo> skierDayInfoList) {
        try (Connection database = databaseConnector.getConnection()){
            if (database == null) {
                throw new SQLException();
            }
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = database.prepareStatement(UPDATE);
                for(SkierDayInfo skierDayInfo : skierDayInfoList) {
                    preparedStatement.setString(1, skierDayInfo.getSkierID());
                    preparedStatement.setInt(2, skierDayInfo.getDayNum());
                    preparedStatement.setInt(3, skierDayInfo.getTotalVertical());
                    preparedStatement.setInt(4, skierDayInfo.getNumOfLiftRides());
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

    public void updateAllSkierDayInfo(List<LiftRide> liftRideList) {
        try (Connection database = databaseConnector.getConnection()){
            if (database == null) {
                throw new SQLException();
            }
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = database.prepareStatement(UPDATE);
                for(LiftRide liftRide : liftRideList) {
                    preparedStatement.setString(1, liftRide.getSkierID());
                    preparedStatement.setInt(2, liftRide.getDayNum());
                    preparedStatement.setInt(3, LiftRide.getVerticalByLiftId(liftRide.getLiftID()));
                    preparedStatement.setInt(4, 1);
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

    public SkierDayInfo getBySkierIDAndDayNum(String skierID, Integer dayNum) {
        SkierDayInfo skierDayInfo = null;
        try (Connection database = databaseConnector.getConnection()){
            if (database == null) {
                throw new SQLException();
            }
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = database.prepareStatement(GET_VERTICAL_AND_LIFT_NUM);
                preparedStatement.setString(1, skierID);
                preparedStatement.setInt(2, dayNum);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Integer totalVertical = resultSet.getInt("totalvertical");
                        Integer numOfLift = resultSet.getInt("numoflift");
                        skierDayInfo = new SkierDayInfo(skierID, dayNum, totalVertical, numOfLift);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
        return skierDayInfo;
    }
}

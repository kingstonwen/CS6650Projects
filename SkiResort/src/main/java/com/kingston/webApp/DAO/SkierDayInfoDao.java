package com.kingston.webApp.DAO;

import com.kingston.webApp.config.DatabaseConnector;
import com.kingston.webApp.dataEntity.SkierDayInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkierDayInfoDao {
    private static final String VERTICAL_AND_LIFT_NUM =
            "SELECT SUM(vertical), COUNT(liftid) FROM lift_ride WHERE skierid = ? AND dayNum = ?" ;

    private DatabaseConnector databaseConnector;

    public SkierDayInfoDao() {
        this.databaseConnector = DatabaseConnector.getInstance();
    }

    public SkierDayInfo getSkierDayInfo(String skierID, Integer dayNum) {
        SkierDayInfo skierDayInfo = null;
        try (Connection database = databaseConnector.getConnection()){
            if (database == null) {
                throw new SQLException();
            }
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = database.prepareStatement(VERTICAL_AND_LIFT_NUM);
                preparedStatement.setString(1, skierID);
                preparedStatement.setInt(2, dayNum);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Integer totalVertical = resultSet.getInt("sum");
                        Integer numOfLift = resultSet.getInt("count");
                        skierDayInfo = new SkierDayInfo(skierID, dayNum, totalVertical, numOfLift);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skierDayInfo;
    }
}

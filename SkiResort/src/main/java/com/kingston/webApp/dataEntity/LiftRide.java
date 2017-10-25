package com.kingston.webApp.dataEntity;

public class LiftRide {
    private String resortID;
    private Integer dayNum;
    private String timeStamp;
    private String skierID;
    private String liftID;

    public LiftRide(String resortID, Integer dayNum, String timeStamp, String skierID, String liftID) {
        this.resortID = resortID;
        this.dayNum = dayNum;
        this.timeStamp = timeStamp;
        this.skierID = skierID;
        this.liftID = liftID;
    }

    public LiftRide() {
    }

    public String getResortID() {
        return resortID;
    }

    public void setResortID(String resortID) {
        this.resortID = resortID;
    }

    public Integer getDayNum() {
        return dayNum;
    }

    public void setDayNum(Integer dayNum) {
        this.dayNum = dayNum;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSkierID() {
        return skierID;
    }

    public void setSkierID(String skierID) {
        this.skierID = skierID;
    }

    public String getLiftID() {
        return liftID;
    }

    public void setLiftID(String liftID) {
        this.liftID = liftID;
    }

    @Override
    public String toString() {
        return "LiftRide{" +
                "resortID='" + resortID + '\'' +
                ", dayNum=" + dayNum +
                ", timeStamp='" + timeStamp + '\'' +
                ", skierID='" + skierID + '\'' +
                ", liftID='" + liftID + '\'' +
                '}';
    }
}

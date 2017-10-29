package com.kingston.webApp.dataEntity;

public class LiftRide {
    private Long id;
    private String resortID;
    private Integer dayNum;
    private String timeStamp;
    private String skierID;
    private String liftID;
//    private Integer vertical;

    private static final int[] verticals = {200, 300, 400, 500};

    public LiftRide(String resortID, Integer dayNum, String timeStamp, String skierID, String liftID) {
        this.resortID = resortID;
        this.dayNum = dayNum;
        this.timeStamp = timeStamp;
        this.skierID = skierID;
        this.liftID = liftID;
//        this.vertical = vertical;
    }

    public LiftRide() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

//    public Integer getVertical() {
//        return vertical;
//    }
//
//    public void setVertical(Integer vertical) {
//        this.vertical = vertical;
//    }

    public static int getVerticalByLiftId(String liftID) {
        Integer liftIDNum = Integer.valueOf(liftID);
        int liftVertical = verticals[(liftIDNum - 1) / 10];
        return liftVertical;
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

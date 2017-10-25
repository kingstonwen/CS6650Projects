package com.kingston.webApp.dataEntity;


public class SkierDayInfo {

    private String skierID;
    private Integer dayNum;
    private Integer totalVertical;
    private Integer numOfLiftRides;

    public SkierDayInfo(String skierID, Integer dayNum, Integer totalVertical, Integer numOfLiftRides) {
        this.skierID = skierID;
        this.dayNum = dayNum;
        this.totalVertical = totalVertical;
        this.numOfLiftRides = numOfLiftRides;
    }

    public SkierDayInfo() {
    }

    public String getSkierID() {
        return skierID;
    }

    public void setSkierID(String skierID) {
        this.skierID = skierID;
    }

    public Integer getDayNum() {
        return dayNum;
    }

    public void setDayNum(Integer dayNum) {
        this.dayNum = dayNum;
    }

    public Integer getTotalVertical() {
        return totalVertical;
    }

    public void setTotalVertical(Integer totalVertical) {
        this.totalVertical = totalVertical;
    }

    public Integer getNumOfLiftRides() {
        return numOfLiftRides;
    }

    public void setNumOfLiftRides(Integer numOfLiftRides) {
        this.numOfLiftRides = numOfLiftRides;
    }
}

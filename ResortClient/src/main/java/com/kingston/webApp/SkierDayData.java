package com.kingston.webApp;


public class SkierDayData {
    private String skierID;
    private Integer dayNum;
    private Integer totalVertical;
    private Integer numOfLiftRides;

    public SkierDayData(String skierID, Integer dayNum, Integer totalVertical, Integer numOfLiftRides) {
        this.skierID = skierID;
        this.dayNum = dayNum;
        this.totalVertical = totalVertical;
        this.numOfLiftRides = numOfLiftRides;
    }

    public SkierDayData() {
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

    @Override
    public String toString() {
        return "SkierDayData{" +
                "skierID='" + skierID + '\'' +
                ", dayNum=" + dayNum +
                ", totalVertical=" + totalVertical +
                ", numOfLiftRides=" + numOfLiftRides +
                '}';
    }
}

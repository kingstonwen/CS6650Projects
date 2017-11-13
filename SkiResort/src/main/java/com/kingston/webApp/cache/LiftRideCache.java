package com.kingston.webApp.cache;

import com.kingston.webApp.dataEntity.LiftRide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiftRideCache {

    private List<LiftRide> listOfLifts;

    public LiftRideCache() {
//        this.listOfLifts = Collections.synchronizedList(new ArrayList<>());
        this.listOfLifts = new ArrayList<>();
    }

    public List<LiftRide> getListOfLifts() {
        return listOfLifts;
    }

    public synchronized List<LiftRide> getAndClearCache() {
        List<LiftRide> liftRideList = this.listOfLifts;
        this.listOfLifts = new ArrayList<>();
        return liftRideList;
    }

    public synchronized void addLift(LiftRide liftRide) {
        this.listOfLifts.add(liftRide);
    }

    public synchronized int size() {
        return this.listOfLifts.size();
    }
}

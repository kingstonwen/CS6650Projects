package com.kingston.webApp.cache;

import com.kingston.webApp.dataEntity.LiftRide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LiftRideCache {

    private List<LiftRide> listOfLifts;

    public LiftRideCache() {
        this.listOfLifts = new ArrayList<>();
//        this.listOfLifts = Collections.synchronizedList(new ArrayList<>());
    }

    public List<LiftRide> getListOfLifts() {
        return listOfLifts;
    }

    public synchronized List<LiftRide> getAndClearCache() {
        List<LiftRide> liftRideList = this.listOfLifts;
        this.listOfLifts = new ArrayList<>();
//        this.listOfLifts = Collections.synchronizedList(new ArrayList<>());
        return liftRideList;
    }

    public synchronized void addLift(LiftRide liftRide) {
        this.listOfLifts.add(liftRide);
    }

    public synchronized int size() {
        return this.listOfLifts.size();
    }
}

package com.kingston.webApp.cache;

import com.kingston.webApp.DAO.LiftRideDAO;
import com.kingston.webApp.DAO.SkierDayInfoDao;
import com.kingston.webApp.dataEntity.LiftRide;
import com.kingston.webApp.dataEntity.SkierDayInfo;

import java.util.*;

public class PostRequestCache {

    private static PostRequestCache instance;

    private List<LiftRide> liftRideList = Collections.synchronizedList(new LinkedList<>());
//    private List<LiftRide> liftRideList = new LinkedList<>();
//    private static BlockingQueue<LiftRide> liftRideList = new ArrayBlockingQueue<>(500);

    private Map<String, SkierDayInfo> skierDayCacheMap = Collections.synchronizedMap(new HashMap<>());
//    private Map<String, SkierDayInfo> skierDayCacheMap = new HashMap<>();

    public static PostRequestCache getInstance(){
        if (instance == null) {
            instance = new PostRequestCache();
        }
        return instance;

    }

    public synchronized void add(LiftRide liftRide) {
        if (liftRide.getTimeStamp().equals("0")) {
            sendAndClearLiftCache();
            sendAndClearMapCache();
        } else {
            liftRideList.add(liftRide);
            if(liftRideList.size() >= 1000) {
                sendAndClearLiftCache();
            }
            skierDayCacheAdd(liftRide);
            if (skierDayCacheMap.size() >= 500) {
                sendAndClearMapCache();
            }
        }
    }

    private void skierDayCacheAdd(LiftRide liftRide) {
        String skierID = liftRide.getSkierID();
        int dayNum = liftRide.getDayNum();
        String key = skierID + "&" + dayNum;
        int verticalFromLift = LiftRide.getVerticalByLiftId(liftRide.getLiftID());
        if (!this.skierDayCacheMap.containsKey(key)) {
            skierDayCacheMap.put(key,
                    new SkierDayInfo(skierID, dayNum, verticalFromLift, 1));
        } else {
            skierDayCacheMap.get(key).update(verticalFromLift);
        }
    }


    private void sendAndClearLiftCache() {
        if (this.liftRideList.isEmpty()) {
            return;
        }
        LiftRideDAO liftRideDAO = new LiftRideDAO();
        liftRideDAO.batchInsert(liftRideList);
        liftRideList.clear();
    }

    private void sendAndClearMapCache() {
        if (this.skierDayCacheMap.isEmpty()) {
            return;
        }
        SkierDayInfoDao skierDayInfoDao = new SkierDayInfoDao();
        List<SkierDayInfo> skierDayInfoList = new LinkedList<>(skierDayCacheMap.values());
        skierDayInfoDao.batchUpdateSkierDayInfo(skierDayInfoList);
        skierDayCacheMap.clear();
    }
}

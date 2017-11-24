package com.kingston.webApp.cache;

import com.kingston.webApp.DAO.LiftRideDAO;
import com.kingston.webApp.DAO.SkierDayInfoDao;
import com.kingston.webApp.dataEntity.LiftRide;
import com.kingston.webApp.dataEntity.SkierDayInfo;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PostRequestCache {

    public static final String END_OF_REQUEST = "0";
    public static final int CACHE_LIST_UPPER_LIMIT = 100;
    public static final int INIT_NUM_OF_LIFT_RIDES = 1;
    private static PostRequestCache instance;

    private List<LiftRide> liftRideList = Collections.synchronizedList(new LinkedList<>());
//    private List<LiftRide> liftRideList = new CopyOnWriteArrayList<>();
//    private static BlockingQueue<LiftRide> liftRideList = new ArrayBlockingQueue<>(1000);

//    private Map<String, SkierDayInfo> skierDayCacheMap = Collections.synchronizedMap(new HashMap<>());
    private Map<String, SkierDayInfo> skierDayCacheMap = new ConcurrentHashMap<>();

    public static PostRequestCache getInstance(){
        if (instance == null) {
            instance = new PostRequestCache();
        }

        return instance;

    }

    private boolean ifEndOfRequestInADay(LiftRide liftRide) {
        return liftRide.getTimeStamp().equals(END_OF_REQUEST);
    }

    public synchronized void add(LiftRide liftRide) {
        if (ifEndOfRequestInADay(liftRide)) {
            sendAndClearLiftCache();
        } else {
            liftRideList.add(liftRide);
            if(liftRideList.size() >= CACHE_LIST_UPPER_LIMIT) {
                sendAndClearLiftCache();
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
                    new SkierDayInfo(skierID, dayNum, verticalFromLift, INIT_NUM_OF_LIFT_RIDES));
        } else {
            skierDayCacheMap.get(key).update(verticalFromLift);
        }
    }


    private synchronized void sendAndClearLiftCache() {
        if (this.liftRideList.isEmpty()) {
            return;
        }

        LiftRideDAO liftRideDAO = new LiftRideDAO();
        liftRideDAO.multipleRowsInsert(liftRideList);

        SkierDayInfoDao skierDayInfoDao = new SkierDayInfoDao();
        skierDayInfoDao.updateAllSkierDayInfo(liftRideList);
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

package com.kingston.webApp.cache;

import com.kingston.webApp.dataEntity.LiftRide;
import com.kingston.webApp.dataEntity.SkierDayInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SkierDayCache {
    public static final int INIT_NUM_OF_LIFT_RIDES = 1;
    private Map<String, SkierDayInfo> skierDayCacheMap;

    public SkierDayCache() {
        this.skierDayCacheMap = new ConcurrentHashMap<>();
    }

    public synchronized void addLift(LiftRide liftRide) {
        String skierID = liftRide.getSkierID();
        int dayNum = liftRide.getDayNum();
        String key = "skierid:" + skierID + "daynum:" + dayNum;
        int verticalByLift = LiftRide.getVerticalByLiftId(liftRide.getLiftID());
        if (!this.skierDayCacheMap.containsKey(key)) {
            skierDayCacheMap.put(key,
                    new SkierDayInfo(skierID, dayNum, verticalByLift, INIT_NUM_OF_LIFT_RIDES));
        } else {
            skierDayCacheMap.get(key).update(verticalByLift);
        }
    }

    public synchronized List<SkierDayInfo> getAndClearCache() {
        List<SkierDayInfo> skierDayInfoList = new ArrayList<>(this.skierDayCacheMap.values());
        this.skierDayCacheMap = new HashMap<>();
        return skierDayInfoList;
    }

    public synchronized int size() {
        return this.skierDayCacheMap.size();
    }
}

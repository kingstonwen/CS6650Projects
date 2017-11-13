package com.kingston.webApp.cache;

import com.kingston.webApp.dataEntity.LiftRide;
import com.kingston.webApp.dataEntity.SkierDayInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkierDayCache {
    private Map<String, SkierDayInfo> skierDayCacheMap;

    public SkierDayCache() {
        this.skierDayCacheMap = new HashMap<>();
    }

    public synchronized void addLift(LiftRide liftRide) {
        String skierID = liftRide.getSkierID();
        int dayNum = liftRide.getDayNum();
        String key = "skierid:" + skierID + "daynum:" + dayNum;
        int verticalFromLift = LiftRide.getVerticalByLiftId(liftRide.getLiftID());
        if (!this.skierDayCacheMap.containsKey(key)) {
            skierDayCacheMap.put(key,
                    new SkierDayInfo(skierID, dayNum, verticalFromLift, 1));
        } else {
            skierDayCacheMap.get(key).update(verticalFromLift);
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

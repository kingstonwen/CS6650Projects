package com.kingston.webApp.cache;

import com.kingston.webApp.DAO.LiftRideDAO;
import com.kingston.webApp.DAO.SkierDayInfoDao;
import com.kingston.webApp.dataEntity.LiftRide;
import com.kingston.webApp.dataEntity.SkierDayInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PostRequestCache {

    private BlockingQueue<LiftRide> liftRideList = new ArrayBlockingQueue<LiftRide>(500);

    public synchronized void add(LiftRide liftRide) {
        liftRideList.offer(liftRide);
        if(liftRideList.size() == 500) {
            List<LiftRide> cachedList = new ArrayList<>(liftRideList);

            LiftRideDAO liftRideDAO = new LiftRideDAO();
            liftRideDAO.saveAll(cachedList);

            SkierDayInfoDao skierDayInfoDao = new SkierDayInfoDao();
            skierDayInfoDao.updateAllSkierDayInfo(cachedList);
            liftRideList.clear();
        }
    }
}

package com.kingston.webApp.cache;

import com.kingston.webApp.DAO.LiftRideDAO;
import com.kingston.webApp.DAO.SkierDayInfoDao;
import com.kingston.webApp.dataEntity.LiftRide;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CacheMainThread implements Runnable {

    private static final int CACHE_SIZE_MAX_LIMIT = 250;
    private static final int ONE_SEC = 1000;
    private static final int CACHE_TIME_OUT = 5 * ONE_SEC;

    private LiftRideCache liftRideCache;
//    private SkierDayCache skierDayCache;
    private ExecutorService liftCacheExecutorService;
//    private ExecutorService skierDayCacheExecutorService;

    public CacheMainThread() {
        this.liftRideCache = new LiftRideCache();
//        this.skierDayCache = new SkierDayCache();
        this.liftCacheExecutorService = Executors.newSingleThreadExecutor();
//        this.skierDayCacheExecutorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void run() {
        Long liftCacheStartTime = System.currentTimeMillis();
//        Long skierDayCacheStartTime = System.currentTimeMillis();
        while(true) {
            Long currentTime = System.currentTimeMillis();
            if (ifCacheMatured(liftCacheStartTime, currentTime, liftRideCache.size())) {

                this.liftCacheExecutorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        List<LiftRide> cacheList = liftRideCache.getAndClearCache();
                        if (!cacheList.isEmpty()) {
                            LiftRideDAO liftRideDAO = new LiftRideDAO();
                            liftRideDAO.batchInsert(cacheList);
//                        while (!insertFailed.isEmpty()) {
//                            insertFailed = liftRideDAO.batchInsert(insertFailed);
//                        }
                            SkierDayInfoDao skierDayInfoDao = new SkierDayInfoDao();
                            skierDayInfoDao.updateAllSkierDayInfo(cacheList);
//                        while(!updateFailed.isEmpty()) {
//                            updateFailed = skierDayInfoDao.updateAllSkierDayInfo(updateFailed);
//                        }
                        }
                    }
                });

                liftCacheStartTime = currentTime;
            }

//            currentTime = System.currentTimeMillis();
//            if (ifCacheMatured(skierDayCacheStartTime, currentTime,skierDayCache.size())) {
//
//                this.skierDayCacheExecutorService.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        SkierDayInfoDao skierDayInfoDao = new SkierDayInfoDao();
//                        skierDayInfoDao.batchUpdateSkierDayInfo(skierDayCache.getAndClearCache());
//                    }
//                });
//
//                skierDayCacheStartTime = currentTime;
//            }


            try {
                Thread.sleep(CACHE_TIME_OUT / 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean ifCacheMatured(Long startTime, Long currentTime, Integer cacheSize) {
        return cacheSize > CACHE_SIZE_MAX_LIMIT || currentTime - startTime > CACHE_TIME_OUT;
    }


    public LiftRideCache getLiftRideCache() {
        return liftRideCache;
    }

//    public SkierDayCache getSkierDayCache() {
//        return skierDayCache;
//    }
}

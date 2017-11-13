package com.kingston.webApp.cache;

import com.kingston.webApp.DAO.LiftRideDAO;
import com.kingston.webApp.DAO.SkierDayInfoDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CacheMainThread implements Runnable {

    private static final int CACHE_SIZE_MAX_LIMIT = 1000;
    private static final int ONE_SEC = 1000;
    private static final int CACHE_TIME_OUT = 5 * ONE_SEC;

    private LiftRideCache liftRideCache;
    private SkierDayCache skierDayCache;
    private ExecutorService liftCacheExecutorService;
    private ExecutorService skierDayCacheExecutorService;

    public CacheMainThread() {
        this.liftRideCache = new LiftRideCache();
        this.skierDayCache = new SkierDayCache();
        this.liftCacheExecutorService = Executors.newSingleThreadExecutor();
        this.skierDayCacheExecutorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void run() {
        Long liftCacheStartTime = System.currentTimeMillis();
        Long skierDayCacheStartTime = System.currentTimeMillis();
        while(true) {
            Long currentTime = System.currentTimeMillis();
            if (ifLiftCacheMatured(liftCacheStartTime, currentTime)) {

                this.liftCacheExecutorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        LiftRideDAO liftRideDAO = new LiftRideDAO();
                        liftRideDAO.batchInsert(liftRideCache.getAndClearCache());
                    }
                });

                liftCacheStartTime = currentTime;
            }

            currentTime = System.currentTimeMillis();
            if (ifSkierDayCacheMatured(skierDayCacheStartTime, currentTime)) {

                this.skierDayCacheExecutorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        SkierDayInfoDao skierDayInfoDao = new SkierDayInfoDao();
                        skierDayInfoDao.batchUpdateSkierDayInfo(skierDayCache.getAndClearCache());
                    }
                });

                skierDayCacheStartTime = currentTime;
            }


            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean ifLiftCacheMatured(Long startTime, Long currentTime) {
        return this.liftRideCache.size() > CACHE_SIZE_MAX_LIMIT || currentTime - startTime > CACHE_TIME_OUT;
    }

    private boolean ifSkierDayCacheMatured(Long startTime, Long currentTime) {
        return this.skierDayCache.size() > CACHE_SIZE_MAX_LIMIT || currentTime - startTime > CACHE_TIME_OUT;
    }

    public LiftRideCache getLiftRideCache() {
        return liftRideCache;
    }

    public SkierDayCache getSkierDayCache() {
        return skierDayCache;
    }
}

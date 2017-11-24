package com.kingston.webApp.cache;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CacheContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        CacheMainThread cacheMainThread = new CacheMainThread();
        servletContext.setAttribute("LiftCache", cacheMainThread.getLiftRideCache());
        new Thread(cacheMainThread).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}

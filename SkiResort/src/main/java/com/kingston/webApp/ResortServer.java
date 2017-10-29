package com.kingston.webApp;

import com.kingston.webApp.DAO.LiftRideDAO;
import com.kingston.webApp.DAO.SkierDayInfoDao;
import com.kingston.webApp.cache.PostRequestCache;
import com.kingston.webApp.dataEntity.LiftRide;
import com.kingston.webApp.dataEntity.SkierDayInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("/")
public class ResortServer {

    @GET
    @Path("myvert/{skierID}&{dayNum}")
    @Produces(MediaType.APPLICATION_JSON)
    public SkierDayInfo getSkierDayInfo(@PathParam("skierID") String skierId, @PathParam("dayNum") Integer dayNum) {
        SkierDayInfoDao skierDayInfoDao = new SkierDayInfoDao();
        return skierDayInfoDao.getBySkierIDAndDayNum(skierId, dayNum);
    }

    @POST
    @Path("load/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LiftRide postLiftRideInfo(LiftRide liftRide) {
//        LiftRideDAO liftRideDAO = new LiftRideDAO();
//        SkierDayInfoDao skierDayInfoDao = new SkierDayInfoDao();
//        try {
//            liftRideDAO.save(liftRide);
//            skierDayInfoDao.updateSkierDayInfo(liftRide);
            PostRequestCache postRequestCache = new PostRequestCache();
            postRequestCache.add(liftRide);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return liftRide;
    }

    @DELETE
    @Path("lift/deleteAllByDay/{dayNum}")
    public void deleteAllByDay(@PathParam("dayNum") Integer dayNum) {
        LiftRideDAO liftRideDAO = new LiftRideDAO();
        liftRideDAO.deleteAllByDay(dayNum);
    }


    @Path("home")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus() {
        return "Hello World!";
    }

    @Path("home")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public int postText(String content) {
        return content.length();
    }
}

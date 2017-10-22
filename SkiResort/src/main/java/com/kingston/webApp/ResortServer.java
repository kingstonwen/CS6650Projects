package com.kingston.webApp;

import com.kingston.webApp.data.LiftRide;
import com.kingston.webApp.data.SkierDayInfo;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class ResortServer {

    @GET
    @Path("myvert/{skierID}&{dayNum}")
    @Produces(MediaType.APPLICATION_JSON)
    public SkierDayInfo getSkierDayInfo(@PathParam("skierID") String skierId, @PathParam("dayNum") Integer dayNum) {
        SkierDayInfo test = new SkierDayInfo();
        test.setDayNum(dayNum);
        test.setSkierID(skierId);
        test.setTotalVertical(100.0);
        test.setNumOfLiftRides(5);
        return test;
    }

    @POST
    @Path("load/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LiftRide postLiftRideInfo(LiftRide liftRide) {
        return liftRide;
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

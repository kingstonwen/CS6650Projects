package com.kingston;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by marom on 27/09/16.
 */
@Path("home")
public class MyServer {

  /**
   * Method handling HTTP GET requests. The returned object will be sent
   * to the client as "text/plain" media type.
   *
   * @return String that will be returned as a text/plain response.
   */

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String getStatus() {
    return ("alive");
  }

  @POST
  @Consumes(MediaType.TEXT_PLAIN)
  public int postText(String content) {
    return (content.length());
  }
}

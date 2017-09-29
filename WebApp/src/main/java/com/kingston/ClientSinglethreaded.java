package com.kingston;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Created by sizhewen on 9/27/17.
 */
public class ClientSinglethreaded {

  public static void main(String[] args) {


    Client client = ClientBuilder.newClient();

    long start = System.currentTimeMillis();
    for (int i = 0; i < 10; i++) {
//      WebTarget webTarget = client.target("http://localhost:8080/rest/home");
      WebTarget webTarget = client.target("http://52.91.183.56:8080/WebApp_war/rest/home");

      String response = webTarget.request(MediaType.TEXT_PLAIN).get(String.class);
//      System.out.println(response);
    }

    long end = System.currentTimeMillis();
    System.out.println("Time taking: " + (end - start));
  }
}

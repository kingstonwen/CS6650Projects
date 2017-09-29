package com.kingston.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by sizhewen on 9/26/17.
 */
public class MyClient {

  private static void getRequest() {
    Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target("http://localhost:8080/rest/home");
    String response = webTarget.request(MediaType.TEXT_PLAIN).get(String.class);
    System.out.println(response);
  }

  private static void postRequest() {
    Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target("http://localhost:8080/rest/home");
    Response response = webTarget.request().post(Entity.text("abc"));
    System.out.println(response);
  }

  public static void main(String[] args) {
    getRequest();
    postRequest();
  }
}

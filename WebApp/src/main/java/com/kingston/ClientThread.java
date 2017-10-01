package com.kingston;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by sizhewen on 9/27/17.
 */
public class ClientThread extends Thread {

  private int countOfRequestSent = 0;
  private int countOfSuccessfulResponse = 0;
  private int numOfIterations;
  private String serverAddress;
  private int portOnServer;
  private CyclicBarrier barrier;
  private List<Long> requestLatencies;

  public ClientThread(int numOfIterations, String serverAddress,int portOnServer, CyclicBarrier barrier) {
    this.numOfIterations = numOfIterations;
    this.serverAddress = serverAddress;
    this.portOnServer = portOnServer;
    this.barrier = barrier;
    this.requestLatencies = Collections.synchronizedList(new ArrayList<Long>());
  }

  public synchronized void requestSentIncrement() {
    countOfRequestSent++;
  }

  public synchronized void successfulResponseIncrement() {countOfSuccessfulResponse++; }

  public int getCountOfRequestSent() {
    return countOfRequestSent;
  }

  public int getCountOfSuccessfulResponse() {
    return countOfSuccessfulResponse;
  }

  public List<Long> getRequestLatencies() {
    return requestLatencies;
  }

  public void run() {
    Client client = ClientBuilder.newClient();

    for (int i = 0; i < this.numOfIterations; i++) {
      WebTarget webTarget = client.target(serverAddress + ":" + portOnServer + "/WebApp_war/rest/home");

      long startOfRequest = System.currentTimeMillis();
      Response getResponse = webTarget.request(MediaType.TEXT_PLAIN).get();
      getResponse.close();
      Response postResponse = webTarget.request().post(Entity.text("abc"));
      postResponse.close();
      long endOfRequest = System.currentTimeMillis();
      Long latencyPerRequestCombo = endOfRequest - startOfRequest;
      requestLatencies.add(latencyPerRequestCombo);
      requestSentIncrement();
      if (getResponse.getStatus() == 200 && postResponse.getStatus() == 200) {
        successfulResponseIncrement();
      }
    }

    try {
//      System.out.println("Thread : " + Thread.currentThread().getName() + " is waiting");
      barrier.await();
//      System.out.println("Thread : " + Thread.currentThread().getName() + " is released");
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (BrokenBarrierException e) {
      e.printStackTrace();
    }
  }
}

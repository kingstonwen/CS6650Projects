package com.kingston;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by sizhewen on 9/27/17.
 */
public class ClientMultithreaded {
  private static CyclicBarrier barrier;
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh/mm/ss");

  public static void main(String[] args) {
    Integer numOfThreads = null;
    Integer numOfIterations = null;
    String serverAddress = null;
    Integer portOnServer = null;
    int numOfRequestSent = 0;
    int numOfSuccessfulResponse = 0;
    List<Long> allLatencyResult = new ArrayList<Long>();

    if (args.length == 4) {
      numOfThreads = Integer.parseInt(args[0]);
      numOfIterations = Integer.parseInt(args[1]);
      serverAddress = args[2];
      portOnServer = Integer.parseInt(args[3]);
    } else {
      numOfThreads = 100;
      numOfIterations = 100;
      serverAddress = "http://52.91.183.56";
      portOnServer = 8080;
    }


    barrier = new CyclicBarrier(numOfThreads + 1);

    ClientThread[] clients = new ClientThread[numOfThreads];

    Date date = Calendar.getInstance().getTime();
    System.out.println("Client starting ... Time: " + sdf.format(date));

    long start = System.currentTimeMillis();

    for (int i = 0; i < numOfThreads; i++) {
      clients[i] = new ClientThread(numOfIterations, serverAddress, portOnServer, barrier);
      clients[i].start();
    }

    try {
//      System.out.println("Thread : " + Thread.currentThread().getName() + " is waiting");
      barrier.await();
//      System.out.println("Main thread finishing");
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (BrokenBarrierException e) {
      e.printStackTrace();
    }

// Step 4 ==================

//    long end = System.currentTimeMillis();

//    date = Calendar.getInstance().getTime();
//    System.out.println("All threads complete ... Time: " + sdf.format(date));
//
//    for(int i = 0;i < numOfThreads; i++) {
//      numOfRequestSent += clients[i].getCountOfRequestSent();
//      numOfSuccessfulResponse += clients[i].getCountOfSuccessfulResponse();
//    }
//
//    System.out.println("Total number of requests sent: " + numOfRequestSent);
//    System.out.println("Total number of Successful responses: " + numOfSuccessfulResponse);
//    System.out.println("Test Wall Time: " + (end - start) / 1000.0 + " seconds");

    //Step 5 =================

    long end = System.currentTimeMillis();
    System.out.println("Test Wall Time: " + (end - start) / 1000.0 + " seconds");

    for(int i = 0;i < numOfThreads; i++) {
      numOfRequestSent += clients[i].getCountOfRequestSent();
      numOfSuccessfulResponse += clients[i].getCountOfSuccessfulResponse();
      allLatencyResult.addAll(clients[i].getRequestLatencies());
    }

    System.out.println("Total number of requests sent: " + numOfRequestSent);
    System.out.println("Total number of Successful responses: " + numOfSuccessfulResponse);

    //sort the entire list of latency value for every request combo
    Collections.sort(allLatencyResult);

    // mean latencies
    long allLatenciesSum = 0;
    for(int i = 0; i < allLatencyResult.size(); i++) {
      allLatenciesSum += allLatencyResult.get(i);
    }
    long allLatencyMean = allLatenciesSum / allLatencyResult.size();

    System.out.println("Mean latencies for all requests: " + allLatencyMean + "ms");

    //median latencies
    long allLatencyMedian = 0;

    if (allLatencyResult.size() % 2 == 0) {
      int index1 = allLatencyResult.size() / 2 - 1;
      int index2 = index1 + 1;
      allLatencyMedian = (allLatencyResult.get(index1) + allLatencyResult.get(index2)) / 2;
    } else {
      int indexOfMean = allLatencyResult.size() / 2;
      allLatencyMedian = (allLatencyResult.get(indexOfMean));
    }

    System.out.println("Median latencies for all requests: " + allLatencyMedian + "ms");

    double nintyninePercentileIndex = allLatencyResult.size() * 0.99 - 1;
    double nintyFivePersentileIndex = allLatencyResult.size() * 0.95 - 1;

    long nintyninePersentileLatency = allLatencyResult.get((int) nintyninePercentileIndex);
    long nintyFivePersentileLatency = allLatencyResult.get((int) nintyFivePersentileIndex);

    System.out.println("99th percentile latency: " + nintyninePersentileLatency);
    System.out.println("95th percentile latency: " + nintyFivePersentileLatency);
  }
}

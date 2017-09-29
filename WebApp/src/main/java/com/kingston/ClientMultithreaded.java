package com.kingston;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    long end = System.currentTimeMillis();
    date = Calendar.getInstance().getTime();
    System.out.println("All threads complete ... Time: " + sdf.format(date));

    for(int i = 0;i < numOfThreads; i++) {
      numOfRequestSent += clients[i].getCountOfRequestSent();
      numOfSuccessfulResponse += clients[i].getCountOfSuccessfulResponse();
    }

    System.out.println("Total number of requests sent: " + numOfRequestSent);
    System.out.println("Total number of Successful responses: " + numOfSuccessfulResponse);
    System.out.println("Test Wall Time: " + (end - start) / 1000.0 + " seconds");
  }
}

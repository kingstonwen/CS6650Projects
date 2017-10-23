package com.kingston.webApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ResortClient {

    public static final String TEST_CSV = "test.csv";
    public static final String DAY1_CSV = "BSDSAssignment2Day1.csv";

    public static void main(String[] args) throws Exception{
        String ip = "http://localhost";
        String portNum = "8080";
        String postRequestPath = "/rest/load";
        LiftDataReader reader = new LiftDataReader(DAY1_CSV);
        List<LiftData> liftDataList = reader.getList();

        int numOfThreads = 100;

        int dataSize = liftDataList.size();
        int requestPerThread = dataSize / numOfThreads;

        List<Callable<MetricsOfRequest>> postRequestTaskList = Collections.synchronizedList(new ArrayList<>());

        for(int i = 0; i < dataSize; i += requestPerThread) {
            int endIndex = i + requestPerThread;
            if (i + requestPerThread >= dataSize) {
                endIndex = dataSize;
            }
            Callable<MetricsOfRequest> task =
                    new PostRequestTask(ip, portNum, postRequestPath, liftDataList.subList(i, endIndex));
            postRequestTaskList.add(task);
        }



        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        System.out.println("Client service is starting ...");
        Long start = System.currentTimeMillis();

        List<Future<MetricsOfRequest>> futureList = executorService.invokeAll(postRequestTaskList);
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        int requestSent = 0;
        int successfulRequestSent = 0;
        for(Future<MetricsOfRequest> future : futureList) {
            requestSent += future.get().getNumOfRequestSent();
            successfulRequestSent += future.get().getNumOfSuccessfulRequestSent();
        }
        System.out.println("request sent : " + requestSent);
        System.out.println("Successful request sent : " + successfulRequestSent);


        System.out.println(numOfThreads + " thread(s) working on postTask: " + (System.currentTimeMillis() - start) / 1000.0);
    }

}

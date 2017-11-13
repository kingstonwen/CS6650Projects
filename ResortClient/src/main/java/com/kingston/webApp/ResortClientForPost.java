package com.kingston.webApp;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ResortClientForPost {

    public static final String TEST_CSV = "test.csv";
    public static final String DAY1_CSV = "BSDSAssignment2Day1.csv";
    public static final String DAY2_CSV = "BSDSAssignment2Day2.csv";
    public static final String LOCAL_HOST_PROTOCAL = "http://localhost";
    public static final String PORT_NUMBER = "8080";
    public static final String FILE_LOCAL_POST_PATH = "/rest/load";
    public static final String AWS_EC2_PROTOCAL = "http://35.167.15.233";
    public static final String FILE_AWS_POST_PATH = "/SkiResort/rest/load";

    public static void main(String[] args) throws Exception{
        Boolean ifTesting = false;
        String IPAddress = ifTesting ? LOCAL_HOST_PROTOCAL : AWS_EC2_PROTOCAL;
        String postRequestPath = ifTesting ? FILE_LOCAL_POST_PATH : FILE_AWS_POST_PATH;
        String portNum = PORT_NUMBER;
        int numOfThreads = 200;
        String importFile = DAY1_CSV;

        LiftDataReader reader = new LiftDataReader(importFile);
        List<LiftData> liftDataList = reader.getList();
        System.out.println("Finishing importing " + importFile + " to file reader.");

        System.out.println("There are " + numOfThreads + " threads.");
        int dataSize = liftDataList.size();
        int requestPerThread = dataSize / numOfThreads;

        List<Callable<MetricsOfRequest>> postRequestTaskList = Collections.synchronizedList(new ArrayList<>());

        for(int i = 0; i < dataSize; i += requestPerThread) {
            int endIndex = i + requestPerThread;
            if (i + requestPerThread >= dataSize) {
                endIndex = dataSize;
            }
            Callable<MetricsOfRequest> task =
                    new PostRequestTask(IPAddress, portNum, postRequestPath, liftDataList.subList(i, endIndex));
            postRequestTaskList.add(task);
        }


        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        System.out.println("Client POST service is starting at " + LocalDateTime.now());

        Long startTime = System.currentTimeMillis();
        List<Future<MetricsOfRequest>> futureList = executorService.invokeAll(postRequestTaskList);
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        Long endingTime = System.currentTimeMillis();

        //send EOF signal to the server and clear out server cache
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(AWS_EC2_PROTOCAL + ":" + PORT_NUMBER + FILE_AWS_POST_PATH);
        Response response = webTarget.request().post(Entity.json(new LiftData("0")));
        if (response != null) {
            response.close();
        }
        if (client != null) {
            client.close();
        }



        System.out.println("Client POST service is wrapping up ...");

        StatReport statReport = new StatReport();
        for(Future<MetricsOfRequest> future : futureList) {
            statReport.digestMetric(future.get());
        }

        statReport.setTotalWallTime(endingTime - startTime);
        System.out.println(statReport.getReport());

        StatGraph graph = new StatGraph("POST Request And Latency", "nthRequest", "latency(ms)");
        graph.importData(statReport.getListOfLatencies(), "latencies");
        graph.exportToFile("PostRequestGraph");
    }

}

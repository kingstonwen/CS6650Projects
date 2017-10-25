package com.kingston.webApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ResortClient {

    public static final String TEST_CSV = "test.csv";
    public static final String DAY1_CSV = "BSDSAssignment2Day1.csv";
    public static final String LOCAL_HOST_PROTOCAL = "http://localhost";
    public static final String PORT_NUMBER = "8080";
    public static final String FILE_LOCAL_POST_PATH = "/rest/load";
    public static final String AWS_EC2_PROTOCAL = "http://34.212.77.130";
    public static final String FILE_AWS_POST_PATH = "/SkiResort/rest/load";

    public static void main(String[] args) throws Exception{
        Boolean ifTesting = true;
        String IPAddress = ifTesting ? LOCAL_HOST_PROTOCAL : AWS_EC2_PROTOCAL;
        String postRequestPath = ifTesting ? FILE_LOCAL_POST_PATH : FILE_AWS_POST_PATH;
        String portNum = PORT_NUMBER;

        LiftDataReader reader = new LiftDataReader(TEST_CSV);
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
                    new PostRequestTask(IPAddress, portNum, postRequestPath, liftDataList.subList(i, endIndex));
            postRequestTaskList.add(task);
        }


        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        System.out.println("Client service is starting ...");

        Long startTime = System.currentTimeMillis();
        List<Future<MetricsOfRequest>> futureList = executorService.invokeAll(postRequestTaskList);
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        Long endingTime = System.currentTimeMillis();

        System.out.println("Client service is wrapping up ...");

        StatReport statReport = new StatReport();
        for(Future<MetricsOfRequest> future : futureList) {
            statReport.digestMetric(future.get());
        }

        statReport.setTotalWallTime(endingTime - startTime);
        System.out.println(statReport.getReport());

        StatGraph graph = new StatGraph("RequestAndLatency", "nthRequest", "latency(ms)");
        graph.importData(statReport.getListOfLatencies(), "latencies");
        graph.exportToFile("testGraph");
    }

}

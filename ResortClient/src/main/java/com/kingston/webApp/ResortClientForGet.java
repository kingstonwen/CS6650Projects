package com.kingston.webApp;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

public class ResortClientForGet {
    public static final String TEST_CSV = "/Users/sizhewen/Code/NEU/CS6650/test.csv";
    public static final String DAY1_CSV = "/Users/sizhewen/Code/NEU/CS6650/BSDSAssignment2Day1.csv";
    public static final String LOCAL_HOST_PROTOCAL = "http://localhost";
    public static final String PORT_NUMBER = "8080";
    public static final String FILE_LOCAL_POST_PATH = "/rest/load";
    public static final String FILE_LOCAL_GET_PATH = "/rest/myvert/";
    public static final String AWS_EC2_PROTOCAL = "http://54.201.205.187";
    public static final String FILE_AWS_POST_PATH = "/SkiResort/rest/load";
    public static final String FILE_AWS_GET_PATH = "/SkiResort/rest/myvert/";

    public static void main(String[] args) throws Exception{
        Boolean ifTesting = false;
        String IPAddress = ifTesting ? LOCAL_HOST_PROTOCAL : AWS_EC2_PROTOCAL;
        String getRequestPath = ifTesting ? FILE_LOCAL_GET_PATH : FILE_AWS_GET_PATH;
        String portNum = PORT_NUMBER;
        String CSVFile = DAY1_CSV;
        int numOfThreads = 80;
        Integer dayNum = 1;

        LiftDataReader reader = new LiftDataReader(CSVFile);
        List<LiftData> liftDataList = reader.getList();
        Set<String> skierIDSet = new HashSet<>();
        for(LiftData liftData : liftDataList) {
            skierIDSet.add(liftData.getSkierID());
        }
        List<String> skierIDList = new ArrayList<>(skierIDSet);
        System.out.println("Finishing reading all skierID from day" + dayNum);
        System.out.println("There are " + numOfThreads + " threads.");
        int dataSize = skierIDList.size();
        System.out.println("There are " + dataSize + " skierIDs.");
        int requestPerThread = dataSize / numOfThreads;

        List<Callable<MetricsOfRequest>> getRequestTaskList = Collections.synchronizedList(new ArrayList<>());

        for(int i = 0; i < dataSize; i += requestPerThread) {
            int endIndex = i + requestPerThread;
            if (i + requestPerThread >= dataSize) {
                endIndex = dataSize;
            }
            Callable<MetricsOfRequest> task =
                    new GetRequestTask(IPAddress, portNum, getRequestPath, skierIDList.subList(i, endIndex), dayNum);
            getRequestTaskList.add(task);
        }


        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        System.out.println("Client GET service is starting at " + LocalDateTime.now());

        Long startTime = System.currentTimeMillis();

        List<Future<MetricsOfRequest>> futureList = executorService.invokeAll(getRequestTaskList);
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        Long endingTime = System.currentTimeMillis();

        System.out.println("Client GET service is wrapping up ...");

        StatReport statReport = new StatReport();
        for(Future<MetricsOfRequest> future : futureList) {
            statReport.digestMetric(future.get());
        }

        statReport.setTotalWallTime(endingTime - startTime);
        System.out.println(statReport.getReport());

        StatGraph graph = new StatGraph("Get Request And Latency", "nthRequest", "latency(ms)");
        graph.importData(statReport.getListOfLatencies(), "latencies");
        graph.exportToFile("GetRequestTestChart");
    }
}

package com.kingston.webApp;

import com.google.gson.Gson;
import com.sun.deploy.util.SessionState;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import static java.net.HttpURLConnection.HTTP_OK;

public class GetRequestTask implements Callable<MetricsOfRequest> {

    private String ipAddress;
    private String portNum;
    private String requestPath;

    private List<String> skierIDList;
    private Integer dayNum;
    private MetricsOfRequest metrics;

    private List<SkierDayData> skierDayDataList;

    public GetRequestTask(String ipAddress, String portNum, String requestPath, List<String> skierIDList, Integer dayNum) {
        this.ipAddress = ipAddress;
        this.portNum = portNum;
        this.requestPath = requestPath;
        this.skierIDList = skierIDList;
        this.dayNum = dayNum;
        metrics = new MetricsOfRequest();
        skierDayDataList = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public MetricsOfRequest call() throws Exception {
        Client client = ClientBuilder.newClient();
        for(String skierId :skierIDList) {
            this.get(client, skierId);
        }
        client.close();
        return this.metrics;
    }

    private void get(Client client, String skierID) {
        String targetURL = ipAddress + ":" + portNum + requestPath + skierID + "&" + dayNum;
        WebTarget webTarget = client.target(targetURL);
        Response response = null;

        try {
            Long start = System.currentTimeMillis();
            response = webTarget.request().get();
            Long latency = System.currentTimeMillis() - start;
//            if (response.getStatus() != HTTP_OK) {
//                System.out.println(response.readEntity(String.class));
//            }

            if (response.getStatus() == HTTP_OK) {
                Gson gson = new Gson();
                SkierDayData skierDayData = gson.fromJson(response.readEntity(String.class), SkierDayData.class);
//                System.out.println(skierDayData.toString());
                skierDayDataList.add(skierDayData);
            }

            this.metrics.incrementNumOfRequestSent();
            if (response.getStatus() == HTTP_OK) {
                this.metrics.incrementNumOfSuccessfulRequestSent();
            }
            this.metrics.addOneLatency(latency);
        } catch (ProcessingException pe) {
            System.out.println(pe.getMessage());
            pe.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}

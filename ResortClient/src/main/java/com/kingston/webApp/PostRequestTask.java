package com.kingston.webApp;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.Callable;

import static java.net.HttpURLConnection.HTTP_OK;

public class PostRequestTask implements Callable<MetricsOfRequest>{

    private String ipAddress;
    private String portNum;
    private String requestPath;

    private List<LiftData> liftDataList;
    private MetricsOfRequest metrics;

    public PostRequestTask(String ipAddress, String portNum, String requestPath, List<LiftData> liftDataList) {
        this.ipAddress = ipAddress;
        this.portNum = portNum;
        this.requestPath = requestPath;
        this.liftDataList = liftDataList;
        metrics = new MetricsOfRequest();
    }

    @Override
    public MetricsOfRequest call() throws Exception {
        String targetUrl = ipAddress + ":" + portNum + requestPath;

        Client client = ClientBuilder.newClient();
        for(LiftData data : this.liftDataList) {
            this.post(client, targetUrl, data);
        }
        client.close();
        return this.metrics;
    }

    private void post(Client client, String targetUrl, LiftData data) {
        WebTarget webTarget = client.target(targetUrl);
        Response response = null;
        try {
            Long startTime = System.currentTimeMillis();
            response = webTarget.request().post(Entity.json(data));
            Long latency = System.currentTimeMillis() - startTime;
            this.metrics.incrementNumOfRequestSent();

//            if (response.getStatus() != HTTP_OK) {
//                System.err.println(response.readEntity(String.class));
//            }

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

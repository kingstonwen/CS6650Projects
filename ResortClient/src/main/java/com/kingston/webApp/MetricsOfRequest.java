package com.kingston.webApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MetricsOfRequest {
    private AtomicInteger numOfRequestSent;
    private AtomicInteger numOfSuccessfulRequestSent;
    private AtomicLong totalLatencies;
    private List<Long> latenciesList = Collections.synchronizedList(new ArrayList<>());

    public MetricsOfRequest() {
        numOfRequestSent = new AtomicInteger(0);
        numOfSuccessfulRequestSent = new AtomicInteger(0);
        totalLatencies = new AtomicLong(0);
    }

    public void incrementNumOfRequestSent() {
        numOfRequestSent.incrementAndGet();
    }

    public void incrementNumOfSuccessfulRequestSent() {
        numOfSuccessfulRequestSent.incrementAndGet();
    }

    public Integer getNumOfRequestSent() {
        return numOfRequestSent.get();
    }

    public Integer getNumOfSuccessfulRequestSent() {
        return numOfSuccessfulRequestSent.get();
    }


    public Long getTotalLatencies() {
        return totalLatencies.get();
    }

    public void addOneLatency(Long latency) {
        latenciesList.add(latency);
        totalLatencies.addAndGet(latency);
    }
}

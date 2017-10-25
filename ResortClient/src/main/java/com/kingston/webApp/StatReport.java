package com.kingston.webApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatReport {

    private Integer numOfRequestSent;
    private Integer numOfSuccessfulRequestSent;
    private Long totalLatencies;
    private List<Long> listOfLatencies;
    private Long totalWallTime;
    private Boolean isListSorted;

    private static final String template = "Total number of requests sent: %s\n" +
            "Total number of successful responses: %s\n" +
            "Total wall time: %.3f seconds\n" +
            "Mean latency is %.3f seconds\n" +
            "Median latency is %.3f seconds\n" +
            "99th percentile latency is %.3f seconds\n" +
            "95th percentile latency is %.3f seconds\n";

    public StatReport() {
        numOfRequestSent = 0;
        numOfSuccessfulRequestSent = 0;
        totalLatencies = new Long(0);
        listOfLatencies = new ArrayList<>();
        totalWallTime = new Long(0);
        isListSorted = false;
    }

    public Integer getNumOfRequestSent() {
        return numOfRequestSent;
    }

    public Integer getNumOfSuccessfulRequestSent() {
        return numOfSuccessfulRequestSent;
    }

    public Long getTotalLatencies() {
        return totalLatencies;
    }

    public Boolean getListSorted() {
        return isListSorted;
    }

    public Long getTotalWallTime() {
        return totalWallTime;
    }

    public List<Long> getListOfLatencies() {
        return listOfLatencies;
    }

    public void setTotalWallTime(Long totalWallTime) {
        this.totalWallTime = totalWallTime;
    }

    public void digestMetric(MetricsOfRequest metrics) {
        numOfRequestSent += metrics.getNumOfRequestSent();
        numOfSuccessfulRequestSent += metrics.getNumOfSuccessfulRequestSent();
        totalLatencies += metrics.getTotalLatencies();
        listOfLatencies.addAll(metrics.getLatenciesList());
        isListSorted = false;
    }

    public Boolean isEmpty() {
        return this.listOfLatencies.isEmpty();
    }

    public Long getMean() {
        if (isEmpty()) {
            return new Long(-1);
        }
        Long mean = this.totalLatencies / this.listOfLatencies.size();
        return mean;
    }

    public Long getMedian() {
        if (isEmpty()) {
            return new Long(-1);
        }
        return getKthPercentile(50);
    }

    public Long getKthPercentile(Integer percentile) {
        if (!isListSorted) {
            Collections.sort(this.listOfLatencies);
            this.isListSorted = true;
        }

        Double percentileInDecimal = percentile / 100.0;

        Integer index = ((Double)(listOfLatencies.size() * percentileInDecimal)).intValue();
        if (index < 0 || index > listOfLatencies.size()) {
            return new Long(-1);
        }

        Long result = null;
        if (listOfLatencies.size() % 2 == 0) {
            Integer upperIndex = index - 1;
            result = (listOfLatencies.get(upperIndex) + listOfLatencies.get(index)) / 2;
        } else {
            result = listOfLatencies.get(index);
        }

        return result;
    }

    public String getReport() {
        return String.format(template,
                numOfRequestSent,
                numOfSuccessfulRequestSent,
                totalWallTime / 1000.0,
                getMean() / 1000.0,
                getMedian() / 1000.0,
                getKthPercentile(99) / 1000.0,
                getKthPercentile(95) / 1000.0);
    }


}
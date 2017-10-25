package com.kingston.webApp;

import javafx.scene.chart.LineChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class StatGraph{

    private XYSeriesCollection dataSet;
    private String graphTitle;
    private String xTitle;
    private String yTitle;

    public StatGraph(String graphTitle, String xTitle, String yTitle) {
        this.graphTitle = graphTitle;
        this.xTitle = xTitle;
        this.yTitle = yTitle;
        dataSet = new XYSeriesCollection();
    }

    public void importData(List<Long> latencies, String name) {
        XYSeries xySeries = new XYSeries(name);
        for(int i = 0; i < latencies.size(); i++) {
            xySeries.add(i, latencies.get(i));
        }
        this.dataSet.addSeries(xySeries);
    }

    public JFreeChart createGraph() {
        JFreeChart chart =
                ChartFactory.createXYLineChart(this.graphTitle, this.xTitle, this.yTitle, this.dataSet);
        chart.getLegend().setFrame(BlockBorder.NONE);
        return chart;
    }

    public void exportToFile(String fileName) {
        File file = new File(fileName);
        try {
            ChartUtilities.saveChartAsJPEG(file, createGraph(), 640, 480);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

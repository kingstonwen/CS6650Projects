package com.kingston.webApp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LiftDataReader {

    private final String filePath;
//    private List<LiftData> liftDataList = Collections.synchronizedList(new ArrayList<>());
    private List<LiftData> liftDataList = new ArrayList<>();

    private static final int[] verticals = {200, 300, 400, 500};

    public LiftDataReader(String filePath) {
        this.filePath = filePath;
    }

    private void read() {
        try (BufferedReader csvFile = new BufferedReader(new FileReader(this.filePath))) {
            String titleLine = csvFile.readLine();
            String line = null;
            while ((line = csvFile.readLine()) != null) {
                LiftData liftData = createLiftData(line);
                liftDataList.add(liftData);
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("*** Oops! A file was not found : " + fnfe.getMessage());
            fnfe.printStackTrace();
        } catch (IOException ioe){
            System.out.println("Something went wrong! : " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    private LiftData createLiftData(String line) {
        line = line.trim();
        String[] rawLiftData = line.split(",");
        String liftID = rawLiftData[3];
        LiftData liftData = new LiftData(rawLiftData[0], Integer.parseInt(rawLiftData[1]),
                rawLiftData[2], liftID, rawLiftData[4]);
        return liftData;
    }

    private Integer liftIDToVertical(String liftID) {
        Integer liftIDNum = Integer.valueOf(liftID);
        int liftVertical = verticals[(liftIDNum - 1) / 10];
        return liftVertical;
    }

    public List<LiftData> getList(){
        if (this.liftDataList.isEmpty()) {
            this.read();
        }
        //add a end of csv file signal so that the cache on server know when to send cache all out
//        this.liftDataList.add(new LiftData("0"));
        return this.liftDataList;
    }
}

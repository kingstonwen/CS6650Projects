package com.kingston.webApp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LiftDataReader {

    private final String filePath;
    private List<LiftData> liftDataList = new ArrayList<LiftData>();

    public LiftDataReader(String filePath) {
        this.filePath = filePath;
    }

    public void read(String filePath) {
        try (BufferedReader csvFile = new BufferedReader(new FileReader(filePath))) {
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
        LiftData liftData = new LiftData(rawLiftData[0], Integer.parseInt(rawLiftData[1]),
                rawLiftData[2], rawLiftData[3], rawLiftData[4]);
        return liftData;
    }
}

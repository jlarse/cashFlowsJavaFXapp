package com.experiment.firstintelijfxprojectjl;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;

public class MasterFileData implements Serializable {

    final static String SAVE_DIRECTORY = "Cash Flow Save Files";
    final static String CASH_FLOW_EXTENSION = ".cfse";  //cash flow serialize
    final static String MASTER_FILE_NAME = "_Master" + CASH_FLOW_EXTENSION;
    final static String MASTER_FILE_PATH = SAVE_DIRECTORY + File.separator + MASTER_FILE_NAME;

    private LocalDate earliestDate;
    private LocalDate mostRecentDate;

    public MasterFileData()
    {

    }

    public void setMostRecentDate(LocalDate mostRecentDate) {
        this.mostRecentDate = mostRecentDate;
    }

    public void setEarliestDate(LocalDate earliestDate)
    {
        this.earliestDate = earliestDate;
    }

    public LocalDate getMostRecentDate() {
        return mostRecentDate;
    }

    public LocalDate getEarliestDate()
    {
        return earliestDate;
    }

    public static String getSavePath()
    {
        return MASTER_FILE_PATH;
    }

    public static String getSaveDirectory()
    {
        return SAVE_DIRECTORY + File.separator;
    }

    public static String getSaveExtension()
    {
        return CASH_FLOW_EXTENSION;
    }

    @Override
    public String toString()
    {
        String earliest,mostRecent;
        if(earliestDate == null)
            earliest = "Null";
        else
            earliest = earliestDate.toString();
        if(mostRecentDate == null)
            mostRecent = "Null";
        else
            mostRecent = mostRecentDate.toString();

        return "Earliest Date: " + earliest + " Most Recent Date: " +  mostRecent;
    }
}

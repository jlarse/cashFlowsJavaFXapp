/**
 * CashFlowEventHandler.java         Eclipse IDE

 * @author Justin Larsen
 * 7/23/2024
 *
 * Description: Handles button preess events for buttons in Main.java. Functionality for updating the master file
 * adding and removing cash flows is handled here.
 */

package com.experiment.firstintelijfxprojectjl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;

public class CashFlowEventHandler implements EventHandler<ActionEvent> {

    private enum Page
    {
        TITLE_SCREEN,
        REPORT_BY_MONTH,
        REPORT_BY_YEAR,
        CUSTOM_REPORT,
        FULL_REPORT
    }

    private MasterFileData masterFileData;
    private VBox contentDisplay;
    private String fontName;
    private int fontSize;
    private TitleScreen titleScreen;
    private MonthScreen monthScreen;
    private YearlyScreen yearlyScreen;
    private CustomScreen customScreen;
    private FullReportScreen fullReportScreen;
    private Label contentTitle, newCashFlowAdded;
    private Page currentPage;
    private HBox statsBoxBox;
    private LocalDate lastCashFlowDate;

    public CashFlowEventHandler(MasterFileData masterFileData,VBox contentDisplay,HBox statsBoxBox,Label contentTitle, Label newCashFlowAdded ,String fontName, int fontSize) {
        this.masterFileData = masterFileData;
        this.contentDisplay = contentDisplay;
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.titleScreen = new TitleScreen(fontName,fontSize);
        this.contentTitle = contentTitle;
        this.newCashFlowAdded = newCashFlowAdded;
        this.monthScreen = new MonthScreen(masterFileData,statsBoxBox,fontName,fontSize);
        this.yearlyScreen = new YearlyScreen(masterFileData,statsBoxBox,fontName,fontSize);
        this.customScreen = new CustomScreen(masterFileData,statsBoxBox,fontName,fontSize);
        this.fullReportScreen = new FullReportScreen(masterFileData,statsBoxBox,fontName,fontSize);
        this.statsBoxBox = statsBoxBox;


        currentPage = Page.TITLE_SCREEN;
        //Make contentTitle underlined for title screen
        Text underlineTextGraphic= new Text("Title Screen");
        underlineTextGraphic.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.REGULAR, fontSize)); // Set font to Arial, size 20
        underlineTextGraphic.setUnderline(true); // Set underline
        // Create a TextFlow to hold the Text
        TextFlow textFlow = new TextFlow(underlineTextGraphic);
        // Create a Label and set its graphic to the TextFlow
        contentTitle.setGraphic(textFlow);

        contentDisplay.getChildren().addAll(titleScreen);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() instanceof Button sourceButton) {
            switch (sourceButton.getText()) {
                case "Title Screen": {
                    currentPage = Page.TITLE_SCREEN;
                    //Make content title underlined for Title Screen
                    Text underlineTextGraphic = new Text("Title Screen");
                    underlineTextGraphic.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.REGULAR, fontSize)); // Set font to Arial, size 20
                    underlineTextGraphic.setUnderline(true); // Set underline
                    // Create a TextFlow to hold the Text
                    TextFlow textFlow = new TextFlow(underlineTextGraphic);
                    // Create a Label and set its graphic to the TextFlow
                    contentTitle.setGraphic(textFlow);

                    contentDisplay.getChildren().clear();
                    contentDisplay.getChildren().addAll(titleScreen);
                    statsBoxBox.getChildren().clear();
                    break;
                }
                case "Report by Month": {
                    currentPage = Page.REPORT_BY_MONTH;
                    //Make Content title underlined for monthly report
                    Text underlineTextGraphic = new Text("Monthly Report");
                    underlineTextGraphic.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.REGULAR, fontSize)); // Set font to Arial, size 20
                    underlineTextGraphic.setUnderline(true); // Set underline
                    // Create a TextFlow to hold the Text
                    TextFlow textFlow = new TextFlow(underlineTextGraphic);
                    // Create a Label and set its graphic to the TextFlow
                    contentTitle.setGraphic(textFlow);

                    contentDisplay.getChildren().clear();
                    contentDisplay.getChildren().addAll(monthScreen);
                    statsBoxBox.getChildren().clear();
                    monthScreen.displayMonthScreen(false);
                    monthScreen.loadStatsBox();


                    break;
                }
                case "Report by Year":
                {
                    currentPage = Page.REPORT_BY_YEAR;
                    //Make content title underlined for Yearly Report
                    Text underlineTextGraphic = new Text("Yearly Report");
                    underlineTextGraphic.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.REGULAR, fontSize)); // Set font to Arial, size 20
                    underlineTextGraphic.setUnderline(true); // Set underline
                    // Create a TextFlow to hold the Text
                    TextFlow textFlow = new TextFlow(underlineTextGraphic);
                    // Create a Label and set its graphic to the TextFlow
                    contentTitle.setGraphic(textFlow);

                    contentDisplay.getChildren().clear();
                    contentDisplay.getChildren().addAll(yearlyScreen);
                    statsBoxBox.getChildren().clear();
                    yearlyScreen.displayYearScreen(false);
                    yearlyScreen.loadStatsBox();


                    break;
                }
                case "Custom Report":
                {
                    currentPage = Page.CUSTOM_REPORT;
                    //Make content title underlined for Custom Report
                    Text underlineTextGraphic = new Text("Custom Report");
                    underlineTextGraphic.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.REGULAR, fontSize)); // Set font to Arial, size 20
                    underlineTextGraphic.setUnderline(true); // Set underline
                    // Create a TextFlow to hold the Text
                    TextFlow textFlow = new TextFlow(underlineTextGraphic);
                    // Create a Label and set its graphic to the TextFlow
                    contentTitle.setGraphic(textFlow);

                    contentDisplay.getChildren().clear();
                    contentDisplay.getChildren().addAll(customScreen);
                    statsBoxBox.getChildren().clear();
                    customScreen.displayCustomScreen(false);
                    customScreen.loadStatsBox();

                    break;
                }
                case "Full Report":
                {
                    currentPage = Page.FULL_REPORT;
                    //----------------Underlined text for content title
                    Text underlineTextGraphic = new Text("Full Report");
                    underlineTextGraphic.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.REGULAR, fontSize)); // Set font to Arial, size 20
                    underlineTextGraphic.setUnderline(true); // Set underline
                    // Create a TextFlow to hold the Text
                    TextFlow textFlow = new TextFlow(underlineTextGraphic);
                    // Create a Label and set its graphic to the TextFlow
                    contentTitle.setGraphic(textFlow);

                    contentDisplay.getChildren().clear();
                    contentDisplay.getChildren().addAll(fullReportScreen);
                    fullReportScreen.displayFullReportScreen(false);
                    statsBoxBox.getChildren().clear();
                    fullReportScreen.loadStatsBox();
                    break;
                }

                //add new cash flow
                case "Add Cash Flow":
                    //retrieve cash flow from cash flow prompt box
                    CashFlow newCashFlow = CashFlowPromptBox.display(fontName,fontSize,lastCashFlowDate);
                    if(newCashFlow != null)
                    {
                        //set newly added cash flow label to display the cash flow that was added
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                        if(newCashFlow.getAmount() < 0) {
                            newCashFlowAdded.setText("New cash flow added: " + newCashFlow.getLocalDate().format(formatter) + "  " +
                                    newCashFlow.getCategory() + "  -$" + (-1* newCashFlow.getAmount()));
                            newCashFlowAdded.setStyle("-fx-text-fill: red;");
                        }
                        else {
                            newCashFlowAdded.setText("New cash flow added: " + newCashFlow.getLocalDate().format(formatter) + "  " +
                                    newCashFlow.getCategory() + "  +$" + newCashFlow.getAmount());
                            newCashFlowAdded.setStyle("-fx-text-fill: green;");
                        }
                        newCashFlowAdded.setFont(Font.font(fontName,fontSize));

                        //store last cash flow date to preselect the date when a new cash flow is added
                        lastCashFlowDate = newCashFlow.getLocalDate();
                        placeCashFlowInFile(newCashFlow);

                        //reload each type of display to include new cash flow
                        monthScreen.displayMonthScreen(true);
                        yearlyScreen.displayYearScreen(true);
                        customScreen.displayCustomScreen(true);
                        fullReportScreen.displayFullReportScreen(true);

                        //based on which page is being displayed, display it again with new cash flow added
                        switch(currentPage)
                        {
                            case REPORT_BY_MONTH -> {
                                contentDisplay.getChildren().clear();
                                contentDisplay.getChildren().addAll(monthScreen);
                                this.statsBoxBox.getChildren().clear();
                                monthScreen.loadStatsBox();


                                break;
                            }
                            case REPORT_BY_YEAR ->  {
                                contentDisplay.getChildren().clear();
                                contentDisplay.getChildren().addAll(yearlyScreen);
                                this.statsBoxBox.getChildren().clear();
                                yearlyScreen.loadStatsBox();

                                break;
                            }
                            case CUSTOM_REPORT -> {
                                contentDisplay.getChildren().clear();
                                contentDisplay.getChildren().addAll(customScreen);
                                this.statsBoxBox.getChildren().clear();
                                customScreen.loadStatsBox();

                                break;
                            }
                            case FULL_REPORT -> {
                                contentDisplay.getChildren().clear();
                                contentDisplay.getChildren().addAll(fullReportScreen);
                                this.statsBoxBox.getChildren().clear();
                                fullReportScreen.loadStatsBox();

                                break;
                            }
                            case TITLE_SCREEN -> {
                                statsBoxBox.getChildren().clear();
                                break;
                        }
                        }
                    }

                    break;

                case "Remove Checked":
                    ArrayList<CashFlowAndCheckbox> cashFlowAndCheckboxes;

                    switch(currentPage)
                    {
                        case REPORT_BY_MONTH -> {
                            cashFlowAndCheckboxes = monthScreen.getCashFlowAndCheckboxes();

                            for(CashFlowAndCheckbox current : cashFlowAndCheckboxes)
                                if(current.getCheckBox().isSelected()) {
                                    removeCashFlowFromFile(current.getCashFlow());
                                }

                            break;
                        }
                        case REPORT_BY_YEAR ->  {

                            break;
                        }
                        case CUSTOM_REPORT -> {
                            cashFlowAndCheckboxes = customScreen.getCashFlowAndCheckboxes();

                            for(CashFlowAndCheckbox current : cashFlowAndCheckboxes)
                                if(current.getCheckBox().isSelected()) {
                                    removeCashFlowFromFile(current.getCashFlow());
                                }
                            break;
                        }
                        case FULL_REPORT -> {


                            break;
                        }
                    }

                    //reload each type of display to include new cash flow
                    monthScreen.displayMonthScreen(true);
                    yearlyScreen.displayYearScreen(true);
                    customScreen.displayCustomScreen(true);
                    fullReportScreen.displayFullReportScreen(true);

                    switch(currentPage)
                    {
                        case REPORT_BY_MONTH -> {
                            contentDisplay.getChildren().clear();
                            contentDisplay.getChildren().addAll(monthScreen);
                            this.statsBoxBox.getChildren().clear();
                            monthScreen.loadStatsBox();

                            break;
                        }
                        case REPORT_BY_YEAR ->  {
                            contentDisplay.getChildren().clear();
                            contentDisplay.getChildren().addAll(yearlyScreen);
                            this.statsBoxBox.getChildren().clear();
                            yearlyScreen.loadStatsBox();

                            break;
                        }
                        case CUSTOM_REPORT -> {
                            contentDisplay.getChildren().clear();
                            contentDisplay.getChildren().addAll(customScreen);
                            this.statsBoxBox.getChildren().clear();
                            customScreen.loadStatsBox();

                            break;
                        }
                        case FULL_REPORT -> {
                            contentDisplay.getChildren().clear();
                            contentDisplay.getChildren().addAll(fullReportScreen);
                            this.statsBoxBox.getChildren().clear();
                            fullReportScreen.loadStatsBox();

                            break;
                        }
                    }

                    break;
                default:
                    break;
            }
        }
    }

    private void placeCashFlowInFile(CashFlow newCashFlow)
    {
        //if master files' earliest date and most recent date are not set set them to new cash flow (for first cashflow added)
        if(masterFileData.getEarliestDate() == null && masterFileData.getMostRecentDate() == null)
        {
            masterFileData.setEarliestDate(newCashFlow.getLocalDate());
            masterFileData.setMostRecentDate(newCashFlow.getLocalDate());
            overWriteMasterFile();
        }
        // if new cash flow date is earlier than replace earliest date in master file
        else if(newCashFlow.getLocalDate().isBefore(masterFileData.getEarliestDate()))
        {
            masterFileData.setEarliestDate(newCashFlow.getLocalDate());
            overWriteMasterFile();
        }
        else if(newCashFlow.getLocalDate().isAfter(masterFileData.getMostRecentDate()))
        {
            masterFileData.setMostRecentDate(newCashFlow.getLocalDate());
            overWriteMasterFile();
        }
        //filename month file naming convention
        placeCashFlow(newCashFlow);
    }

    private void removeCashFlowFromFile(CashFlow cashFlow)
    {
        String filePath  = MasterFileData.getSaveDirectory() + "_" +cashFlow.getLocalDate().getYear() + File.separator +
                           "_" + cashFlow.getLocalDate().getMonth().toString() + MasterFileData.CASH_FLOW_EXTENSION;
        File file = new File(filePath);
        ArrayList<CashFlow> cashFlows = new ArrayList<>();

        if(file.exists()) {
            int numCashFlowsInFile;
            boolean isEarliestOnFile = false, isMostRecentOnFile = false,cashFlowRemoved = false;

            //check that this flow is both/or earliest and latest date on file
            if(masterFileData.getEarliestDate().equals(cashFlow.getLocalDate())) {
                isEarliestOnFile = true;
            }
            if(masterFileData.getMostRecentDate().equals(cashFlow.getLocalDate())) {

                isMostRecentOnFile = true;
            }
            //store all cash flows from file except the one to be removed
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                numCashFlowsInFile = ois.readInt();

                // Read the serialized objects
                //Add all cash flows to array except the one we want removed
                CashFlow currentFromFile;

                //the following loop places all cash flows in arrayList cashFlows except the current cash flow to be removed (cashFlow).
                for (int i = 0; i < numCashFlowsInFile; i++)
                {
                    currentFromFile = (CashFlow) ois.readObject();
                    if (!currentFromFile.equals(cashFlow) || cashFlowRemoved)
                    {
                        cashFlows.add(currentFromFile);
                    }
                    else
                        cashFlowRemoved = true;
                }

                //store new master file earliest and most recent dates
                if(cashFlows.isEmpty())
                {   if(isEarliestOnFile && isMostRecentOnFile)
                    {
                        masterFileData.setMostRecentDate(null);
                        masterFileData.setEarliestDate(null);
                    }
                    else
                    {
                        if(isEarliestOnFile)
                            masterFileData.setEarliestDate(findNextMostEarliestDate(cashFlow.getLocalDate()));
                        else if(isMostRecentOnFile)
                            masterFileData.setMostRecentDate(findNextMostRecentDate(cashFlow.getLocalDate()));
                    }
                    //find next earliest date, and next most recent date from other files

                }
                else {
                    if (isMostRecentOnFile)
                        masterFileData.setMostRecentDate(cashFlows.getLast().getLocalDate());
                    if (isEarliestOnFile)
                        masterFileData.setEarliestDate(cashFlows.getFirst().getLocalDate());
                }

            } catch (IOException | ClassNotFoundException e) {
                AlertBox.display("Error reading from file", e.getMessage(),fontName,fontSize);
            }

            if(isEarliestOnFile || isMostRecentOnFile)
                overWriteMasterFile();

            //if array is empty delete the file because no cash flows are left for file
            if (cashFlows.isEmpty()) {
                Path path = Paths.get(filePath);
                try {
                    Files.delete(path);

                } catch (IOException e) {
                    AlertBox.display("Failed to delete file.", e.getMessage(), fontName, fontSize);
                }
                //check to see if any other files exist in the year directory if not delete directory
                int countOfFiles = 0;
                LocalDate current = LocalDate.of(cashFlow.getLocalDate().getYear(), 1, 1);
                while (current.isBefore(LocalDate.of(cashFlow.getLocalDate().getYear() + 1, 1, 1))) {
                    String monthFilePath = MasterFileData.getSaveDirectory() + "_" + current.getYear() + File.separator +
                            "_" + current.getMonth().toString() + MasterFileData.CASH_FLOW_EXTENSION;
                    File monthFile = new File(monthFilePath);
                    if (monthFile.exists())
                        countOfFiles++;

                    current = current.plusMonths(1);
                }

                if (countOfFiles == 0) {
                    String yearDirPath = MasterFileData.getSaveDirectory() + "_" + cashFlow.getLocalDate().getYear();
                    path = Paths.get(yearDirPath);
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        AlertBox.display("Failed to delete directory.", e.getMessage(), fontName, fontSize);
                    }
                }
            }
            //otherwise overwrite all cash flows to file in array
            else
            {
                try (FileOutputStream fos = new FileOutputStream(file);
                     ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                    // Write total number of cash flows to be in file
                    oos.writeInt(cashFlows.size());

                    // Write the serialized objects

                    for (CashFlow currentToFile : cashFlows) {
                        oos.writeObject(currentToFile);
                    }

                } catch (IOException e) {
                    AlertBox.display("Write first cash flow to file.", "An error occurred while writing to the file: " + e.getMessage(), fontName, fontSize);
                }
            }
        }
    }

    //Searches for next earliest date on file outside of month file that checkDate is stored in
    private LocalDate findNextMostEarliestDate(LocalDate checkDate)
    {
        while(!checkDate.isAfter(masterFileData.getMostRecentDate()))
        {
            checkDate = checkDate.plusMonths(1);
            String nextFilePath = MasterFileData.getSaveDirectory() + "_" +checkDate.getYear() + File.separator +
                    "_" + checkDate.getMonth().toString() + MasterFileData.CASH_FLOW_EXTENSION;
            File nextMonthFile = new File(nextFilePath);

            if(nextMonthFile.exists())
            {
                int numCashFlowsInFile;
                CashFlow nextEarliestFlow;
                try (FileInputStream fis = new FileInputStream(nextMonthFile);
                     ObjectInputStream ois = new ObjectInputStream(fis))
                {

                    numCashFlowsInFile = ois.readInt();
                    nextEarliestFlow = (CashFlow) ois.readObject();
                    return nextEarliestFlow.getLocalDate();

                } catch (IOException | ClassNotFoundException e) {
                    AlertBox.display("Error occured when reading from file", e.getMessage(),fontName,fontSize);
                }
            }
        }
        return checkDate;
    }

    private LocalDate findNextMostRecentDate(LocalDate checkDate)
    {
        while(!checkDate.isBefore(masterFileData.getEarliestDate()))
        {
            checkDate = checkDate.minusMonths(1);
            String nextFilePath = MasterFileData.getSaveDirectory() + "_" +checkDate.getYear() + File.separator +
                    "_" + checkDate.getMonth().toString() + MasterFileData.CASH_FLOW_EXTENSION;
            File nextMonthFile = new File(nextFilePath);

            if(nextMonthFile.exists())
            {
                int numCashFlowsInFile;
                CashFlow nextMostRecentFlow = new CashFlow();
                try (FileInputStream fis = new FileInputStream(nextMonthFile);
                     ObjectInputStream ois = new ObjectInputStream(fis))
                {

                    numCashFlowsInFile = ois.readInt();
                    for(int i = 0; i < numCashFlowsInFile; i++)
                    {
                        nextMostRecentFlow = (CashFlow) ois.readObject();
                    }

                    return nextMostRecentFlow.getLocalDate();

                } catch (IOException | ClassNotFoundException e) {
                    AlertBox.display("Error occured when reading form file", e.getMessage(),fontName,fontSize);
                }
            }
        }
        return checkDate;
    }

    //overwrites the master file with new data
    private void overWriteMasterFile()
    {
        try (FileOutputStream fos = new FileOutputStream(masterFileData.getSavePath());
             ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(masterFileData);
        } catch (IOException e) {
            AlertBox.display("Overwrite master file","An error occurred while serializing the object: " + e.getMessage(),fontName,fontSize);
        }
    }

    //places cash flow in fileName file creating the file if necessary
    //File contains an integer object followed by that number of CashFlow objects
    private void placeCashFlow(CashFlow newCashFlow)
    {
        String yearDirectoryName = MasterFileData.getSaveDirectory() + "_" +newCashFlow.getLocalDate().getYear() + File.separator;
        File dir = new File(yearDirectoryName);
        if (!dir.exists()) //if save direcory doesnt exist create it and put a new master file into it
        {
            try {
                if (dir.mkdirs());// Create the directory if it doesn't exist

            } catch (SecurityException exception) {
                AlertBox.display("Make Directory Error", exception.getMessage(), fontName, fontSize);
            }
        }

        File file = new File(yearDirectoryName + "_" + newCashFlow.getLocalDate().getMonth().toString()+ MasterFileData.CASH_FLOW_EXTENSION);

        //if file exists place cash flow in appropriate spot in file ascending by date
        if (file.exists())
        {
            int numCashFlowsInFile;
            ArrayList<CashFlow> cashFlowsInMonthYearFile = new ArrayList<>();

            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                // Read the integer
                numCashFlowsInFile = ois.readInt();

                // Read the serialized objects
                CashFlow currentFromFile;
                boolean placed = false;

                //the following loop reads cash flows from searialized file and places them in array, while doing this
                //it places the new cash flow in the correct spot in the array based on the date of the cash flows
                for(int i = 0; i < numCashFlowsInFile; i++)
                {
                    currentFromFile = (CashFlow)ois.readObject();
                    //if cash flow to add is before current cashflow from file, add new cash flow to array and then add current cash flow from file to array
                    if(newCashFlow.getLocalDate().isBefore(currentFromFile.getLocalDate()) && !placed)
                    {
                        cashFlowsInMonthYearFile.add(newCashFlow);
                        cashFlowsInMonthYearFile.add(currentFromFile);
                        placed = true;
                    }
                    //if this is the last cash flow in the array and cash flow has not been placed, it is a more recent cash flow, therefore
                    //place it at end of array after last cash flow
                    else if(i == numCashFlowsInFile - 1 && !placed)
                    {
                        cashFlowsInMonthYearFile.add(currentFromFile);
                        cashFlowsInMonthYearFile.add(newCashFlow);
                    }
                    else
                        cashFlowsInMonthYearFile.add(currentFromFile);
                }
            } catch (IOException | ClassNotFoundException e) {
                AlertBox.display("Error occured when reading from file", e.getMessage(),fontName,fontSize);
            }

            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                // Write total number of cash flows to be in file
                oos.writeInt(cashFlowsInMonthYearFile.size());

                // Write the serialized objects

                for(CashFlow currentToFile : cashFlowsInMonthYearFile)
                {
                    oos.writeObject(currentToFile);
                }

            } catch (IOException e) {
                AlertBox.display("Write first cash flow to file." , "An error occurred while writing to the file: " + e.getMessage(),fontName,fontSize);
            }
        }
        else //create new file place cash flow in it
        {
            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                // Write the integer
                oos.writeInt(1);

                // Write the serialized objects
                oos.writeObject(newCashFlow);

            } catch (IOException e) {
                AlertBox.display("Write first cash flow to file." , "An error occurred while writing to the file: " + e.getMessage(),fontName,fontSize);
            }
        }
    }
}

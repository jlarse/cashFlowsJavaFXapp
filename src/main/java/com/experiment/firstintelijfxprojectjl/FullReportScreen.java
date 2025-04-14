/**
 * FullReportScreen.java         Eclipse IDE

 * @author Justin Larsen
 * 7/23/2024
 *
 * Description: Extends Border pane to view a full report of cash flows. Displaying the cash flow information
 * for each year.
 */

package com.experiment.firstintelijfxprojectjl;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.control.Label;


import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.io.*;

public class FullReportScreen extends BorderPane
{

    private String fontName;
    private int fontSize;
    private ScrollPane fullReportContent;
    private HBox topBox;
    private VBox fullReportViewBox;
    private GridPane yearBox;
    private MasterFileData masterFileData;
    private boolean firstTimeLoaded;
    private HBox statsBoxBox;
    private StatsBox statsBox;

    public FullReportScreen(MasterFileData masterFileData,HBox statsBoxBox, String fontName, int fontSize) {
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.masterFileData = masterFileData;
        this.topBox = new HBox(fontSize / 3);
        this.fullReportViewBox = new VBox();
        this.fullReportContent = new ScrollPane();
        this.yearBox = new GridPane();
        fullReportContent.setStyle("-fx-background-color: transparent;");
        firstTimeLoaded = true;
        this.statsBoxBox = statsBoxBox;
        statsBox = new StatsBox(fontSize,fontName);
        fullReportViewBox.setAlignment(Pos.CENTER);

        ColumnConstraints spacerLeft = new ColumnConstraints();
        spacerLeft.setPrefWidth(fontSize * 1);  // Set preferred width for column 1
        spacerLeft.setHgrow(Priority.ALWAYS);

        //Setup collumns with predifined width
        ColumnConstraints dateCollumn = new ColumnConstraints();
        dateCollumn.setPrefWidth(fontSize * 10.5);  // Set preferred width for column 1
        dateCollumn.setHgrow(Priority.ALWAYS);  // Allow column to grow

        ColumnConstraints categoryCollumn = new ColumnConstraints();
        categoryCollumn.setPrefWidth(fontSize * 10.5);  // Set preferred width for column 1
        categoryCollumn.setHgrow(Priority.ALWAYS);  // Allow column to grow


        ColumnConstraints amountCollumn = new ColumnConstraints();
        amountCollumn.setPrefWidth(fontSize * 10.5);  // Set preferred width for column 1
        amountCollumn.setHgrow(Priority.ALWAYS);  // Allow column to grow

        //Setup collumns with predifined width
        ColumnConstraints collumn5 = new ColumnConstraints();
        collumn5.setPrefWidth(fontSize * 10.5);  // Set preferred width for column 1
        collumn5.setHgrow(Priority.ALWAYS);  // Allow column to grow

        ColumnConstraints collumn6 = new ColumnConstraints();
        collumn6.setPrefWidth(fontSize * 10.5);  // Set preferred width for column 1
        collumn6.setHgrow(Priority.ALWAYS);  // Allow column to grow

        ColumnConstraints collumn7 = new ColumnConstraints();
        collumn7.setPrefWidth(fontSize * 6);  // Set preferred width for column 1
        collumn7.setHgrow(Priority.ALWAYS);  // Allow column to grow

        yearBox.getColumnConstraints().addAll(spacerLeft,dateCollumn, categoryCollumn, amountCollumn,collumn5,collumn6,collumn7);

        this.setTop(topBox);
        this.setCenter(fullReportContent);
    }

    public void displayFullReportScreen(Boolean needToLoadContent) {
        this.topBox.getChildren().clear();
        topBox.setAlignment(Pos.CENTER);
        fullReportContent.setFitToHeight(true);
        fullReportContent.setFitToWidth(true);

        if(needToLoadContent || firstTimeLoaded)//work on this-<<
        {
            loadFullReportContent();
            firstTimeLoaded = false;
            statsBoxBox.getChildren().clear();
            statsBox.loadStatsBox();
            statsBoxBox.getChildren().addAll(statsBox);
        }
    }

    public void loadStatsBox()
    {
        statsBoxBox.getChildren().clear();
        statsBox.loadStatsBox();
        statsBoxBox.getChildren().addAll(statsBox);
    }

    private void loadFullReportContent() {
        fullReportViewBox.getChildren().clear();
        yearBox.getChildren().clear();
        fullReportContent.setContent(fullReportViewBox);
        statsBox.setAllAccumulationToZero();

        if(masterFileData.getMostRecentDate() != null && masterFileData.getEarliestDate() != null)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            Label topBoxTitle = new Label("This is a full report of all Cash Flows on record ranging from the first on " + masterFileData.getEarliestDate().format(formatter) +
                    " to the last on " + masterFileData.getMostRecentDate().format(formatter) + ".\n\n");
            topBoxTitle.setFont(Font.font(fontName, fontSize * 1.2));
            fullReportViewBox.getChildren().add(topBoxTitle);

            int row = 0;
            for (int currentYear = masterFileData.getEarliestDate().getYear(); currentYear <= masterFileData.getMostRecentDate().getYear(); currentYear++) {
                String yearDirPath = MasterFileData.SAVE_DIRECTORY + File.separator + "_" + Integer.toString(currentYear);
                File yearDir = new File(yearDirPath);

                if (yearDir.exists()) {
                    row++;
                    double yearInflow = 0, yearOutflow = 0, yearGainOrLoss = 0;

                    //Add column titles grid pane
                    Label yearTitle = new Label("Year");
                    yearTitle.setFont(Font.font(fontName, fontSize * 1.2));
                    Label inflowTitle = new Label("Inflow");
                    inflowTitle.setFont(Font.font(fontName, fontSize * 1.2));
                    Label outFlowTitle = new Label("Outflow");
                    outFlowTitle.setFont(Font.font(fontName, fontSize * 1.2));
                    Label gainOrLossTitle = new Label("Gain/Loss");
                    gainOrLossTitle.setFont(Font.font(fontName, fontSize * 1.2));

                    yearBox.add(yearTitle, 1, 0);
                    yearBox.add(inflowTitle, 2, 0);
                    yearBox.add(outFlowTitle, 3, 0);
                    yearBox.add(gainOrLossTitle, 4, 0);

                    LocalDate currentDate = LocalDate.of(currentYear, 1, 1);
                    while (currentDate.isBefore(LocalDate.of(currentYear + 1, 1, 1))) {
                        String monthFilePath = yearDirPath + File.separator + "_" + currentDate.getMonth() + MasterFileData.CASH_FLOW_EXTENSION;
                        File monthFile = new File(monthFilePath);

                        if (monthFile.exists()) {
                            int numCashFlowsInFile;
                            try (FileInputStream fis = new FileInputStream(monthFile);
                                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                                // Read the integer
                                numCashFlowsInFile = ois.readInt();

                                // Read the serialized objects
                                CashFlow currentFromFile;
                                boolean placed = false;
                                for (int i = 0; i < numCashFlowsInFile; i++) {
                                    currentFromFile = (CashFlow) ois.readObject();

                                    //build overall data
                                    statsBox.addAccumulation(currentFromFile);

                                    //build year data
                                    if (currentFromFile.getAmount() < 0)
                                        yearOutflow += currentFromFile.getAmount();
                                    else
                                        yearInflow += currentFromFile.getAmount();

                                    yearGainOrLoss += currentFromFile.getAmount();

                                }
                            } catch (IOException | ClassNotFoundException e) {
                                AlertBox.display("Error occured when reading from file", e.getMessage(),fontName,fontSize);
                            }
                        }
                        currentDate = currentDate.plusMonths(1); //iterate forward one month
                    }

                    Label yearLabel = new Label(Integer.toString(currentYear));
                    yearLabel.setFont(Font.font(fontName, fontSize * 1.2));
                    Label inflowLabel = new Label();
                    if (yearInflow == 0) {
                        inflowLabel.setText("  $" + String.format("%.2f", yearInflow));
                        inflowLabel.setStyle("-fx-text-fill: black;");
                    } else {
                        inflowLabel.setText(" +$" + String.format("%.2f", yearInflow));
                        inflowLabel.setStyle("-fx-text-fill: green;");
                    }
                    inflowLabel.setFont(Font.font(fontName, fontSize * 1.2));

                    Label outflowLabel = new Label();
                    if (yearOutflow == 0) {
                        outflowLabel.setText("  $" + String.format("%.2f", yearOutflow));
                        outflowLabel.setStyle("-fx-text-fill: black;");
                    } else {
                        outflowLabel.setText(" -$" + String.format("%.2f", -1 * yearOutflow));
                        outflowLabel.setStyle("-fx-text-fill: red;");
                    }
                    outflowLabel.setFont(Font.font(fontName, fontSize * 1.2));

                    Label monthGainOrLossLabel = new Label();

                    if (yearGainOrLoss == 0) {
                        monthGainOrLossLabel.setText("$0.00");
                        monthGainOrLossLabel.setStyle("-fx-text-fill: black;");
                    } else if (yearGainOrLoss < 0) {
                        monthGainOrLossLabel.setText("-$" + String.format("%.2f", -1 * yearGainOrLoss));
                        monthGainOrLossLabel.setStyle("-fx-text-fill: red;");
                    } else {
                        monthGainOrLossLabel.setText("+$" + String.format("%.2f", yearGainOrLoss));
                        monthGainOrLossLabel.setStyle("-fx-text-fill: green;");
                    }
                    monthGainOrLossLabel.setFont(Font.font(fontName, fontSize * 1.2));

                    yearBox.add(yearLabel, 1, row);
                    yearBox.add(inflowLabel, 2, row);
                    yearBox.add(outflowLabel, 3, row);
                    yearBox.add(monthGainOrLossLabel, 4, row);
                }
            }
            fullReportViewBox.getChildren().add(yearBox);
        }
        else
        {
            Label noneToViewLabel = new Label("No cash flows stored. Please add a cash flow.");
            noneToViewLabel.setFont(Font.font(fontName,fontSize));
            noneToViewLabel.setAlignment(Pos.CENTER);
            fullReportViewBox.setAlignment(Pos.CENTER);
            fullReportViewBox.getChildren().clear();
            fullReportViewBox.getChildren().add(noneToViewLabel);
            fullReportContent.setContent(fullReportViewBox);


        }

    }

}
package com.experiment.firstintelijfxprojectjl;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.control.ScrollPane;

import javafx.scene.control.Button;

import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;

public class YearlyScreen extends BorderPane {

    private String fontName;
    private int fontSize;
    private ScrollPane yearContent;
    private HBox topMenu;
    private VBox yearViewBox;
    private GridPane monthsList;
    private MasterFileData masterFileData;
    private int yearSelected;
    private HBox statsBoxBox;
    private StatsBox statsBox;

    public YearlyScreen(MasterFileData masterFileData,HBox statsBoxBox, String fontName, int fontSize) {
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.masterFileData = masterFileData;
        this.topMenu = new HBox(fontSize / 3);
        this.yearViewBox = new VBox();
        this.yearContent = new ScrollPane();
        this.monthsList = new GridPane();
        this.statsBoxBox = statsBoxBox;
        this.statsBox = new StatsBox(fontSize,fontName);

        yearContent.setStyle("-fx-background-color: transparent;");

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPrefWidth(fontSize * 1);  // Set preferred width for column 1
        column1.setHgrow(Priority.ALWAYS);

        //Setup collumns with predifined width
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPrefWidth(fontSize * 10.5);  // Set preferred width for column 1
        column2.setHgrow(Priority.ALWAYS);  // Allow column to grow

        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPrefWidth(fontSize * 10.5);  // Set preferred width for column 1
        column3.setHgrow(Priority.ALWAYS);  // Allow column to grow


        ColumnConstraints column4 = new ColumnConstraints();
        column4.setPrefWidth(fontSize * 10.5);  // Set preferred width for column 1
        column4.setHgrow(Priority.ALWAYS);  // Allow column to grow

        //Setup collumns with predifined width
        ColumnConstraints collumn5 = new ColumnConstraints();
        collumn5.setPrefWidth(fontSize * 10.5);  // Set preferred width for column 1
        collumn5.setHgrow(Priority.ALWAYS);  // Allow column to grow

        ColumnConstraints collumn6 = new ColumnConstraints();
        collumn6.setPrefWidth(fontSize * 10.5);  // Set preferred width for column 1
        collumn6.setHgrow(Priority.ALWAYS);  // Allow column to grow

        ColumnConstraints collumn7 = new ColumnConstraints();
        collumn7.setPrefWidth(fontSize * 6);  // Set preferred width for column 1
        collumn7.setHgrow(Priority.ALWAYS);

        monthsList.getColumnConstraints().addAll(column1,column2,column3,column4,collumn5,collumn6,collumn7);
        yearViewBox.setAlignment(Pos.CENTER);
        this.yearSelected = 0;

        this.setTop(topMenu);
        this.setCenter(yearContent);
    }

    public void displayYearScreen(Boolean needToLoadContent) {
        this.topMenu.getChildren().clear();
        topMenu.setAlignment(Pos.CENTER);
        displayMenuLayout();
        yearContent.setFitToHeight(true);
        yearContent.setFitToWidth(true);

        //if no year has been selected don't display year content, as it will display an error message
        if(yearSelected != 0 && needToLoadContent) {
            loadYearlyContent();
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

    private void loadYearlyContent() {
        yearViewBox.getChildren().clear();
        monthsList.getChildren().clear();
        yearContent.setContent(yearViewBox);
        statsBox.setAllAccumulationToZero();

        double monthInFlow = 0, monthOutFlow = 0, monthGainOrLoss = 0;
        String yearDirPath = MasterFileData.SAVE_DIRECTORY + File.separator + "_" + Integer.toString(yearSelected);
        File dir = new File(yearDirPath);

        if (dir.exists())
        {

            Label yearTitle = new Label(Integer.toString(yearSelected));
            yearTitle.setFont(Font.font(fontName,fontSize * 2));
            yearTitle.setAlignment(Pos.CENTER);
            yearViewBox.getChildren().addAll(yearTitle,monthsList);

            //Add column titles grid pane
            Label monthTitle = new Label("Month");
            monthTitle.setFont(Font.font(fontName,fontSize * 1.2));
            Label inflowTitle = new Label ("Inflow");
            inflowTitle.setFont(Font.font(fontName,fontSize * 1.2));
            Label outFlowTitle = new Label("Outflow");
            outFlowTitle.setFont(Font.font(fontName,fontSize * 1.2));
            Label gainOrLossTitle = new Label("Gain/Loss");
            gainOrLossTitle.setFont(Font.font(fontName,fontSize * 1.2));

            monthsList.add(monthTitle, 1, 0);
            monthsList.add(inflowTitle, 2, 0);
            monthsList.add(outFlowTitle, 3, 0);
            monthsList.add(gainOrLossTitle,4,0);

            LocalDate current = LocalDate.of(yearSelected, 1, 1);
            int row = 0;
            Label monthLabel = new Label();
            while (current.isBefore(LocalDate.of(yearSelected + 1, 1, 1)))
            {
                String monthFilePath = yearDirPath + File.separator + "_" + current.getMonth() + MasterFileData.CASH_FLOW_EXTENSION;
                File monthFile = new File(monthFilePath);

                if (monthFile.exists()) {
                    row++;
                    monthInFlow = 0;
                    monthOutFlow = 0;
                    monthGainOrLoss = 0;

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
                            if (currentFromFile.getAmount() < 0) {
                                monthOutFlow += currentFromFile.getAmount();
                            } else {
                                monthInFlow += currentFromFile.getAmount();
                            }
                            monthGainOrLoss += currentFromFile.getAmount();
                            statsBox.addAccumulation(currentFromFile);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        AlertBox.display("Error occured when reading from file", e.getMessage(), fontName,fontSize);
                    }
                    monthLabel = new Label(convertToLowerCaseExceptFirst(current.getMonth().toString()));
                    monthLabel.setFont(Font.font(fontName, fontSize * 1.2));

                    Label inflowLabel = new Label();
                    if (monthInFlow == 0) {
                        inflowLabel.setText("  $" + String.format("%.2f",monthInFlow));
                        inflowLabel.setStyle("-fx-text-fill: black;");
                    } else
                    {
                        inflowLabel.setText(" +$" +  String.format("%.2f",monthInFlow));
                        inflowLabel.setStyle("-fx-text-fill: green;");
                    }
                    inflowLabel.setFont(Font.font(fontName, fontSize * 1.2));

                    Label outflowLabel = new Label();
                    if (monthOutFlow == 0) {
                        outflowLabel.setText("  $" +  String.format("%.2f",monthOutFlow));
                        outflowLabel.setStyle("-fx-text-fill: black;");
                    } else
                    {
                        outflowLabel.setText(" -$" +  String.format("%.2f",-1* monthOutFlow));
                        outflowLabel.setStyle("-fx-text-fill: red;");
                    }
                    outflowLabel.setFont(Font.font(fontName, fontSize * 1.2));

                    Label monthGainOrLossLabel = new Label();

                    if(monthGainOrLoss == 0) {
                        monthGainOrLossLabel.setText("$0.00");
                        monthGainOrLossLabel.setStyle("-fx-text-fill: black;");
                    }
                    else if(monthGainOrLoss < 0) {
                        monthGainOrLossLabel.setText("-$" +  String.format("%.2f",-1 * monthGainOrLoss));
                        monthGainOrLossLabel.setStyle("-fx-text-fill: red;");
                    }
                    else {
                        monthGainOrLossLabel.setText("+$" +  String.format("%.2f",monthGainOrLoss));
                        monthGainOrLossLabel.setStyle("-fx-text-fill: green;");
                    }
                    monthGainOrLossLabel.setFont(Font.font(fontName, fontSize * 1.2));

                    monthsList.add(monthLabel, 1, row);
                    monthsList.add(inflowLabel, 2, row);
                    monthsList.add(outflowLabel, 3, row);
                    monthsList.add(monthGainOrLossLabel,4,row);
                }
                current = current.plusMonths(1);
            }
            statsBox.loadStatsBox();
        }
        else
        {
            Label error = new Label("No year directory found: " + yearDirPath);
            error.setFont(Font.font(fontName, fontSize));
            error.setAlignment(Pos.CENTER);
            yearViewBox.getChildren().add(error);
        }
    }


    private void displayMenuLayout()
    {
        //if a cash flow has been added to the system make the ComboBox Selectors for it
        if(masterFileData.getMostRecentDate() != null && masterFileData.getEarliestDate() != null)
        {
            //componenents for meu
            Label selectYearTitle = new Label("Select the year you wish to view: ");
            selectYearTitle.setFont(Font.font(fontName,fontSize));
            ComboBox<String> yearSelector = new ComboBox<>();
            yearSelector.setStyle("-fx-font-family: '" + fontName + "'; -fx-font-size: " + fontSize + ";");
            Button selectButton = new Button("Select");
            selectButton.setFont(Font.font(fontName,fontSize));


            //set the first combo box to the value of years that are stored
            ArrayList<Integer> yearsToSelect = new ArrayList<Integer>();
            LocalDate current = masterFileData.getEarliestDate();
            while (!current.isAfter(masterFileData.getMostRecentDate())) {
                String dirName = "_" + current.getYear() + File.separator;
                File dir = new File(MasterFileData.getSaveDirectory() + dirName);
                if(dir.exists()) {
                    yearSelector.getItems().add(String.valueOf(current.getYear()));
                    yearsToSelect.add(current.getYear());
                }
                current = current.plusYears(1);
            }
            if(yearSelected != 0)
                yearSelector.setValue(Integer.toString(yearSelected));

            //add all components to the menu
            this.topMenu.getChildren().addAll(selectYearTitle,yearSelector,selectButton);

            //when submit buton is pressed load the cash flow data
            selectButton.setOnAction(e -> {
                yearSelected = Integer.parseInt(yearSelector.getValue());

                loadYearlyContent();
                statsBoxBox.getChildren().clear();
                statsBox.loadStatsBox();
                statsBoxBox.getChildren().addAll(statsBox);
            });
        }
        else
        {
            Label noneToViewLabel = new Label("No cash flows stored. Please add a cash flow.");
            noneToViewLabel.setFont(Font.font(fontName,fontSize));
            noneToViewLabel.setAlignment(Pos.CENTER);
            yearViewBox.getChildren().clear();
            yearContent.setContent(yearViewBox);
            yearViewBox.getChildren().add(noneToViewLabel);
        }

    }

    //Converts string to lower case except first character
    private static String convertToLowerCaseExceptFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        // Get the first character
        char firstChar = str.charAt(0);
        // Get the rest of the string
        String restOfString = str.substring(1).toLowerCase();
        // Combine the first character with the rest of the string
        return firstChar + restOfString;
    }

}

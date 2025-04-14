package com.experiment.firstintelijfxprojectjl;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.control.Label;


import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.*;

public class CustomScreen extends BorderPane {

    private String fontName;
    private int fontSize;
    private ScrollPane customContent;
    private HBox topMenu;
    private VBox customViewBox;
    private GridPane allCashFlowsBox;
    private MasterFileData masterFileData;
    private LocalDate beginningDateSelected, endDateSelected;
    private boolean firstTimeSelected; // true indicates user has selected a custom date range
    private ArrayList<CashFlowAndCheckbox> cashFlowAndCheckboxes;
    private HBox statsBoxBox;
    private StatsBox statsBox;


    public CustomScreen(MasterFileData masterFileData,HBox statsBoxBox, String fontName, int fontSize) {
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.masterFileData = masterFileData;
        this.topMenu = new HBox(fontSize / 3);
        this.customViewBox = new VBox();
        this.customContent = new ScrollPane();
        this.allCashFlowsBox = new GridPane();
        customContent.setStyle("-fx-background-color: transparent;");
        this.firstTimeSelected = false;
        this.statsBoxBox = statsBoxBox;
        this.statsBox = new StatsBox(fontSize,fontName);
        customViewBox.setAlignment(Pos.CENTER);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPrefWidth(fontSize * 2);  // Set preferred width for column 1
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
        collumn5.setPrefWidth(fontSize * 31.5);  // Set preferred width for column 1
        collumn5.setHgrow(Priority.ALWAYS);  // Allow column to grow


        allCashFlowsBox.getColumnConstraints().addAll(column1,column2,column3,column4,collumn5);

        this.setTop(topMenu);
        this.setCenter(customContent);
    }

    public void displayCustomScreen(Boolean needToLoadContent) {
        this.topMenu.getChildren().clear();
        topMenu.setAlignment(Pos.CENTER);
        displayMenuLayout();
        customContent.setFitToHeight(true);
        customContent.setFitToWidth(true);

        //if user has selected a custom date range
        if(firstTimeSelected && needToLoadContent ) {
            loadCustomContent();
            statsBoxBox.getChildren().clear();
            statsBox.loadStatsBox();
            statsBoxBox.getChildren().addAll(statsBox);
        }
    }

    public void loadStatsBox() {
        statsBoxBox.getChildren().clear();
        statsBox.loadStatsBox();
        statsBoxBox.getChildren().addAll(statsBox);
    }

    private void loadCustomContent() {
        customViewBox.getChildren().clear();
        allCashFlowsBox.getChildren().clear();
        customContent.setContent(customViewBox);
        statsBox.setAllAccumulationToZero();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        cashFlowAndCheckboxes = new ArrayList<>();
        int cashFlowAndCheckboxesIndex = 0;


        if(masterFileData.getMostRecentDate() != null && masterFileData.getEarliestDate() != null)
        {
            Label topBoxTitle = new Label("Report of cash flows from " + beginningDateSelected.format(formatter) +
                    " to the last on " + endDateSelected.format(formatter) + ".\n\n");
            topBoxTitle.setFont(Font.font(fontName, fontSize * 1.2));
            customViewBox.getChildren().add(topBoxTitle);

            int row = 0;
            for (int currentYear = beginningDateSelected.getYear(); currentYear <= endDateSelected.getYear(); currentYear++) {
                String yearDirPath = MasterFileData.SAVE_DIRECTORY + File.separator + "_" + Integer.toString(currentYear);
                File yearDir = new File(yearDirPath);

                if (yearDir.exists()) {
                    double yearInflow = 0, yearOutflow = 0, yearGainOrLoss = 0;

                    //Add column titles grid pane
                    Label dateTitle = new Label("Date");
                    dateTitle.setFont(Font.font(fontName, fontSize * 1.2));
                    Label categoryTitle = new Label("Category");
                    categoryTitle.setFont(Font.font(fontName, fontSize * 1.2));
                    Label amountTitle = new Label("Amount");
                    amountTitle.setFont(Font.font(fontName, fontSize * 1.2));
                    Label descriptionTitle = new Label("Description");
                    descriptionTitle.setFont(Font.font(fontName, fontSize * 1.2));

                    allCashFlowsBox.add(dateTitle, 1, 0);
                    allCashFlowsBox.add(categoryTitle, 2, 0);
                    allCashFlowsBox.add(amountTitle, 3, 0);
                    allCashFlowsBox.add(descriptionTitle, 4, 0);

                    //set bounds for the beginning month selected and end month selected if they are in the current year file
                    int endMonth = 12, beginningMonth = 1;
                    if (endDateSelected.getYear() == currentYear)
                        endMonth = endDateSelected.getMonthValue();
                    if (beginningDateSelected.getYear() == currentYear)
                        beginningMonth = beginningDateSelected.getMonthValue();

                    LocalDate currentDate = LocalDate.of(currentYear, beginningMonth, 1);
                    LocalDate endDate = LocalDate.of(currentYear, endMonth, 1);
                    endDate = endDate.plusMonths(1);

                    while (!currentDate.isAfter(endDate)) {
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

                                    //if current cash flow is between dates selected inclusive add the cash flow
                                    if (!currentFromFile.getLocalDate().isBefore(beginningDateSelected) && !currentFromFile.getLocalDate().isAfter(endDateSelected)) {
                                        row++;
                                        cashFlowAndCheckboxes.add(new CashFlowAndCheckbox(currentFromFile, fontSize));
                                        //build overall data
                                        statsBox.addAccumulation(currentFromFile);

                                        //build year data
                                        if (currentFromFile.getAmount() < 0)
                                            yearOutflow += currentFromFile.getAmount();
                                        else
                                            yearInflow += currentFromFile.getAmount();

                                        yearGainOrLoss += currentFromFile.getAmount();


                                        Label dateLabel = new Label(currentFromFile.getLocalDate().format(formatter));
                                        dateLabel.setFont(Font.font(fontName, fontSize * 1.2));

                                        Label categoryLabel = new Label(currentFromFile.getCategory());
                                        categoryLabel.setFont(Font.font(fontName, fontSize * 1.2));

                                        Label amountLabel = new Label();
                                        if (currentFromFile.getAmount() == 0) {
                                            amountLabel.setText("  $" + String.format("%.2f", currentFromFile.getAmount()));
                                            amountLabel.setStyle("-fx-text-fill: black;");
                                        } else if (currentFromFile.getAmount() > 0) {
                                            amountLabel.setText(" +$" + String.format("%.2f", currentFromFile.getAmount()));
                                            amountLabel.setStyle("-fx-text-fill: green;");
                                        } else {
                                            amountLabel.setText(" -$" + String.format("%.2f", -1 * currentFromFile.getAmount()));
                                            amountLabel.setStyle("-fx-text-fill: red;");
                                        }
                                        amountLabel.setFont(Font.font(fontName, fontSize * 1.2));

                                        Label descriptionLabel = new Label(currentFromFile.getDescription());
                                        descriptionLabel.setFont(Font.font(fontName, fontSize * 1.2));

                                        allCashFlowsBox.add(cashFlowAndCheckboxes.get(row - 1).getCheckBox(), 0, row);
                                        allCashFlowsBox.add(dateLabel, 1, row);
                                        allCashFlowsBox.add(categoryLabel, 2, row);
                                        allCashFlowsBox.add(amountLabel, 3, row);
                                        allCashFlowsBox.add(descriptionLabel, 4, row);

                                        cashFlowAndCheckboxesIndex++;
                                    }
                                }
                            } catch (IOException | ClassNotFoundException e) {
                                AlertBox.display("Error occured when reading from file", e.getMessage(),fontName,fontSize);
                            }
                        }
                        currentDate = currentDate.plusMonths(1); //iterate forward one month
                    }

                }
            }
            customViewBox.getChildren().add(allCashFlowsBox);
        }
        else
        {
            Label noneToViewLabel = new Label("No cash flows stored. Please add a cash flow.");
            noneToViewLabel.setFont(Font.font(fontName,fontSize));
            noneToViewLabel.setAlignment(Pos.CENTER);
            customContent.setContent(customViewBox);
            customViewBox.getChildren().add(noneToViewLabel);
        }
    }

    public ArrayList<CashFlowAndCheckbox> getCashFlowAndCheckboxes()
    {
        return cashFlowAndCheckboxes;
    }

    private void displayMenuLayout()
    {

        //if a cash flow has been added to the system make the ComboBox Selectors for it
        if(masterFileData.getMostRecentDate() != null && masterFileData.getEarliestDate() != null)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            //custom date picker menu
            Label selectCustomRangeTitle = new Label("Select a date range anywhere between " + masterFileData.getEarliestDate().format(formatter) + "\nto " +
                                                         masterFileData.getMostRecentDate().format(formatter)+ " to view custom report: ");
            selectCustomRangeTitle.setFont(Font.font(fontName,fontSize));

            //Beginning Date selector
            Label beginningDateTitle = new Label("    Beginning date:");
            beginningDateTitle.setFont(Font.font(fontName,fontSize));
            DatePicker beginningDatePicker = new DatePicker();
            beginningDatePicker.setStyle("-fx-font-family: '" + fontName + "'; -fx-font-size: " + fontSize + ";");
            beginningDatePicker.setPrefWidth(fontSize * 9);
            beginningDatePicker.setEditable(false); // Disable text input

            //End Date selector
            Label endDateTitle = new Label("End date:");
            endDateTitle.setFont(Font.font(fontName,fontSize));
            DatePicker endDatePicker = new DatePicker();
            endDatePicker.setStyle("-fx-font-family: '" + fontName + "'; -fx-font-size: " + fontSize + ";");
            endDatePicker.setPrefWidth(fontSize * 9);
            endDatePicker.setEditable(false); // Disable text input

            if(firstTimeSelected) {
                beginningDatePicker.setValue(beginningDateSelected);
                endDatePicker.setValue(endDateSelected);
            }


            Button selectButton = new Button("Select");
            selectButton.setFont(Font.font(fontName,fontSize));

            //add all components to the menu
            this.topMenu.getChildren().addAll(selectCustomRangeTitle,beginningDateTitle,beginningDatePicker,endDateTitle,endDatePicker,selectButton);

            //when submit button is pressed load the cash flow data
            selectButton.setOnAction(e -> {
                LocalDate beginningDateSelectedTemp = beginningDatePicker.getValue();
                LocalDate endDateSelectedTemp = endDatePicker.getValue();

                if(beginningDateSelectedTemp != null && endDateSelectedTemp != null)
                {
                    boolean datesAreValid = true;
                    if(beginningDateSelectedTemp.isBefore(masterFileData.getEarliestDate()) || endDateSelectedTemp.isAfter(masterFileData.getMostRecentDate()))
                    {
                        String message = "Dates selected for custom report are outside of range of stored cash flows." +
                                "Please\nselect a beginning date after " + masterFileData.getEarliestDate().format(formatter) +
                                " and an end date before " + masterFileData.getMostRecentDate().format(formatter) + ".";
                        AlertBox.display("Dates out of range", message,fontName,fontSize);
                        datesAreValid = false;
                    }
                    if(beginningDateSelectedTemp.isAfter(endDateSelectedTemp))
                    {
                        AlertBox.display("Beginning date after end date", "Beginning date must be before end date." ,fontName,fontSize);
                        datesAreValid = false;
                    }
                    if(datesAreValid)
                    {
                        firstTimeSelected = true;
                        beginningDateSelected = beginningDateSelectedTemp;
                        endDateSelected = endDateSelectedTemp;
                        loadCustomContent();
                        loadStatsBox();
                    }

                }
            });
        }
        else
        {
            Label noneToViewLabel = new Label("No cash flows stored. Please add a cash flow.");
            noneToViewLabel.setFont(Font.font(fontName,fontSize));
            noneToViewLabel.setAlignment(Pos.CENTER);
            customViewBox.getChildren().clear();
            customViewBox.setAlignment(Pos.CENTER);
            customContent.setContent(customViewBox);
            customViewBox.getChildren().add(noneToViewLabel);
        }

    }



}

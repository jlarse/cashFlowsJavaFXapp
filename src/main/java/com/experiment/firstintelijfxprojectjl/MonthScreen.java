package com.experiment.firstintelijfxprojectjl;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;

public class MonthScreen extends BorderPane{

    private String fontName;
    private int fontSize;
    private ScrollPane monthContent;
    private HBox topMenu;
    private VBox monthViewBox;
    private GridPane cashFlowsList;
    private MasterFileData masterFileData;
    private int yearSelected;
    private String monthSelected;
    private ArrayList<CashFlowAndCheckbox> cashFlowAndCheckboxes;
    private HBox statsBoxBox;
    private StatsBox statsBox;


    public MonthScreen(MasterFileData masterFileData,HBox statsBoxBox, String fontName, int fontSize)
    {
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.masterFileData = masterFileData;
        this.topMenu = new HBox(fontSize/3);
        this.monthViewBox = new VBox();
        this.monthContent = new ScrollPane();
        this.cashFlowsList= new GridPane();
        this.statsBoxBox = statsBoxBox;
        this.statsBox = new StatsBox(fontSize,fontName);

        monthContent.setStyle("-fx-background-color: transparent;");

        ColumnConstraints spacerLeft = new ColumnConstraints();
        spacerLeft.setPrefWidth(fontSize * 2);  // Set preferred width for column 1
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
        collumn5.setPrefWidth(fontSize * 31.5);  // Set preferred width for column 1
        collumn5.setHgrow(Priority.ALWAYS);  // Allow column to grow

        cashFlowsList.getColumnConstraints().addAll(spacerLeft,dateCollumn, categoryCollumn, amountCollumn,collumn5);

        monthViewBox.setAlignment(Pos.CENTER);

        this.yearSelected = 0;
        this.monthSelected = "";

        this.setTop(topMenu);
        this.setCenter(monthContent);
    }

    public void displayMonthScreen(Boolean needToRetrieveContent)
    {
        this.topMenu.getChildren().clear();
        topMenu.setAlignment(Pos.CENTER);
        displayMenuLayout();
        monthContent.setFitToHeight(true);
        monthContent.setFitToWidth(true);

        //if no month and year have been selected dont display mont content as it will show an error message
        if(yearSelected != 0 && monthSelected != "" && needToRetrieveContent) {
            loadMonthContent();
            statsBoxBox.getChildren().clear();
            statsBox.loadStatsBox();
            statsBoxBox.getChildren().addAll(statsBox);
        }
    }

    public void setNoneSelected()
    {
        monthSelected = "";
        yearSelected = 0;
    }

    public void loadStatsBox() {
        statsBoxBox.getChildren().clear();
        statsBox.loadStatsBox();
        statsBoxBox.getChildren().addAll(statsBox);
    }
    private void loadMonthContent()
    {
        monthViewBox.getChildren().clear();
        cashFlowsList.getChildren().clear();
        monthContent.setContent(monthViewBox);
        statsBox.setAllAccumulationToZero();


        String filePath = MasterFileData.SAVE_DIRECTORY + File.separator+ "_" +Integer.toString(yearSelected) + File.separator + "_" + monthSelected + MasterFileData.CASH_FLOW_EXTENSION;
        File file = new File(filePath);

        if(file.exists())
        {
            Label monthYearTitle = new Label(monthSelected + " " + yearSelected);
            monthYearTitle.setFont(Font.font(fontName,fontSize * 2));
            monthYearTitle.setAlignment(Pos.CENTER);
            monthViewBox.getChildren().add(monthYearTitle);
            cashFlowAndCheckboxes = new ArrayList<>();

            //Add column titles grid pane
            Label dateTitleLabel = new Label("Date");
            dateTitleLabel.setFont(Font.font(fontName,fontSize * 1.2));
            Label cateogoryTitleLabel = new Label ("Category");
            cateogoryTitleLabel.setFont(Font.font(fontName,fontSize * 1.2));
            Label amountTitleLabel = new Label("Amount");
            amountTitleLabel.setFont(Font.font(fontName,fontSize * 1.2));

            cashFlowsList.add(dateTitleLabel, 1, 0);
            cashFlowsList.add(cateogoryTitleLabel, 2, 0);
            cashFlowsList.add(amountTitleLabel, 3, 0);

            int numCashFlowsInFile;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                // Read the integer
                numCashFlowsInFile = ois.readInt();

                // Read the serialized objects
                CashFlow currentFromFile;
                boolean placed = false;
                for(int i = 0; i < numCashFlowsInFile; i++)
                {
                    currentFromFile = (CashFlow)ois.readObject();
                    cashFlowAndCheckboxes.add(new CashFlowAndCheckbox(currentFromFile,fontSize));

                    statsBox.addAccumulation(currentFromFile); //store flow in stats box records totals and accumulates designated category of flow

                    Label dateLabel = new Label(currentFromFile.getLocalDate().format(formatter));
                    dateLabel.setFont(Font.font(fontName,fontSize * 1.2));
                    Label cateogoryLabel = new Label(currentFromFile.getCategory());
                    cateogoryLabel.setFont(Font.font(fontName,fontSize * 1.2));
                    Label amountLabel = new Label();
                    amountLabel.setFont(Font.font(fontName,fontSize * 1.2));
                    Label descriptionLabel = new Label(currentFromFile.getDescription());
                    descriptionLabel.setFont(Font.font(fontName,fontSize * 1.2));

                    if(currentFromFile.getAmount() < 0)
                    {
                        amountLabel.setText("-$" + Double.toString(-1 * currentFromFile.getAmount()));
                        dateLabel.setStyle("-fx-text-fill: red;");
                        cateogoryLabel.setStyle("-fx-text-fill: red;");
                        amountLabel.setStyle("-fx-text-fill: red;");
                    }
                    else
                    {
                        amountLabel.setText("+$" + Double.toString(currentFromFile.getAmount()));
                        dateLabel.setStyle("-fx-text-fill: green;");
                        cateogoryLabel.setStyle("-fx-text-fill: green;");
                        amountLabel.setStyle("-fx-text-fill: green;");
                    }

                    cashFlowsList.add(cashFlowAndCheckboxes.get(i).getCheckBox(), 0, i + 1);
                    cashFlowsList.add(dateLabel, 1, i + 1);
                    cashFlowsList.add(cateogoryLabel, 2, i + 1);
                    cashFlowsList.add(amountLabel, 3, i + 1);
                    cashFlowsList.add(descriptionLabel, 4, i +1);


                }
                monthViewBox.getChildren().add(cashFlowsList);
                statsBox.loadStatsBox();
                monthViewBox.getChildren().add(statsBox);
            }
            catch (IOException | ClassNotFoundException e)
            {
                AlertBox.display("Error occured when reading from file", e.getMessage(),fontName,fontSize);
            }
        }
        else
        {
            Label noneToViewLabel = new Label();
            if(masterFileData.getEarliestDate() == null && masterFileData.getMostRecentDate() == null )
                noneToViewLabel.setText("No cash flows on record. Please add a cash flow.");
            else
                noneToViewLabel.setText("No cash cash flows stored for " + monthSelected + ". Please select another month.");
            noneToViewLabel.setFont(Font.font(fontName,fontSize));
            noneToViewLabel.setAlignment(Pos.CENTER);
            monthViewBox.getChildren().add(noneToViewLabel);
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
            //componenents for meu
            Label selectMonthYearTitle = new Label("Select the year, then the month you wish to view: ");
            selectMonthYearTitle.setFont(Font.font(fontName,fontSize));
            ComboBox<String> yearSelector = new ComboBox<>(), monthSelector = new ComboBox<>();
            yearSelector.setStyle("-fx-font-family: '" + fontName + "'; -fx-font-size: " + fontSize + ";");
            monthSelector.setStyle("-fx-font-family: '" + fontName + "'; -fx-font-size: " + fontSize + ";");
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

            //If user already chose a year, add month selector items and preselect that month
            if(yearSelected != 0) {
                yearSelector.setValue(Integer.toString(yearSelected));

                for(Integer year: yearsToSelect)
                {
                    if(year == yearSelected)
                    {
                        boolean firstSelected = true;
                        ArrayList<String> monthsAvailable = buildMonthListFromYear(year);
                        for(String cur: monthsAvailable) {
                            monthSelector.getItems().add(convertToLowerCaseExceptFirst(cur));
                        }
                        break;
                    }
                }
                monthSelector.setValue(monthSelected);
            }

            //Based on selection of first combo box set the  month values for the second combo box
            yearSelector.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    // Update the items in the second ComboBox based on the selection in the first ComboBox
                    ObservableList<String> items = FXCollections.observableArrayList();

                    ArrayList<String> monthsAvailable = buildMonthListFromYear(Integer.parseInt(newValue));
                    for(String current: monthsAvailable)
                       items.add(convertToLowerCaseExceptFirst(current));

                    // Set the items to the second ComboBox
                    monthSelector.setItems(items);
                    monthSelector.setValue(items.get(0)); //when year is selected automatically select 1st value on
                }
            });

            //add all components to the menu
            this.topMenu.getChildren().addAll(selectMonthYearTitle,yearSelector,monthSelector,selectButton);

            //when submit buton is pressed load the cash flow data
            selectButton.setOnAction(e -> {
                yearSelected = Integer.parseInt(yearSelector.getValue());
                monthSelected = monthSelector.getValue();

                loadMonthContent();
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
            monthViewBox.getChildren().clear();
            monthContent.setContent(monthViewBox);
            monthViewBox.getChildren().add(noneToViewLabel);
        }

    }

    //returns an array of strings that holds the months that have a cash flow file
    private ArrayList<String> buildMonthListFromYear(int yearSelected) {
        int count;
        ArrayList<String> monthsExistsFile = new ArrayList<>();
        String yearDirectory = MasterFileData.getSaveDirectory() + "_" + yearSelected + File.separator;

        LocalDate yearForMonth = LocalDate.of(yearSelected, 1, 1);
        for (int i = 0; i < 12; i++) {
            File file = new File(yearDirectory + "_" + yearForMonth.getMonth() + MasterFileData.CASH_FLOW_EXTENSION);
            if (file.exists())
                monthsExistsFile.add(yearForMonth.getMonth().toString());
            yearForMonth = yearForMonth.plusMonths(1);
        }

        return monthsExistsFile;
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

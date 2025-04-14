/**
 * StatsBox.java         Eclipse IDE

 * @author Justin Larsen
 * 7/23/2024
 *
 * Description: Extends Border pane to view a full report of cash flows. Displaying the cash flow information
 * for each year.
 */

package com.experiment.firstintelijfxprojectjl;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.scene.control.Separator;
import java.text.DecimalFormat;

import java.util.ArrayList;

public class StatsBox extends VBox{


    private ArrayList<HBox> inFlow, outFlow;
    private CategoryAccumulator[] inflowAccumulators, outflowAccumulators;
    private double totalInflow = 0, totalOutflow = 0, totalGainOrLoss = 0;
    private int fontSize;
    private String fontType;
    private int maxAccumulatorBarSize;


    //Instnatiates StatsBox sets up storage for each category
    public StatsBox(int fontSize, String fontType)
    {
        this.setAlignment(Pos.CENTER);
        this.fontSize = fontSize;
        this.fontType = fontType;
        maxAccumulatorBarSize = fontSize * 27;

        outflowAccumulators = new CategoryAccumulator[10]; //each index represents a category from CashFlow class outflows
        outflowAccumulators[0] = new CategoryAccumulator(CashFlow.Category.HOUSING, Color.rgb(140, 53, 222)); //purple
        outflowAccumulators[1] = new CategoryAccumulator(CashFlow.Category.UTILITIES, Color.rgb(205, 222, 80)); // yellow
        outflowAccumulators[2] = new CategoryAccumulator(CashFlow.Category.TRANSPORTATION, Color.rgb(227, 73, 91)); //red
        outflowAccumulators[3] = new CategoryAccumulator(CashFlow.Category.FOOD, Color.rgb(250, 143, 50)); //bright orange
        outflowAccumulators[4] = new CategoryAccumulator(CashFlow.Category.HEALTHCARE, Color.rgb(108, 144, 245)); //light blue
        outflowAccumulators[5] = new CategoryAccumulator(CashFlow.Category.EDUCATION, Color.rgb(171, 188, 237)); //very light blue
        outflowAccumulators[6] = new CategoryAccumulator(CashFlow.Category.PERSONAL_CARE, Color.rgb(245, 95, 95)); //light red
        outflowAccumulators[7] = new CategoryAccumulator(CashFlow.Category.ENTERTAINMENT, Color.rgb(0, 0, 0)); //black
        outflowAccumulators[8] = new CategoryAccumulator(CashFlow.Category.TRAVEL, Color.rgb(138, 204, 209)); //light teal
        outflowAccumulators[9] = new CategoryAccumulator(CashFlow.Category.MISC, Color.rgb(227, 150, 222)); //light pink

        inflowAccumulators = new CategoryAccumulator[4];  // each index represents a category from CashFlow class inflows
        inflowAccumulators[0] =  new CategoryAccumulator(CashFlow.Category.SALARY, Color.rgb(158, 227, 150)); //light green
        inflowAccumulators[1] =  new CategoryAccumulator(CashFlow.Category.INVESTMENTS, Color.rgb(213, 227, 89)); //light yellow
        inflowAccumulators[2] =  new CategoryAccumulator(CashFlow.Category.BUISNESS, Color.rgb(62, 81, 222)); //light blue
        inflowAccumulators[3] =  new CategoryAccumulator(CashFlow.Category.OTHER, Color.rgb(128, 81, 156)); //light purple
    }

    //sets all accumulators to 0 for each category
    public void setAllAccumulationToZero()
    {
        totalInflow = 0;
        totalOutflow = 0;
        totalGainOrLoss = 0;

        for(CategoryAccumulator current: outflowAccumulators)
            current.setToZero();

        for(CategoryAccumulator current: inflowAccumulators)
            current.setToZero();
    }

    //adds cashFlow amount to appropriate category, update total inflow, total outflow, and total gain or loss
    public void addAccumulation(CashFlow cashFlow)
    {
        if(cashFlow.getAmount() < 0)
            totalOutflow += cashFlow.getAmount() * -1;
        else
            totalInflow += cashFlow.getAmount();

        totalGainOrLoss += cashFlow.getAmount();

        switch(cashFlow.getCatCategory())
        {
            //outflows
            case CashFlow.Category.HOUSING ->{
                outflowAccumulators[0].accumulate(cashFlow.getAmount());
                break;
            }
            case CashFlow.Category.UTILITIES ->{
                outflowAccumulators[1].accumulate(cashFlow.getAmount());
                break;
            }
            case CashFlow.Category.TRANSPORTATION ->{
                outflowAccumulators[2].accumulate(cashFlow.getAmount());
                break;
            }
            case CashFlow.Category.FOOD ->{
                outflowAccumulators[3].accumulate(cashFlow.getAmount());
                break;
            }
            case CashFlow.Category.HEALTHCARE ->{
                outflowAccumulators[4].accumulate(cashFlow.getAmount());
                break;
            }
            case CashFlow.Category.EDUCATION ->{
                outflowAccumulators[5].accumulate(cashFlow.getAmount());
                break;
            }
            case CashFlow.Category.PERSONAL_CARE ->{
                outflowAccumulators[6].accumulate(cashFlow.getAmount());
                break;
            }
            case CashFlow.Category.ENTERTAINMENT ->{
                outflowAccumulators[7].accumulate(cashFlow.getAmount());
                break;
            }
            case CashFlow.Category.TRAVEL ->{
                outflowAccumulators[8].accumulate(cashFlow.getAmount());
                break;
            }
            case CashFlow.Category.MISC ->{
                outflowAccumulators[9].accumulate(cashFlow.getAmount());
                break;
            }
            //inflows
            case CashFlow.Category.SALARY ->{
                inflowAccumulators[0].accumulate(cashFlow.getAmount());
                break;
            }
            case CashFlow.Category.INVESTMENTS ->{
                inflowAccumulators[1].accumulate(cashFlow.getAmount());
                break;
            }
            case CashFlow.Category.BUISNESS ->{
                inflowAccumulators[2].accumulate(cashFlow.getAmount());
                break;
            }
            case CashFlow.Category.OTHER ->{
                inflowAccumulators[3].accumulate(cashFlow.getAmount());
                break;
            }
        }
    }

    //display Stats box containing information for each accumulated category does not display categories with 0 accumulation
    public void loadStatsBox() {
        this.getChildren().clear();
        if (!(totalInflow == 0 && totalOutflow == 0 && totalGainOrLoss == 0)) {

            GridPane statsBoxGrid = new GridPane();
            // Adding column constraints
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPrefWidth(maxAccumulatorBarSize * 1.3);  // Set preferred width for column 1
            col1.setHgrow(Priority.ALWAYS); // 50% of the GridPane's width

            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPrefWidth(maxAccumulatorBarSize * 1.3);  // Set preferred width for column 1
            col2.setHgrow(Priority.ALWAYS); // 50% of the GridPane's width
            statsBoxGrid.getColumnConstraints().addAll(col1, col2);

            int acumCatKeyRectHeight = (int) (fontSize);
            int acumCatKeyRectWidth = (int) (fontSize);

            VBox inflowVBox = new VBox();
            HBox inflowColorKeyBox1 = new HBox(fontSize / 4);
            HBox inflowColorKeyBox2 = new HBox(fontSize / 4);

            HBox inflowAcumulatorBarBox = new HBox();
            int count = 0;
            for (CategoryAccumulator current : inflowAccumulators) {
                if (current.getAccumulation() > 0) {
                    HBox catKeyBox = new HBox();
                    Rectangle catKeyColorRect = new Rectangle(0,0, acumCatKeyRectHeight,acumCatKeyRectWidth);
                    catKeyColorRect.setFill(current.getColor());
                    catKeyColorRect.setStroke(Color.BLACK);
                    catKeyColorRect.setStrokeWidth(1);
                    Label catTitle = new Label(" " + current.getCategory().toString());
                    catTitle.setFont(Font.font(fontType,fontSize * 3/4));
                    catKeyBox.getChildren().addAll(catKeyColorRect,catTitle);
                    inflowColorKeyBox1.getChildren().add(catKeyBox);


                    int acumBarCatWidth = (int) (maxAccumulatorBarSize * (current.getAccumulation() / totalInflow));
                    int acumBarCatHeight = fontSize;
                    Rectangle acumBarCatRect = new Rectangle(count++ * acumBarCatWidth, 50, acumBarCatWidth, acumBarCatHeight);
                    acumBarCatRect.setFill(current.getColor());
                    acumBarCatRect.setStroke(Color.BLACK);
                    acumBarCatRect.setStrokeWidth(1);
                    inflowAcumulatorBarBox.getChildren().add(acumBarCatRect);
                }
            }

            VBox outflowVBox = new VBox();
            HBox outflowColorKeyBox1 = new HBox(fontSize / 4);
            HBox outflowColorKeyBox2 = new HBox(fontSize / 4);
            HBox outflowAcumulatorBarBox = new HBox();

            count = 0;
            HBox outflowAccumulatorBox = new HBox((int) (fontSize / 2));
            for (CategoryAccumulator current : outflowAccumulators) {
                if (current.getAccumulation() > 0) {


                    HBox catKeyBox = new HBox();
                    Rectangle catKeyColorRect = new Rectangle(0,0, acumCatKeyRectHeight,acumCatKeyRectWidth);
                    catKeyColorRect.setFill(current.getColor());
                    catKeyColorRect.setStroke(Color.BLACK);
                    catKeyColorRect.setStrokeWidth(1);
                    Label catTitle = new Label(" " + current.getCategory().toString());
                    catTitle.setFont(Font.font(fontType,fontSize * 3/4));
                    catKeyBox.getChildren().addAll(catKeyColorRect,catTitle);
                    if(count < 5) {
                        outflowColorKeyBox1.getChildren().add(catKeyBox);
                    }
                    else
                    {
                        outflowColorKeyBox2.getChildren().add(catKeyBox);
                    }

                    int acumBarCatWidth = (int) (maxAccumulatorBarSize * (current.getAccumulation() / totalOutflow));
                    int acumBarCatHeight = fontSize;
                    Rectangle acumBarCatRect = new Rectangle(count++ * acumBarCatWidth, 50, acumBarCatWidth, acumBarCatHeight);
                    acumBarCatRect.setFill(current.getColor());
                    acumBarCatRect.setStroke(Color.BLACK);
                    acumBarCatRect.setStrokeWidth(1);
                    outflowAcumulatorBarBox.getChildren().add(acumBarCatRect);
                }
            }

            if(count > 5) {
                inflowVBox.getChildren().addAll(inflowColorKeyBox1,inflowColorKeyBox2, inflowAcumulatorBarBox);
                outflowVBox.getChildren().addAll(outflowColorKeyBox1, outflowColorKeyBox2, outflowAcumulatorBarBox);
            }
            else {
                inflowVBox.getChildren().addAll(inflowColorKeyBox1, inflowAcumulatorBarBox);
                outflowVBox.getChildren().addAll(outflowColorKeyBox1, outflowAcumulatorBarBox);
            }

            inflowVBox.setSpacing(fontSize / 2);
            outflowVBox.setSpacing(fontSize / 2);

            Separator separator1 = new Separator(), separator2 = new Separator();
            separator1.setStyle("-fx-background-color: black; -fx-pref-height: 1;");
            separator2.setStyle("-fx-background-color: black; -fx-pref-height: 1;");

            DecimalFormat decimalFormat = new DecimalFormat("#.00");

            HBox totalsBox = new HBox();
            Label totalInflowLabel = new Label("Total Inflow: ");
            totalInflowLabel.setFont(Font.font(fontType, fontSize * 1.3));
            Label totalInflowAmountLabel = new Label();
            totalInflowAmountLabel.setFont(Font.font(fontType, fontSize * 1.3));
            if(totalInflow == 0) {
                totalInflowAmountLabel.setText("$0.00");
                totalInflowAmountLabel.setStyle("-fx-text-fill: black;");
            }
            else {
                totalInflowAmountLabel.setText("+$" + decimalFormat.format(totalInflow));
                totalInflowAmountLabel.setStyle("-fx-text-fill: green;");
            }

            Label totalOutflowLabel = new Label("         Total Outflow: ");
            totalOutflowLabel.setFont(Font.font(fontType, fontSize * 1.3));
            Label totalOutflowAmountLabel = new Label();
            totalOutflowAmountLabel.setFont(Font.font(fontType, fontSize * 1.3));
            if(totalOutflow == 0) {
                totalOutflowAmountLabel.setText("$0.00");
                totalOutflowAmountLabel.setStyle("-fx-text-fill: black;");
            }
            else {
                totalOutflowAmountLabel.setText("-$" + decimalFormat.format(totalOutflow));
                totalOutflowAmountLabel.setStyle("-fx-text-fill: red;");
            }


            Label totalGainOrLossLabel = new Label();
            totalGainOrLossLabel.setFont(Font.font(fontType, fontSize * 1.3));
            Label totalGainOrLossflowAmountLabel = new Label();
            totalGainOrLossflowAmountLabel.setFont(Font.font(fontType, fontSize * 1.3));
            if(totalGainOrLoss == 0) {
                totalGainOrLossLabel.setText("         No Gain or Loss");
            }
            else if(totalGainOrLoss > 0){
                totalGainOrLossLabel.setText("         Gain: ");
                totalGainOrLossflowAmountLabel.setText("+$" + decimalFormat.format(totalGainOrLoss));
                totalGainOrLossflowAmountLabel.setStyle("-fx-text-fill: green;");
            }
            else{
                totalGainOrLossLabel.setText("         Loss: ");
                totalGainOrLossflowAmountLabel.setText("-$" + decimalFormat.format(totalGainOrLoss));
                totalGainOrLossflowAmountLabel.setStyle("-fx-text-fill: red;");
            }

            totalsBox.getChildren().addAll(totalInflowLabel,totalInflowAmountLabel, totalOutflowLabel,totalOutflowAmountLabel, totalGainOrLossLabel,totalGainOrLossflowAmountLabel);
            totalsBox.setAlignment(Pos.CENTER);

            HBox inflowAndOutflowAccumBox = new HBox();

            statsBoxGrid.setPadding(new Insets(0, fontSize, fontSize, fontSize));
            statsBoxGrid.setHgap(fontSize);
            statsBoxGrid.setAlignment(Pos.CENTER);
            statsBoxGrid.add(inflowVBox, 0, 0);
            statsBoxGrid.add(outflowVBox, 1, 0);

            this.setMargin(separator1, new Insets(0, 0, 5, 0));
            this.setMargin(separator2, new Insets(0, 0, 5, 0));
            this.getChildren().addAll(separator1,totalsBox,separator2,statsBoxGrid);
        }
    }

}


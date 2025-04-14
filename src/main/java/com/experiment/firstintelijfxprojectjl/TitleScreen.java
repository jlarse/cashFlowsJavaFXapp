package com.experiment.firstintelijfxprojectjl;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.*;
import javafx.scene.layout.VBox;

public class TitleScreen extends VBox {

    private String fontName;
    private int fontSize;

    public TitleScreen(String fontName, int fontSize)
    {
        this.fontName = fontName;
        this.fontSize = fontSize;

        this.setAlignment(Pos.CENTER);
        Label titleScreenText1 = new Label("\nCash Flows");
        titleScreenText1.setFont(Font.font(fontName, FontWeight.BOLD, FontPosture.REGULAR, fontSize * 2));
        titleScreenText1.setAlignment(Pos.CENTER);
        Label titleScreenText2 = new Label("by");
        titleScreenText2.setFont(Font.font(fontName, FontWeight.NORMAL, FontPosture.ITALIC, fontSize));
        titleScreenText2.setAlignment(Pos.CENTER);
        Label titleScreenText3 = new Label("Justin Larsen");
        titleScreenText3.setFont(Font.font(fontName, FontWeight.NORMAL, FontPosture.ITALIC, fontSize));
        titleScreenText3.setAlignment(Pos.CENTER);
        Label progDescriptionText = new Label("   Welcome. This is a monetary program used to record money coming in and going\n" +
                "out of an one's possession, these are called Cash Flows. This program allows one \n to log their cash flows and " +
                "view their gains or losses over chosen periods of time.\n\n\n");

        progDescriptionText.setFont(Font.font(fontName,fontSize));
        progDescriptionText.setAlignment(Pos.CENTER);

        // Create text to be underlined for instructions label
        Text progInstructionText = new Text("Instructions:");
        progInstructionText.setFont(Font.font(fontName, fontSize)); // Set font to Arial, size 20
        progInstructionText.setUnderline(true); // Set underline
        // Create a TextFlow to hold the Text
        TextFlow textFlow = new TextFlow(progInstructionText);
        // Create a Label and set its graphic to the TextFlow
        Label instructionsLabelTitle = new Label();
        instructionsLabelTitle.setGraphic(textFlow);
        Label instructionsLabel = new Label("1) Select add cash flow to add a new cash flow record.\n\n" +
                "2) Select a report from the top(monthly,yearly,full,or custom) to\n" +
                "    view your cash flows over chosen periods of time.\n\n" +
                "3) Cash flows may be removed by clicking the checkbox next to\n" +
                "     the cash flow record and clicking remove checked. This may\n" +
                "     only be done in the month report or custom report screens.");
        instructionsLabel.setFont(Font.font(fontName,fontSize));


        //add title author and program descriptioon to titleScreen
        this.getChildren().addAll(titleScreenText1,titleScreenText2,titleScreenText3,progDescriptionText);
        //Add instructions to title screen
        this.getChildren().addAll(instructionsLabelTitle, instructionsLabel);
    }
}

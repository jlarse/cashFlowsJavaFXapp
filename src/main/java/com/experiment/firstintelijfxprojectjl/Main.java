/**
 * Main.java         Eclipse IDE

 * @author Justin Larsen
 * 7/23/2024
 *
 * Description: My first whole JavaFX apoplication based off my C++ program FinanceReporter
 */

package com.experiment.firstintelijfxprojectjl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.io.*;

public class Main extends Application {

    final static int R1_WIDTH = 1920;
    final static int R1_HEIGHT = 1080;
    final static int R2_WIDTH = 1280;
    final static int R2_HEIGHT = 720;

    private static MasterFileData masterFileData;

    @Override
    public void start(Stage window) throws IOException {

        window.setResizable(true);

        int windowWidth = R2_WIDTH;
        int windowHeight = R2_HEIGHT;

        //FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);


        int fontSize = windowWidth / 65;
        int menuItemSpacing = fontSize / 3;
        String fontName = "Arial";
        Font appFont = new Font(fontName, fontSize);

        //load master file
        loadMasterFileData(fontName, fontSize); //load master Data in masterData

        VBox menu = new VBox(menuItemSpacing);
        //HBox for main menu
        HBox viewMenu = new HBox(menuItemSpacing);

        Label optionsTitle = new Label("Options:");
        optionsTitle.setFont(Font.font(fontName, FontWeight.EXTRA_BOLD, FontPosture.REGULAR, fontSize));
        Button goTitleScreen = new Button("Title Screen");
        goTitleScreen.setFont(appFont);
        Button monthButton = new Button("Report by Month");
        monthButton.setFont(appFont);
        Button yearButton = new Button("Report by Year");
        yearButton.setFont(appFont);
        Button customButton = new Button("Custom Report");
        customButton.setFont(appFont);
        Button fullButton = new Button("Full Report");
        fullButton.setFont(appFont);
        viewMenu.setAlignment(Pos.CENTER);
        viewMenu.getChildren().addAll(optionsTitle,goTitleScreen,monthButton,yearButton,fullButton,customButton);

        //Hbox for control menu
        HBox controlMenu = new HBox();
        Button addCashFlow = new Button ("Add Cash Flow");
        addCashFlow.setFont(appFont);
        Button removeChecked = new Button ("Remove Checked");
        removeChecked.setFont(appFont);
        Label contentTitle = new Label();
        // Add a spacer to push addCashFlow to far left, and removeChecked to far right
        Region middlePushLeft = new Region(), middlePushRight = new Region();
        HBox.setHgrow(middlePushLeft, javafx.scene.layout.Priority.ALWAYS); // Make spacer grow to fill space
        HBox.setHgrow(middlePushRight, javafx.scene.layout.Priority.ALWAYS); // Make spacer grow to fill space
        //Add spacer for far left and right between button and edge of window
        Region farLeft = new Region();
        farLeft.setMinWidth(menuItemSpacing);
        Region farRight = new Region();
        farRight.setMinWidth(menuItemSpacing);


        controlMenu.getChildren().addAll(farLeft,addCashFlow,middlePushLeft,contentTitle,middlePushRight, removeChecked,farRight);

        //label that displays newly added cash flow
        HBox newCashFlowBox = new HBox();
        Label newCashFlowAdded = new Label();
        newCashFlowAdded.setFont(Font.font(fontName,fontSize));
        newCashFlowBox.getChildren().add(newCashFlowAdded);
        newCashFlowBox.setAlignment(Pos.CENTER);

        // Create a Line to place at the bottom
        Line bottomLine = new Line();
        bottomLine.setStartX(0);
        bottomLine.setEndX(windowWidth); // Initial length of the line
        bottomLine.setStartY(0);
        bottomLine.setEndY(0); // Horizontal line
        bottomLine.setStrokeWidth(2); // Line thickness
        bottomLine.setStroke(javafx.scene.paint.Color.BLACK); // Line color

        // Bind the endX property of the line to the width of the HBox
       // bottomLine.endXProperty().bind(controlMenu.widthProperty());
        // Create a Line to place at the bottom
        Line bottomLine2 = new Line();
        bottomLine2.setStartX(0);
        bottomLine2.setEndX(windowWidth); // Initial length of the line
        bottomLine2.setStartY(0);
        bottomLine2.setEndY(0); // Horizontal line
        bottomLine2.setStrokeWidth(2); // Line thickness
        bottomLine2.setStroke(javafx.scene.paint.Color.BLACK); // Line color

        // Bind the endX property of the line to the width of the HBox
        //bottomLine2.endXProperty().bind(controlMenu.widthProperty());


        menu.getChildren().addAll(viewMenu,controlMenu,bottomLine,newCashFlowBox,bottomLine2);
        //Scroll pane for the display of cash flows
        VBox contentPane = new VBox();

        HBox statsBox = new HBox();
        statsBox.setAlignment(Pos.CENTER);

        //Setup Event Handling
        CashFlowEventHandler eventHandler = new CashFlowEventHandler(masterFileData,contentPane,statsBox ,contentTitle,newCashFlowAdded,fontName,fontSize);
        goTitleScreen.setOnAction(eventHandler);
        monthButton.setOnAction(eventHandler);
        yearButton.setOnAction(eventHandler);
        customButton.setOnAction(eventHandler);
        fullButton.setOnAction(eventHandler);
        addCashFlow.setOnAction(eventHandler);
        removeChecked.setOnAction(eventHandler);

        //window contains mainLayout as border pane
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(menu);
        mainLayout.setCenter(contentPane);


        //retrieve stats box from event hanbdler then month screen for stats on screen
        mainLayout.setBottom(statsBox);
        Scene mainScene = new Scene(mainLayout, windowWidth, windowHeight);

        //load window
        window.setTitle("Cash Flows");
        window.setScene(mainScene);
        window.show();

        //-------------------------------End of display----------------------------------------------------------------//
    }


    /** -If no save directory is created , create save directory and put a new master file in it,
         and create now masterFileData with today as mostRecentDate, and earliestDate
        -If save direcory exists but master file doesn't create new master file in save directory,
         and create now masterFileData with today as mostRecentDate, and earliestDate
        -If save directory and master file exist load master file data
    */
     private static void loadMasterFileData(String font, int fontSize)
    {
        boolean firstTime_SaveDirectoryCreated = false, firstTime_MasterFileCreated = false, newMasterCreated = false;

        File dir = new File(MasterFileData.getSaveDirectory());
        if (!dir.exists()) //if save direcory doesnt exist create it and put a new master file into it
        {
            try {
                if(dir.mkdirs())// Create the directory if it doesn't exist
                    firstTime_SaveDirectoryCreated = true;
            }catch (SecurityException exception)
            {
                AlertBox.display("Make Directory Error", exception.getMessage(),font,fontSize);
            }

            masterFileData = new MasterFileData();

            try (FileOutputStream fileOut = new FileOutputStream(MasterFileData.getSavePath());
                 ObjectOutputStream out = new ObjectOutputStream(fileOut))
            {
                out.writeObject(masterFileData);
                firstTime_MasterFileCreated = true;
            } catch (IOException i)
            {
                AlertBox.display("Create new master file error", i.getMessage(),font,fontSize);
                i.printStackTrace();
            }
        }
        else
        {
            File file = new File(MasterFileData.getSavePath());

            // Check if the master file exists, if it does load masterFileData form master file
            if (file.exists())
            {
                try (FileInputStream fileIn = new FileInputStream(file.getPath());
                     ObjectInputStream in = new ObjectInputStream(fileIn))
                {

                    // Read the object from the file
                    masterFileData = (MasterFileData) in.readObject();


                } catch (IOException | ClassNotFoundException e)
                {
                    AlertBox.display("Read from master file error", e.getMessage(),font,fontSize);
                    e.printStackTrace();
                }
            }
            else //if master file does not exist create new one
            {
                masterFileData = new MasterFileData();

                try (FileOutputStream fileOut = new FileOutputStream(MasterFileData.getSavePath());
                     ObjectOutputStream out = new ObjectOutputStream(fileOut))
                {
                    out.writeObject(masterFileData);
                    newMasterCreated = true;
                } catch (IOException i)
                {
                    AlertBox.display("Create new master file error", i.getMessage(),font,fontSize);
                    i.printStackTrace();
                }
            }
        }
    }
}
package com.experiment.firstintelijfxprojectjl; /**
 * AlertBox.java         Eclipse IDE
	
 * @author Justin Larsen  
 * 7/17/2024 - 7/17/22
 * 
 * Description: Implements an alert box that pops up which must be dealt with before other window(s)
 */

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.text.Font;

public class AlertBox {

	public static void display(String title, String message,String font, int fontSize)
	{
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL); //APPLICATION_MODAL setting so this window must be dealt with before 
														 //returning to other window(s)
		window.setTitle(title);
		window.setMinWidth(fontSize * 20);
		window.setMinHeight(fontSize * 10);
		
		Label label = new Label(message + "\n");
		label.setFont(Font.font(font,fontSize));
		Button button = new Button("Close");
		button.setFont(Font.font(font,fontSize));
		button.setOnAction(e -> window.close());
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label,button);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait(); //shows stage and waits for it to be closed before returning to the caller
	}	
}

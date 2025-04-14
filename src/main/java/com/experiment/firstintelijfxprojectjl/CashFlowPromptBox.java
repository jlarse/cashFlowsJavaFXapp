package com.experiment.firstintelijfxprojectjl; /**
 * ConfirmBox.java         Eclipse IDE
	
 * @author Justin Larsen  
 * 7/18/2024 - 7/118/22
 * 
 * Description: Implements a confirm box that pops up and must be delt with before returning to other window(s).
 */
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.time.LocalDate;
import javafx.scene.text.Font;
import javafx.scene.control.TextFormatter;
import java.text.DecimalFormat;
import java.util.function.UnaryOperator;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextArea;

public class CashFlowPromptBox {

	public static CashFlow display(String font, int fontSize, LocalDate lastCashFlowAdded) {
		final CashFlow[] wrapperFlow = new CashFlow[1];
		final Boolean[] wrapperIsInFlow = new Boolean[1];

		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL); //APPLICATION_MODAL setting so this window must be delt with before
		// returning to other window(s)
		window.setTitle("New Cash Flow");
		window.setMinWidth(fontSize * 20);
		window.setMinHeight(fontSize * 15);

		Label formHeader = new Label("Enter cash flow information:");
		formHeader.setFont(Font.font(font, fontSize));
		formHeader.setAlignment(Pos.CENTER);

		//Date selector
		HBox dateSelect = new HBox((int) (fontSize /3));
		Label dateTitle = new Label("         Date:      ");
		dateTitle.setFont(Font.font(font,fontSize));
		DatePicker datePicker = new DatePicker();
		datePicker.setStyle("-fx-font-family: '" + font + "'; -fx-font-size: " + fontSize + ";");
		datePicker.setPrefWidth(fontSize * 9);
		dateSelect.getChildren().addAll(dateTitle,datePicker);
		datePicker.setEditable(false); // Disable text input
		if(lastCashFlowAdded != null)
			datePicker.setValue(lastCashFlowAdded);

		// Consume any key events to prevent typing
		datePicker.getEditor().addEventFilter(KeyEvent.KEY_TYPED, event -> event.consume());

		//Retrive inflow or out flow to set specific categories based on users choice
		HBox flowSelect = new HBox((int) (fontSize / 3));
		Label flowSelectTitle = new Label("         Flow type:");
		flowSelectTitle.setFont(Font.font(font, fontSize));
		RadioButton inFlow = new RadioButton("Inflow");
		inFlow.setFont(Font.font(font, fontSize));
		RadioButton outFlow = new RadioButton("Outflow");
		outFlow.setFont(Font.font(font, fontSize));
		ToggleGroup flowGroup = new ToggleGroup();
		inFlow.setToggleGroup(flowGroup);
		outFlow.setToggleGroup(flowGroup);
		flowSelect.getChildren().addAll(flowSelectTitle, inFlow, outFlow);

		//Retrieve category of cash flow from user
		HBox categorySelect = new HBox(fontSize / 3);
		Label categoryTitle = new Label("         Category:");
		categoryTitle.setFont(Font.font(font, fontSize));
		ComboBox<String> categorySelector = new ComboBox<>();
		categorySelector.setStyle("-fx-font-family: '" + font + "'; -fx-font-size: " + fontSize + ";");

		categorySelect.getChildren().addAll(categoryTitle, categorySelector);

		//Retrieve amount of cash flow from user
		HBox amountSelect = new HBox(fontSize / 3);
		Label amountTitle = new Label("         Amount:$");
		amountTitle.setFont(Font.font(font, fontSize));
		TextField amountField = new TextField();
		amountField.setStyle("-fx-font-family: '" + font + "'; -fx-font-size: " + fontSize + ";");
		amountField.setPrefWidth(fontSize *10);
		amountSelect.getChildren().addAll(amountTitle, amountField);

		DecimalFormat format = new DecimalFormat("#.##");

		// Create a TextFormatter to enforce the input format to only double
		UnaryOperator<TextFormatter.Change> filter = change -> {
			String newText = change.getControlNewText();
			if (newText.isEmpty()) {
				return change;
			}

			// Regex to match a double with up to 2 decimal places
			if (newText.matches("\\d*\\.?\\d{0,2}")) {
				return change;
			}

			return null;
		};

		// Set the TextFormatter to the TextField
		TextFormatter<String> textFormatter = new TextFormatter<>(filter);
		amountField.setTextFormatter(textFormatter);

		inFlow.setOnAction(e -> {
			categorySelector.getItems().clear();
			String[] categories = CashFlow.getIncomeCategories();
			for (String current : categories)
				categorySelector.getItems().add(current);
			categorySelector.setStyle("-fx-font-family: '" + font + "'; -fx-font-size: " + fontSize + ";");
			wrapperIsInFlow[0] = true;
		});

		outFlow.setOnAction(e -> {
			categorySelector.getItems().clear();
			String[] categories = CashFlow.getExpenseCategories();
			for (String current : categories)
				categorySelector.getItems().add(current);
			categorySelector.setStyle("-fx-font-family: '" + font + "'; -fx-font-size: " + fontSize + ";");
			wrapperIsInFlow[0] = false;
		});

		TextArea descriptionTextArea = new TextArea();
		descriptionTextArea.setPrefColumnCount(5);
		descriptionTextArea.setMaxWidth(fontSize * 17);
		descriptionTextArea.setPrefRowCount(3);
		descriptionTextArea.setFont(Font.font(font,fontSize));
		Label descriptionLabel = new Label("Description (optional):");
		descriptionLabel.setFont(Font.font(font,fontSize));

		HBox buttonsBox = new HBox(fontSize / 3);
		Button confirmButton = new Button("Confirm");
		confirmButton.setFont(Font.font(font,fontSize));
		Button cancelButton = new Button("Cancel");
		cancelButton.setFont(Font.font(font,fontSize));
		buttonsBox.getChildren().addAll(confirmButton,cancelButton);
		buttonsBox.setAlignment(Pos.CENTER);

		confirmButton.setOnAction(e -> {
			LocalDate selectedDate = datePicker.getValue();
			String categoryString = categorySelector.getValue();
			String doubleString = amountField.getText();

			//create cash flow if all values from form contain a value
			if(categoryString != null && !doubleString.isEmpty() && selectedDate != null)
			{
				double amount = Double.parseDouble(doubleString);
				if (!wrapperIsInFlow[0]) {
					amount *= -1;
				}

				try {
					wrapperFlow[0] = new CashFlow(CashFlow.getCategoryFromString(categoryString), selectedDate, amount);
					wrapperFlow[0].setDescription(descriptionTextArea.getText());
				} catch (Exception exception) {
					AlertBox.display("Cash Flow Error", exception.getMessage(),font,fontSize);
				}
				window.close();
			}
			else
			{
				AlertBox.display("Incomplete Cash Flow", "All cash flow informatoin not filled out.\nPlease complete form to add cash flow\n" +
						"or select cancel.", font, fontSize);
			}

		});

		cancelButton.setOnAction(e -> {
			window.close();
		});

		VBox layout = new VBox(10);
		layout.getChildren().addAll(formHeader,dateSelect, flowSelect, categorySelect, amountSelect,descriptionLabel,descriptionTextArea, buttonsBox);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait(); //shows stage and waits for it to be closed before returning to the caller

		return wrapperFlow[0];

	}
}

package edu.um.chromaster.gui.menu.boxes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Box that pops up if the user inputs something else than an integer for the number of vertices/edges input
 */
public class AlertBox {
	
	public static void display() {
		
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Error");
		window.setMinWidth(250);
		
		Label label = new Label();
		label.setText("Your input must be a number ! ");
		
		Button close = new Button("OK");
		close.setOnAction(e -> window.close());
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, close);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(20,20,20,20));
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		
	}

}

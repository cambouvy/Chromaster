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
 * Box that pops up if the user clicks on the "Rules" button
 * It describes how to play and the different game modes
 */
public class RulesBox {
	
	public static void display() {
		
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Rules");
		window.setMinWidth(250);
		
		Label label = new Label();
		label.setText("A graph will appear on your screen and your job is to color it such that no adjacent vertives have \n"
					    + "the same color.To do so, click on the node that you want to color and and the corresponding color \n"
					    + "of your choice. \nIf you want to add a new color simply press on the \"add\" button. \n "
					    + "\n"
					    + "There are 3 different game modes: \n"
					    + "  - TO THE BITTER END : Color the graph as quickly as possible \n "
					    + " - BEST UPPER BOUND IN A FIXED TIME FRAME: You have a fixed amount of time to find \n"
					    + "a coloring of the graph with as few colors as possible \n "
					    + " - RANDOM ORDER: A random ordering of the vertices is created, you need to pick the \n"
					    + "colors of the vertices exactly in that order. Keep in mind that once the color of a vertex \n"
					    + "has been chosen, it cannot be changed again. Your goal is to use as few colors as possible \n"
					    + "Good luck!");
		
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

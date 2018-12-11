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
 * Box that pops up if the user tries to color two adjacent vertices with the same color
 * It warns the user that this is not possible
 */

public class WarningBox {

    public static void display() {

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Error");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText("Two adjacent vertices can't have the same color !");

        Button close = new Button("OK");
        close.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, close);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20,20,20,20));

        Scene scene = new Scene(layout);
        scene.getStylesheets().add("res/style.css");
        layout.getStyleClass().add("warning-box");
        window.setScene(scene);
        window.showAndWait();


    }

}



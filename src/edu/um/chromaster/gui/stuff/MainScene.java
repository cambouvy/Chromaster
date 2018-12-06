package edu.um.chromaster.gui.stuff;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainScene {

	public static Scene createMainScene(Stage window) {
		Button button1 = new Button("To the bitter end");
		Button button2 = new Button("Best upper bound in a fixed time frame");
		Button button3 = new Button("Random order");
		Label welcome = new Label("Welcome to Chromaster!");
		Label start = new Label("Chose your game mode");


		button1.setOnAction(e -> chosenGM1(window));
		button2.setOnAction(e -> chosenGM2(window));
		button3.setOnAction(e -> chosenGM3(window));

		GridPane mainGrid = new GridPane();
		mainGrid.setPickOnBounds(false);

		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		mainGrid.setPadding(new Insets(0, 10, 0, 10));

		mainGrid.add(welcome, 2, 1);
		mainGrid.add(start, 2, 2);
		mainGrid.add(button1, 2, 3);
		mainGrid.add(button2, 2, 4);
		mainGrid.add(button3, 2, 5);

		mainGrid.setAlignment(Pos.CENTER);

		HBox topBar = new HBox();
		Button rules = new Button("Rules");
		rules.setOnAction(e -> RulesBox.display());
		topBar.getChildren().addAll(rules);
		topBar.setPadding(new Insets(2,2,2,2));

		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topBar);
		borderPane.setCenter(mainGrid);

		Scene scene = new Scene(borderPane, 1280, 720);
		scene.getStylesheets().add("res/style.css");

		return scene;
	}

	private static void chosenGM1(Stage window) {
		ChosenGameMode.chooseGameMode1 = true;
		ChosenGameMode.chooseGameMode2 = false;
		ChosenGameMode.chooseGameMode3 = false;
		System.out.println("Chosen mode: To the bitter end" );
		window.setScene(GameModeScene.createGameModeScene(window));
	}

	private static void chosenGM2(Stage window) {
		ChosenGameMode.chooseGameMode2 = true;
		ChosenGameMode.chooseGameMode1 = false;
		ChosenGameMode.chooseGameMode3 = false;
		System.out.println("Chosen mode: Best upper bound in a fixed time frame" );
		window.setScene(GameModeScene.createGameModeScene(window));

	}

	private static void chosenGM3(Stage window) {
		ChosenGameMode.chooseGameMode3 = true;
		ChosenGameMode.chooseGameMode1 = false;
		ChosenGameMode.chooseGameMode2 = false;
		System.out.println("Chosen mode: Random order" );
		window.setScene(GameModeScene.createGameModeScene(window));
	}


}
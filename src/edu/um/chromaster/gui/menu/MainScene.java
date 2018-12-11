package edu.um.chromaster.gui.menu;

import edu.um.chromaster.Game;
import edu.um.chromaster.gui.MenuScene;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 * Scene in which the user can selected his preferred game mode
 * The choice is saved in the boolean variables from the ChosenGameMode class
 */

public class MainScene {

	public static Scene createMainScene(Stage window) {

		Game.getInstance().getEventHandler().clear(); // reset event handler fully

		Button button1 = new Button("To the bitter end");
		Button button2 = new Button("Best upper bound in a fixed time frame");
		Button button3 = new Button("Random order");
		Label welcome = new Label("Welcome to Chromaster!");
		Label start = new Label("Choose your game mode");

		button1.setOnAction(e -> chosenGM1(window));
		button2.setOnAction(e -> chosenGM2(window));
		button3.setOnAction(e -> chosenGM3(window));

		return new Scene(MenuScene.createParent(Arrays.asList(welcome, start, button1, button2, button3)), 1280, 720);
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

package edu.um.chromaster.gui.menu;

import edu.um.chromaster.Game;
import edu.um.chromaster.gui.MenuScene;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 * Scene in which the user can selected if he wants to play with a random graph, generate his own or import a graph from a text file
 * If he chooses random graph the choice will be saved in the random boolean parameter from the ChosenVerticesOrEdges class
 */

public class GameModeScene {
	
	
	public static Scene createGameModeScene(Stage window) {
		
		Label intro = new Label("Good choice! ;) ");
		Label chooseGraph = new Label("Do you want to play with a random graph or do you want to generate it yourself?");
		Button button1GM = new Button("Play with a random graph");
		Button button2GM = new Button("Generate my own graph");
		Button button3GM = new Button("Read in Graph from File");
		
		button1GM.setOnAction(e -> pressedButton1GM());
		button2GM.setOnAction(e -> Game.getInstance().getStage().setScene(GenerateGraphScene.generateGraphScene(window)));
		button3GM.setOnAction(event -> window.setScene(ReadGraphScene.createReadGraphScene(window)));

		return new Scene(MenuScene.createParent(Arrays.asList(intro, chooseGraph, button1GM, button2GM, button3GM), (a) -> {
				window.setScene(MainScene.createMainScene(Game.getInstance().getStage()));
		}), 1280, 720);
	}
	
	public static void pressedButton1GM() {
		ChosenVerticesOrEdges.random = true;
		ChosenVerticesOrEdges.vertices = false;
		ChosenVerticesOrEdges.verticesAndEdges = false;
		ChosenVerticesOrEdges.edges = false;
		System.out.println("The player is playing with a random graph");
		Game.getInstance().getStage().setScene(DifficultyScene.createDifficultyScene(Game.getInstance().getStage()));
		
	}

}

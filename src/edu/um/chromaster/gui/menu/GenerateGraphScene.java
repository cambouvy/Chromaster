package edu.um.chromaster.gui.menu;

import edu.um.chromaster.gui.MenuScene;
import edu.um.chromaster.gui.menu.boxes.RulesBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Scene appearing if the user wants to generate his own graph 
 * It gives the user the choice to select the number of vertices, edges or both
 * The choice is saved in the boolean variables from the ChosenVerticesOrEdges class
 */

public class GenerateGraphScene {
	
	public static Scene generateGraphScene(Stage window) {
		
		Label VorE = new Label("Do you want to choose the number of vertices, the number of edges or both?");
		Button vertices = new Button("Choose the number of vertices only");
		Button edges = new Button("Choose the number of edges only");
		Button verticesAndEdges = new Button("Choose the number of vertices and the number of edges");
		
		
		verticesAndEdges.setOnAction(e -> pressedVerticesAndEdges(window));
		vertices.setOnAction(e -> pressedVertices(window));
		edges.setOnAction(e -> pressedEdges(window));

		GridPane chooseGraphGrid = new GridPane();
		chooseGraphGrid.setPickOnBounds(true);
		chooseGraphGrid.setHgap(10);
		chooseGraphGrid.setVgap(10);
		chooseGraphGrid.setPadding(new Insets(0, 10, 0, 10));

		chooseGraphGrid.add(VorE, 2, 2);
		chooseGraphGrid.add(vertices, 2, 3);
		chooseGraphGrid.add(edges, 2, 4);
		chooseGraphGrid.add(verticesAndEdges, 2, 5);
		   
		chooseGraphGrid.setAlignment(Pos.CENTER);
		
		HBox topBar = new HBox();
		Button back = new Button("Go back");
		back.setOnAction(e -> window.setScene(GameModeScene.createGameModeScene(window)));
		Button rules = new Button("Rules");
		rules.setOnAction(e -> RulesBox.display());
		topBar.getChildren().addAll(back, rules);
		topBar.setPadding(new Insets(2,2,2,2));
		
		BorderPane borderPaneGenerateGraph = new BorderPane();
		borderPaneGenerateGraph.setTop(topBar);
		borderPaneGenerateGraph.setCenter(chooseGraphGrid);

		return new Scene(MenuScene.createBasicParent(borderPaneGenerateGraph), 1280, 720);
	}
	
	private static void pressedVerticesAndEdges(Stage window) {
		ChosenVerticesOrEdges.verticesAndEdges = true;
		ChosenVerticesOrEdges.vertices = false;
		ChosenVerticesOrEdges.edges = false;
		ChosenVerticesOrEdges.random = false;
		System.out.println("The number of vertices and edges are chosen by the player" );
		window.setScene(VerticesEdgesInputScenes.createVerticesAndEdgesScene(window));	
	}
	
	private static void pressedVertices(Stage window) {
		ChosenVerticesOrEdges.vertices = true;
		ChosenVerticesOrEdges.edges = false;
		ChosenVerticesOrEdges.verticesAndEdges = false;
		ChosenVerticesOrEdges.random = false;
		System.out.println("The number of vertices is chosen by the player" );
		window.setScene(VerticesEdgesInputScenes.createVerticesScene(window));
		
	}
	
	private static void pressedEdges(Stage window) {
		ChosenVerticesOrEdges.edges = true;
		ChosenVerticesOrEdges.vertices = false;
		ChosenVerticesOrEdges.verticesAndEdges = false;
		ChosenVerticesOrEdges.random = false;
		System.out.println("The number of edges is chosen by the player" );
		window.setScene(VerticesEdgesInputScenes.createEdgesScene(window));
		
	}
	

}

package edu.um.chromaster.gui.menu;

import edu.um.chromaster.gui.MenuScene;
import edu.um.chromaster.gui.menu.boxes.AlertBox;
import edu.um.chromaster.gui.menu.boxes.RulesBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
* Creates the scene in which the user selects the number of vertices and edges
* @param window, the base stage 
* @return scene, the created scene
*/

public class VerticesEdgesInputScenes {
	
	public static int numberOfVertices;
	public static int numberOfEdges;
	
	
	public static Scene createVerticesAndEdgesScene(Stage window) {
		
		GridPane verticesAndEdgesGrid = new GridPane();
		verticesAndEdgesGrid.setPadding(new Insets(0, 10, 0, 10));
		verticesAndEdgesGrid.setVgap(10);
		verticesAndEdgesGrid.setHgap(10);
		
		Label typeInVandE = new Label("Please type in the number of vertices and edges that you want to have");
		GridPane.setConstraints(typeInVandE, 0, 0);
		
		Label numOfVertices = new Label("Number of vertices : ");
		GridPane.setConstraints(numOfVertices, 0, 1);
		
		TextField verticesInput = new TextField();
		verticesInput.setPromptText("Type in a number here");
		GridPane.setConstraints(verticesInput, 1, 1);
		
		Label numOfEdges = new Label("Number of edges : ");
		GridPane.setConstraints(numOfEdges, 0, 2);
		
		TextField edgesInput = new TextField();
		edgesInput.setPromptText("Type in a number here");
		GridPane.setConstraints(edgesInput, 1, 2);
		
		Button enter = new Button("Enter");
		enter.setOnAction(e -> verticesAndEdgesAreInt(verticesInput, edgesInput, window));
		GridPane.setConstraints(enter, 1, 3);
		
		verticesAndEdgesGrid.setAlignment(Pos.CENTER);
		
		verticesAndEdgesGrid.getChildren().addAll(typeInVandE, numOfVertices, verticesInput, numOfEdges, edgesInput, enter);
		
		HBox topBar = new HBox();
		Button back = new Button("Go back");
		back.setOnAction(e -> window.setScene(GenerateGraphScene.generateGraphScene(window)));
		Button rules = new Button("Rules");
		rules.setOnAction(e -> RulesBox.display());
		topBar.getChildren().addAll(back, rules);
		topBar.setPadding(new Insets(2,2,2,2));
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topBar);
		borderPane.setCenter(verticesAndEdgesGrid);
		borderPane.getStyleClass().add("MainScene-background");

		return new Scene(MenuScene.createBasicParent(borderPane), 1280, 720);
		
	}
	
	/**
	 * Creates the scene in which the user selects the number of vertices
	 * @param window, the base stage 
	 * @return scene, the created scene
	 */
	public static Scene createVerticesScene(Stage window) {
		GridPane verticesGrid = new GridPane();
		verticesGrid.setPadding(new Insets(0, 10, 0, 10));
		verticesGrid.setVgap(10);
		verticesGrid.setHgap(10);
		
		Label typeInV = new Label("Please type in the number of vertices that you want to have");
		GridPane.setConstraints(typeInV, 0, 0);
		
		Label numOfVertices = new Label("Number of vertices : ");
		GridPane.setConstraints(numOfVertices, 0, 1);
		
		TextField verticesInput = new TextField();
		verticesInput.setPromptText("Type in an integer here");
		GridPane.setConstraints(verticesInput, 1, 1);
		
		Button enter = new Button("Enter");
		enter.setOnAction(e -> verticesIsInt(verticesInput, window));
		GridPane.setConstraints(enter, 1, 3);

		verticesGrid.setAlignment(Pos.CENTER);
		verticesGrid.getChildren().addAll(typeInV, numOfVertices, verticesInput, enter);

		HBox topBar = new HBox();
		Button back = new Button("Go back");
		back.setOnAction(e -> window.setScene(GenerateGraphScene.generateGraphScene(window)));
		Button rules = new Button("Rules");
		rules.setOnAction(e -> RulesBox.display());
		topBar.getChildren().addAll(back, rules);
		topBar.setPadding(new Insets(2,2,2,2));
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topBar);
		borderPane.setCenter(verticesGrid);
		borderPane.getStyleClass().add("MainScene-background");

		return new Scene(MenuScene.createBasicParent(borderPane), 1280, 720);
	}
	
	/**
	 * Creates the scene in which the user selects the number of edges
	 * @param window, the base stage 
	 * @return scene, the created scene
	 */
	public static Scene createEdgesScene(Stage window) {
		GridPane edgesGrid = new GridPane();
		edgesGrid.setPadding(new Insets(0, 10, 0, 10));
		edgesGrid.setVgap(10);
		edgesGrid.setHgap(10);
		
		Label typeInE = new Label("Please type in the number of edges that you want to have");
		GridPane.setConstraints(typeInE, 0, 0);
		
		Label numOfEdges = new Label("Number of edges : ");
		GridPane.setConstraints(numOfEdges, 0, 1);
		
		TextField edgesInput = new TextField();
		edgesInput.setPromptText("Type in an integer here");
		GridPane.setConstraints(edgesInput, 1, 1);

		Button enter = new Button("Enter");
		enter.setOnAction(e -> edgesIsInt(edgesInput, window));
		GridPane.setConstraints(enter, 1, 3);

		edgesGrid.setAlignment(Pos.CENTER);
		edgesGrid.getChildren().addAll(typeInE, numOfEdges, edgesInput, enter);

		HBox topBar = new HBox();
		Button back = new Button("Go back");
		back.setOnAction(e -> window.setScene(GenerateGraphScene.generateGraphScene(window)));
		Button rules = new Button("Rules");
		rules.setOnAction(e -> RulesBox.display());
		topBar.getChildren().addAll(back, rules);
		topBar.setPadding(new Insets(2,2,2,2));
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topBar);
		borderPane.setCenter(edgesGrid);

		return new Scene(MenuScene.createBasicParent(borderPane), 1280, 720);
	}
	/**
	 * Saves the entered number of vertices in the numberOfVertices variable and the entered number of edges in the numberOfEdges variable
	 * @param window, the base stage 
	 * @param input1, the entered number of vertices
	 * @param input2, the entered number of edges
	 * @throws NumberFormatException if the user enters something else than an integer
	 */
	
	private static boolean verticesAndEdgesAreInt(TextField input1, TextField input2, Stage window) {
		
		try {
			
			numberOfVertices = Integer.parseInt(input1.getText());
			numberOfEdges = Integer.parseInt(input2.getText());
			System.out.println("Number of vertices = " + numberOfVertices);
			System.out.println("Number of edges = " + numberOfEdges);
			window.setScene(DifficultyScene.createDifficultyScene(window));
			return true;
			
		} catch(NumberFormatException e) {
			AlertBox.display();
			return false;
		}
	}
	
	/**
	 * Saves the entered number of vertices in the numberOfVertices variable 
	 * @param window, the base stage 
	 * @param input1, the entered number of vertices
	 * @throws NumberFormatException if the user enters something else than an integer
	 */
	private static boolean verticesIsInt(TextField input1, Stage window) {
		
		try {
			
			numberOfVertices = Integer.parseInt(input1.getText());
			System.out.println("Number of vertices = " + numberOfVertices);
			window.setScene(DifficultyScene.createDifficultyScene(window));
			
			return true;
			
		} catch(NumberFormatException e) {
			AlertBox.display();
			return false;
		}
	}
	
	/**
	 * Saves the entered number of edges in the numberOfEdges variable
	 * @param window, the base stage 
	 * @param input1, the entered number of edges
	 * @throws NumberFormatException if the user enters something else than an integer
	 */
	
	private static boolean edgesIsInt(TextField input1, Stage window) {
		
		try {
			
			numberOfEdges = Integer.parseInt(input1.getText());
			System.out.println("Number of edges = " + numberOfEdges);
			window.setScene(DifficultyScene.createDifficultyScene(window));
			return true;
			
		} catch(NumberFormatException e) {
			AlertBox.display();
			return false;
		}
	}
	

}

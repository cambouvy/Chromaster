package edu.um.chromaster.gui.menu;

/**
 * Class containing 3 boolean parameters that keeps track of the choice of the way the graph is generated
 * @param random, true if user wants to play with a random graph, false otherwise
 * @param vertices, true if user wants to select the number of vertices only , false otherwise
 * @param edges, true if user wants to select the number of edges only, false otherwise
 * @param verticesAndEdges, true if user wants to select the number of edges and the number of vertices, false otherwise
 */

public class ChosenVerticesOrEdges {
	
	public static boolean random;
	public static boolean vertices;
	public static boolean edges;
	public static boolean verticesAndEdges;
	public static boolean readIn;
	
	public ChosenVerticesOrEdges() {
	}

}

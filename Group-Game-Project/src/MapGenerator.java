import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MapGenerator {
	private LinkedHashMap<String, Node> graph; // pathfinding grid

	private ArrayList<Node> tiles;
	private Image floor, wall, goal;
	private int numOfXTiles, numOfYTiles;
	private int numOfGoals = 0;
	private int numOfCrates;
	private boolean isValid;

	public MapGenerator(int x, int y, int numOfCrates) {
		this.numOfXTiles = x;
		this.numOfYTiles = y;
		this.numOfCrates = numOfCrates;

		isValid = false;

		// keep generating until we get a valid map
		makeTiles(x, y);
		System.out.println("Generating Map, please wait...");

		// commence map generation
		clearTiles(this.tiles);
		placeBorders();
		generateMaze(this.tiles, x); // 2nd parameter can be x or y, doesn't
		// matter

		// make x amount of goals according to x amount of crates
		while (this.numOfGoals < this.numOfCrates) {
			makeGoals(x, y);
			this.numOfGoals++;
		}

		if (this.numOfCrates == 1) {
			easyNoWalls(x, y);
		}

		// create a grid map
		makeGraph();
		addEdges();

		if (checkInaccessibleRooms() == false) {
			isValid = true;
		} else {
			isValid = false;
		}
	String s = print(tiles);System.out.println(s);
	}

	public boolean isValid() {
		return isValid;
	}

	public void easyNoWalls(int x, int y) {
		for (Node n : tiles) {
			if (n.getWall() == true && n.getX() != 0 || n.getY() != 0 || n.getX() != x || n.getY() != y) {
				n.unsetWall();
				n.setChar('-');
			}
		}
		placeBorders();
	}

	/**
	 * Check for connectivity Every valid map must contain one contiguous
	 * section of floor also checks for inaccessible rooms
	 * 
	 * @return true if inaccessible room found, otherwise false
	 */
	public boolean checkInaccessibleRooms() {
		// use Floyd-Warshall to ensure generated maps contain no inaccessible
		// rooms
		List<String> mapLocationsAndIndex = new ArrayList<String>();
		int V = this.graph.size();
		int[][] shortestDist = new int[V][V];
		String[][] shortestPaths = new String[V][V];
		floydWarshall(this.graph, mapLocationsAndIndex, shortestDist, shortestPaths);
		boolean inaccessibleRoomFound = false;

		// check for any tile that is inaccessible
		for (int i = 0; i < V; i++) {
			for (int j = 0; j < V; j++) {
				if (i != j && shortestPaths[i][j] == null) {
					inaccessibleRoomFound = true;
					break;
				}
			}
			if (inaccessibleRoomFound == true) {
				break;
			}
		}

		return inaccessibleRoomFound;
	}

	/**
	 * Make a graph of the current map
	 */
	public void makeGraph() {
		graph = new LinkedHashMap<String, Node>();
		for (Node n : this.tiles) {
			if (n.getWall() == false) {
				String coordinates = n.getX() + "," + n.getY(); // x,y
				graph.put(coordinates, n);
			}
		}
	}

	/**
	 * create a pathfinding grid using the current graph
	 */
	public void addEdges() {
		Node adjacentNode;
		Edge e;
		for (Node n : this.graph.values()) {
			int adjacentUp = n.getY() - 1;
			int adjacentDown = n.getY() + 1;
			int adjacentLeft = n.getX() - 1;
			int adjacentRight = n.getX() + 1;

			// System.out.println("current node: " + n.getX() + "," + n.getY());

			// check boundary
			// check wall
			// add edge

			if (adjacentUp >= 0) {
				if ((adjacentNode = getNode(n.getX(), adjacentUp)).getWall() == false) {
					e = new Edge(n, adjacentNode);
					n.addIncidentEdge(e);
				}
			}

			if (adjacentDown <= numOfYTiles - 1) {
				if ((adjacentNode = getNode(n.getX(), adjacentDown)).getWall() == false) {
					e = new Edge(n, adjacentNode);
					n.addIncidentEdge(e);
				}
			}

			if (adjacentLeft >= 0) {
				if ((adjacentNode = getNode(adjacentLeft, n.getY())).getWall() == false) {
					e = new Edge(n, adjacentNode);
					n.addIncidentEdge(e);
				}
			}

			if (adjacentRight <= numOfXTiles - 1) {
				if ((adjacentNode = getNode(adjacentRight, n.getY())).getWall() == false) {
					e = new Edge(n, adjacentNode);
					n.addIncidentEdge(e);
				}
			}
		}
	}

	/**
	 * initially set up tiles to all be of type empty
	 * 
	 * @param x:
	 *            numOfXTiles
	 * @param y:
	 *            numOfYTiles
	 */
	public void makeTiles(int x, int y) {
		tiles = new ArrayList<Node>();
		for (int a = 0; a < x; a++) {
			for (int b = 0; b < y; b++) {
				String coordinate = a + "," + b;
				Node node = new Node(a, b, '-', coordinate);
				tiles.add(node);
			}
		}
	}

	/**
	 * Clear all tiles within map
	 * 
	 * @param tiles:
	 *            tile array list
	 */
	public void clearTiles(ArrayList<Node> tiles) {
		for (Node n : tiles) {
			n.setChar('-');
			n.unsetCrate();
			n.unsetGoal();
			n.unsetWall();
		}
	}

	public void generateMaze(ArrayList<Node> tiles, int dimensions) {
		// 17 different templates
		// each template has 4 different variations (flipped/rotated)
		String[] templates = { "---\n---\n---", "*--\n---\n---", "**-\n---\n---", "***\n---\n---", "***\n*--\n*--",
				"*--\n---\n--*", "*--\n---\n*-*", "*--\n---\n*-*", "*-*\n---\n*-*", "*-*\n*--\n***", "***\n---\n***",
				"---\n-*-\n---", "***\n***\n***", "***\n*--\n---", "---\n*-*\n---", "***\n***\n---", "***\n-*-\n---" };

		final int numOfTemplates = templates.length;
		final int numOfVariations = 4;
		final int blockDimensions = 3;
		// number of 3x3 block/s on the map
		int trueDimension = dimensions - 2;

		int selectedTemplate, selectedVariation;
		ArrayList<String> currentTemplate;
		char[][] templateMatrix;

		// partition map into 3x3 blocks
		for (int by = 1; by < trueDimension; by += blockDimensions) {
			for (int bx = 1; bx < trueDimension; bx += blockDimensions) {
				selectedTemplate = (int) (Math.random() * numOfTemplates);
				selectedVariation = (int) (Math.random() * numOfVariations);

				currentTemplate = new ArrayList<String>(Arrays.asList(templates[selectedTemplate].split("")));
				templateMatrix = new char[blockDimensions][blockDimensions]; // need
				// to
				// copy
				// to
				// a
				// 2D
				// matrix
				// to
				// perform
				// rotation

				// copy the chosen template onto the char template matrix
				int index = 0;
				for (int y = 0; y < blockDimensions; y++) {
					for (int x = 0; x < blockDimensions; x++) {
						templateMatrix[x][y] = currentTemplate.get(index).charAt(0);
						index++;
					}
					index++; // skip the new line
				}

				// rotate matrix n(selectedVariation) times
				for (int i = 0; i < selectedVariation; i++) {
					rotateMatrixClockwise(templateMatrix);
				}

				// System.out.println("ROTATED MATRIX");
				// printMatrix(templateMatrix);
				// System.out.println();

				int j = 0;
				int i = 0;
				// go through each block to insert template
				for (int y = by; y < by + blockDimensions; y++) {
					for (int x = bx; x < bx + blockDimensions; x++) {
						char c = getCharFromTemplate(i, j, templateMatrix);
						i++;
						if (c == '*') {
							getNode(x, y).setWall();
							;
						}
						getNode(x, y).setChar(c);
						// System.out.print(x + "," + y);
						// System.out.print(" " + c + " ");

					}
					// System.out.println();
					j++;
					i = 0;
				}
				// System.out.println();
				// System.out.println();
			}
		}
	}

	/**
	 * Search for and get a specific character from the template
	 * 
	 * @param x:
	 *            x coordinate for character requested
	 * @param y:
	 *            y coordinate for character requested
	 * @param template:
	 *            2D matrix array of template
	 * @return requested character from template
	 */
	public char getCharFromTemplate(int x, int y, char[][] template) {
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 3; i++) {
				if (i == x && j == y) {
					return template[i][j];
				}
			}
		}
		return 'a';
	}

	/**
	 * rotate the 3x3 template matrix
	 * 
	 * @param templateMatrix
	 */
	// taken from:
	// http://javabypatel.blogspot.com.au/2016/11/rotate-matrix-by-90-degrees-inplace.html
	public void rotateMatrixClockwise(char[][] templateMatrix) {
		int length = templateMatrix.length - 1;

		for (int i = 0; i <= (length) / 2; i++) {
			for (int j = i; j < length - i; j++) {
				// Coordinate 1
				char p1 = templateMatrix[i][j];

				// Coordinate 2
				char p2 = templateMatrix[j][length - i];

				// Coordinate 3
				char p3 = templateMatrix[length - i][length - j];

				// Coordinate 4
				char p4 = templateMatrix[length - j][i];

				// Swap values of 4 coordinates.
				templateMatrix[j][length - i] = p1;
				templateMatrix[length - i][length - j] = p2;
				templateMatrix[length - j][i] = p3;
				templateMatrix[i][j] = p4;
			}
		}
	}

	/**
	 * print the 3x3 template according to the game's coordinate system e.g. 0,0
	 * starts at the upper left instead of the lower left 3,3 is positioned at
	 * the lower right instead of the upper right
	 * 
	 * @param templateMatrix
	 */
	public void printMatrix(char[][] templateMatrix) {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				System.out.print(y + "," + x + ":");
				System.out.print(templateMatrix[x][y] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Place walls to block off map boundaries
	 * 
	 * @param o
	 * @param xF
	 * @param yF
	 */
	public void placeBorders() {
		// Creating border walls.
		for (Node n : tiles) {
			if (n.getX() == 0 || n.getY() == 0) {
				n.setChar('*');
				n.setWall();
			} else if (n.getX() == this.numOfXTiles - 1 || n.getY() == this.numOfYTiles - 1) {
				n.setChar('*');
				n.setWall();
			}
		}
	}

	/**
	 * randomly generate goal tiles
	 * 
	 * @param x:
	 *            numOfXTiles
	 * @param y:
	 *            numOfYTiles
	 */
	public void makeGoals(int x, int y) {
		boolean setGoal = false;
		while (setGoal == false) {
			// choose a random coordinate value of x and y
			// and place a goal point at said coordinate
			int positionX = (int) (Math.random() * x + 1);
			int positionY = (int) (Math.random() * y + 1);
			for (Node n : this.tiles) {
				if (n.getX() == positionX && n.getY() == positionY) {
					if (n.getC() == '*') {
						break;
					} else {
						n.setGoal();
						setGoal = true;
						break;
					}
				}
			}
		}
	}

	/**
	 * Calculate solvable positions for starting crate positions, by iteratively
	 * expanding outwards from a goal position while avoiding unsolvable
	 * starting crate positions and walls.
	 * 
	 * @param goal
	 *            the goal square to expand outwards from
	 * @return the set of farthest, valid nodes reached in the iterative
	 *         expansion
	 */
	public ArrayList<Node> calculateGoalRange(Node goal) {

		if (!goal.getGoal())
			return null;

		// The resulting set of nodes expanded to by the last iteration
		ArrayList<Node> resultSet = new ArrayList<Node>();
		// The previous resulting set of the iteration before last
		ArrayList<Node> prevSet = new ArrayList<Node>();
		// The initial node in the expansion is the goal

		// Start the expansion at the goal position
		resultSet.add(goal);

		while (true) {
			prevSet = resultSet;
			resultSet = getNextDepth(resultSet);
			if (resultSet.isEmpty())
				break;
		}
		return prevSet;
	}

	/**
	 * Helper method for calculateGoalRange that expands the current set of
	 * nodes by a depth level and removes from the resulting set the nodes of
	 * the previous depth iteration such that only the nodes at the frontier of
	 * the expansion are returned in the resulting set.
	 *
	 * @param prevResults
	 *            the resulting set of the previous depth iteration
	 * @return the resulting set of nodes at the frontier of this expansion
	 */
	private ArrayList<Node> getNextDepth(ArrayList<Node> prevResults) {

		// The set of nodes reached at only this current expansion iteration
		ArrayList<Node> resultSet = new ArrayList<Node>();
		// TODO
		// resultSet = expandNode(prevResults);
		resultSet.removeAll(prevResults);

		return resultSet;
	}

	/**
	 * linear search for a node
	 * 
	 * @param x:
	 *            graph x coordinate
	 * @param y:
	 *            graph y coordinate
	 * @return Node
	 */
	public Node getNode(int x, int y) {
		Node rN = null;
		for (Node z : tiles) {
			if (z.getX() == x && z.getY() == y) {
				rN = z;
				break;
			}
		}
		return rN;
	}

	/**
	 * print all nodes
	 * 
	 * @param n
	 * @return a string which is used to display an ASCII representation of the
	 *         generated map
	 */
	public String print(ArrayList<Node> n) {
		String s = "";
		s += "\n";
		int x = 0;
		Node temp;

		for (int i = 0; i < this.numOfXTiles; i++) {
			for (Node node : n) {
				if (node.getX() == x) {
					temp = getNode(node.getY(), i);
					s += "   " + temp.getC();
				} else {
					break;
				}
				x = node.getX();
			}
			s += "\n";

		}

		return s;
	}

	/**
	 * Print the generated map onto the frame with graphics
	 * 
	 * @param g2D
	 */
	public void paintMap(Graphics2D g2D, int FW, int FH) {
		ImageIcon img;
		img = new ImageIcon("resource/Images/Floor.jpg");
		floor = img.getImage();
		img = new ImageIcon("resource/Images/Wall.jpg");
		wall = img.getImage();
		img = new ImageIcon("resource/Images/greenX.png");
		goal = img.getImage();

		int WD = (FW / this.numOfXTiles);
		int HD = (FH / this.numOfYTiles);

		for (Node n : tiles) {
			if (n.getC() == '-' && n.getGoal() == false) {
				g2D.drawImage(floor, n.getX() * WD, n.getY() * HD, WD, HD, null);
			} else if (n.getC() == '*' && n.getGoal() == false) {
				g2D.drawImage(wall, n.getX() * WD, n.getY() * HD, WD, HD, null);
			} else if (n.getGoal() == true) {
				g2D.drawImage(floor, n.getX() * WD, n.getY() * HD, WD, HD, null);
				g2D.drawImage(goal, n.getX() * WD, n.getY() * HD, WD, HD, null);
			}
		}

	}

	public ArrayList<Node> getTilesArray() {
		return this.tiles;
	}

	// -----------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------------------------------

	public void floydWarshall(LinkedHashMap<String, Node> map, List<String> mapLocationsAndIndex, int[][] shortestDist,
			String[][] shortestPaths) {
		int infinite = 99999;
		int V = map.size();
		int i = 0;
		int j = 0;

		// get all locations and their corresponding index
		// store them into an array
		for (Node n : map.values()) {
			String identifier = n.getX() + "," + n.getY();
			mapLocationsAndIndex.add(identifier);
		}

		// set up initial state of infinite and zeros for 2D array
		// according to information stored in the map
		for (int row = 0; row < V; row++) {
			for (int col = 0; col < V; col++) {
				if (row != col) {
					shortestDist[row][col] = infinite;
					shortestPaths[row][col] = null;
					// else a node going to itself will be of 0 weight
				} else {
					shortestDist[row][col] = 0;
					shortestPaths[row][col] = null;
				}
			}
		}

		// set up initial values for 2D array
		// according to information stored in the map
		for (Node n : map.values()) {
			for (Edge e : n.getIncidentEdges()) {
				String destinationLocation = e.getDestLocation().getIdentifier();
				for (Node nIndex : map.values()) {
					if (destinationLocation.equals(nIndex.getIdentifier())) {
						shortestDist[i][j] = e.getCost();
						shortestPaths[i][j] = n.getIdentifier();
					}
					j++;
				}
				j = 0;
			}
			i++;
		}

		// printGraph(shortestDist, shortestPaths, map, mapLocationsAndIndex,
		// 0);
		// printGraph(shortestDist, shortestPaths, map, mapLocationsAndIndex,
		// 1);
		getShortestDist(V, shortestDist, shortestPaths);
		// printGraph(shortestDist, shortestPaths, map, mapLocationsAndIndex,
		// 0);
		// printGraph(shortestDist, shortestPaths, map, mapLocationsAndIndex,
		// 1);

	}

	// compute shortest distance between every Vertex to every other Vertex
	public void getShortestDist(int graphSize, int[][] distGraph, String[][] pathGraph) {
		int V = graphSize;
		int x, y, k;
		// k represents the pitstop (intermediary location) we take from source
		// to destination
		for (k = 0; k < V; k++) {
			// Pick all vertices as source one by one
			for (x = 0; x < V; x++) {
				// Pick all vertices as destination for the
				// above picked source
				for (y = 0; y < V; y++) {
					// If intermediary vertex k is on the shortest path from
					// x to y, then update the value of graph[x][y]
					if (distGraph[x][y] > distGraph[x][k] + distGraph[k][y]) {
						distGraph[x][y] = distGraph[x][k] + distGraph[k][y];
						pathGraph[x][y] = pathGraph[k][y];
					}
				}
			}
		}
	}

	// print out graph in matrix form (2D array :D)
	// mainly used for debugging
	public void printGraph(int[][] distGraph, String[][] pathGraph, LinkedHashMap<String, Node> map,
			List<String> mapLocationsAndIndex, int flag) {

		// flag - 0: printing shortest distances
		// flag - 1: printing shortest paths

		int infinite = 99999;
		int V = map.size();
		// int index = 0;

		System.out.printf("\n");

		/************************************************************
		 **************** PRINTING TABLE COMMENCE *******************
		 ************************************************************/
		// print extra spaces at the front of the first row
		System.out.printf("%-18s", "");
		// print x-axis labels (row labels)
		for (int index = 0; index < mapLocationsAndIndex.size(); index++) {
			System.out.printf("%-15s", mapLocationsAndIndex.get(index));
		}
		System.out.printf("\n");

		// estimate how many hyphens will be needed based on the number of
		// labels (locations)
		// "+ 20" compensates for the extra spaces at the front of the first row
		int maxEstimateNumHyphens = (mapLocationsAndIndex.size() * 15) + 20;
		for (int i = mapLocationsAndIndex.size(); i < maxEstimateNumHyphens; i++)
			System.out.printf("%s", "-");
		System.out.printf("\n");

		// print out the values for corresponding row and column
		for (int row = 0; row < V; row++) {
			System.out.printf("%-15s | ", mapLocationsAndIndex.get(row));
			for (int col = 0; col < V; col++) {
				if (flag == 0) {
					if (distGraph[row][col] == infinite) {
						System.out.printf("%-15s", "INF");
					} else {
						System.out.printf("%-15d", distGraph[row][col]);
					}
				} else {
					if (pathGraph[row][col] == null) {
						System.out.printf("%-15s", "-");
					} else {
						System.out.printf("%-15s", pathGraph[row][col]);
					}
				}
			}
			System.out.printf("\n");
		}
	}

	public boolean getLocationOfGoal(MapGenerator mg) {
		ArrayList<Node> t = mg.getTilesArray();
		int crateGoalCounter = 0;
		boolean r = false;
		for (Node n : t) {
			if (n.getGoal() == true && n.getCrate() != true) {
				System.out.println("Goal = X = " + n.getX() + " and Y = " + n.getY());
			} else if (n.getCrate() == true && n.getGoal() != true) {
				System.out.println("Crate = X = " + n.getX() + " and Y = " + n.getY());
			} else if (n.getCrate() == true && n.getGoal() == true) {
				crateGoalCounter++;
			}
		}

		if (crateGoalCounter == mg.numOfCrates) {
			System.out.println("THIS IS THE WIN CONDITION");
			r = true;
		}

		return r;
	}
}

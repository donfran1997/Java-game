import java.util.ArrayList;

public class Node {
	// these coordinates refer to graph coordinates
	// and NOT pixel coordinates
	private int x;         				    // X coordinate
	private int y;         					// Y coordinate
	private char c;		   					// can be either '*' or '-' (wall or empty)
	private boolean wall;  					// wall on this tile?
	private boolean crate; 					// crate on this tile?
	private boolean goal;  					// goal on this tile?
	private ArrayList<Edge> incidentEdges;  // every adjacent edge
	private String identifier;				// coordinates in string (x,y) 
	
	/*
	 * X and Y are the coordinates.
	 * c is the value (wall or not)
	 */
	public Node(int x, int y, char c, String identifier) {
		this.x = x;
		this.y = y;
		this.c = c;
		this.identifier = identifier;
		this.incidentEdges = new ArrayList<Edge>();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public char getC() {
		return c;
	}
	
	public void setChar(char nc) {
		this.c = nc;
	}
	
	public void setCrate() {
		this.crate = true;
	}
	
	public void unsetCrate() {
		this.crate = false;
	}
	
	public boolean getCrate() {
		return this.crate;
	}
	
	public void setGoal() {
		this.goal = true;
	}
	
	public void unsetGoal() {
		this.goal = false;
	}
	
	public boolean getGoal() {
		return this.goal;
	}
	
	public void setWall() {
		this.wall = true;
	}
	
	public void unsetWall() {
		this.wall = false;
	}
	
	public boolean getWall() {
		return this.wall;
	}
	
	public ArrayList<Edge> getIncidentEdges() {
		return this.incidentEdges;
	}
	
	public void addIncidentEdge(Edge e) {
		this.incidentEdges.add(e);
	}
	
	public String getIdentifier() {
		return this.identifier;
	}

}

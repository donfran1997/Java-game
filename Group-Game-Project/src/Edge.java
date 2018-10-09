/**
 * This class is the internal representation of an edge for Graph
 * implementations
 *
 */
public class Edge {
	private Node from;
	private Node to;
	private int cost;
	
	/**
	* Created a directed edge to between two given node and a 
	* cost
	* @param s this is the node from 
	* @param d this is the node to
	*/
	public Edge(Node s, Node d) {
		this.from = s;
		this.to = d;
		cost = 1;
	}

	public Node getSourceLocation() {
		return this.from;
	}
	
	public Node getDestLocation() {
		return this.to;
	}
	
	public int getCost() {
		return cost;
	}
	
}

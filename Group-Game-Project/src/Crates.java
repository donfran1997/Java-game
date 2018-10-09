import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * This class is responsible for generation of crates on the map
 * along with movement of the crates
 *
 */

public class Crates {
    private Point2D crateLocation;
    private MapGenerator mg;
    private int xLocation;
    private int yLocation;
	
    /**
     * This method is used for checking the map to find where a valid crate
     * can be generated. Then it places the crate in that location.
     * 
     * @param map this is an arraylist of the node in the map
     * @param mg this is a mapgenerator
     * @param BD this is box dimension
     * @param mapWidth this is width of the map
     * @param mapHeight this is the height of the map
     */
    public Crates(ArrayList<Node> map, MapGenerator mg, int BD, int mapWidth, int mapHeight) {
    	int positionX, positionY;
    	int adjUp, adjDown, adjLeft, adjRight;
    	boolean spawnedCrate = false;
        this.mg = mg;

    	// choose a random coordinate value and spawn a crate there
    	while (spawnedCrate == false) {
        	positionX = (int) (Math.random() * mapWidth);
        	positionY = (int) (Math.random() * mapHeight);
        	
        	// check if crate is placed in a valid position
        	int numOfAdjacentWall = 0;
        	int numOfSideBySideWallUpDown = 0;
        	int numOfSideBySideWallLeftRight = 0;
        	
        	adjUp = positionY-1;
        	adjDown = positionY+1;
        	adjLeft = positionX-1;
        	adjRight = positionX+1;
        	if (adjUp >= 0) {
        		if (mg.getNode(positionX, adjUp).getWall() == true) {
        			numOfAdjacentWall++;
        			numOfSideBySideWallUpDown++;
        		}
        	}
        	if (adjDown <= mapHeight-1) {
        		if (mg.getNode(positionX, adjDown).getWall() == true) {
        			numOfAdjacentWall++;
        			numOfSideBySideWallUpDown++;
        		}
        	}
        	if (adjLeft >= 0) {
        		//System.out.println(adjLeft + "," + positionY);
        		if (mg.getNode(adjLeft, positionY).getWall() == true) {
        			numOfAdjacentWall++;
        			numOfSideBySideWallLeftRight++;
        		}
        	}
        	if (adjRight <= mapWidth-1) { 
        		//System.out.println(adjRight + "," + positionY);
        		if (mg.getNode(adjRight, positionY).getWall() == true) {
        			numOfAdjacentWall++;
        			numOfSideBySideWallLeftRight++;
        		}
        	}
        	if (numOfAdjacentWall > 1) {
        		if (!((numOfSideBySideWallUpDown == 2 && numOfAdjacentWall == 2) ||
        			(numOfSideBySideWallLeftRight == 2 && numOfAdjacentWall == 2))) {
        			continue;
        		} 
        	}
        	
    		for (Node n : map) {
    			if (n.getX() == positionX && n.getY() == positionY) {
    				// check if position is a non-walled/non-crate/non-player/non-goal tile
	    			if (n.getWall() == true) {
	    				break;
	    			} else if (n.getCrate() == true) {
	    				break;
	    			} else if (n.getGoal() == true) {
	    				break;
	    			} else {	    
	    		        crateLocation = new Point2D.Double(n.getX() * BD, n.getY() * BD);
	    		        this.xLocation = n.getX(); this.yLocation = n.getY();
	    		        n.setCrate();
	    		        spawnedCrate = true;
	    		        break;
	    			}
    			}
    		}
    	}
    }
    
    public void setCrateLocation(Point2D newLocation) {
        this.crateLocation = newLocation;
    }
    
    public Point2D getCrateLocation() {
        return this.crateLocation;
    }
    
    public int getCrateXCoordinate() {
    	return this.xLocation;
    }
    
    public int getCrateYCoordinate() {
    	return this.yLocation;
    }
    
    /**
     * move crate up
     * @param BD
     * @return true if crate is movable, otherwise false
     */
    public boolean moveUp(int BD) {
        Point2D moveTo = new Point2D.Double(getCrateLocation().getX(), 
                							getCrateLocation().getY() - BD);
		this.yLocation -= 1;
		// no obstructions, box can move
		if (this.mg.getNode(xLocation, yLocation).getWall() == false &&
			this.mg.getNode(xLocation, yLocation).getCrate() == false) {
			setCrateLocation(moveTo);
			this.mg.getNode(xLocation, yLocation+1).unsetCrate();
			this.mg.getNode(xLocation, yLocation).setCrate();
			return true;
		// else box cannot move
		} else {
			this.yLocation += 1;
			return false;
		}
        
        //System.out.println(getCrateLocation().getX() + ", " + getCrateLocation().getY());
    }
    
    /**
     * move crate down
     * @param BD
     * @return true if crate is movable, otherwise false
     */
    public boolean moveDown(int BD) {
        Point2D moveTo = new Point2D.Double(getCrateLocation().getX(), 
        									getCrateLocation().getY() + BD);
		this.yLocation += 1;
		// no obstructions, box can move
		if (this.mg.getNode(xLocation, yLocation).getWall() == false &&
			this.mg.getNode(xLocation, yLocation).getCrate() == false) {
			setCrateLocation(moveTo);
			this.mg.getNode(xLocation, yLocation-1).unsetCrate();
			this.mg.getNode(xLocation, yLocation).setCrate();
			return true;
		// else box cannot move
		} else {
			this.yLocation -= 1;
			return false;
		}
        //System.out.println(getCrateLocation().getX() + ", " + getCrateLocation().getY());
    }
    
    /**
     * move crate left
     * @param BD
     * @return true if crate is movable, otherwise false
     */
    public boolean moveLeft(int BD) {
        Point2D moveTo = new Point2D.Double(getCrateLocation().getX() - BD, 
                							getCrateLocation().getY());
		this.xLocation -= 1;
		// no obstructions, box can move
		if (this.mg.getNode(xLocation, yLocation).getWall() == false &&
			this.mg.getNode(xLocation, yLocation).getCrate() == false) {
			setCrateLocation(moveTo);
			this.mg.getNode(xLocation+1, yLocation).unsetCrate();
			this.mg.getNode(xLocation, yLocation).setCrate();
			return true;
		// else box cannot move
		} else {
			this.xLocation += 1;
			return false;
		}
        //System.out.println(getCrateLocation().getX() + ", " + getCrateLocation().getY());
    }
    
    /**
     * move crate right
     * @param BD
     * @return true if crate is movable, otherwise false
     */
    public boolean moveRight(int BD) {
        Point2D moveTo = new Point2D.Double(getCrateLocation().getX() + BD, 
                							getCrateLocation().getY());
		this.xLocation += 1;
		// no obstructions, box can move
		if (this.mg.getNode(xLocation, yLocation).getWall() == false &&
			this.mg.getNode(xLocation, yLocation).getCrate() == false) {
			setCrateLocation(moveTo);
			this.mg.getNode(xLocation-1, yLocation).unsetCrate();
			this.mg.getNode(xLocation, yLocation).setCrate();
			return true;
		// else box cannot move
		} else {
			this.xLocation -= 1;
			return false;
		}
        //System.out.println(getCrateLocation().getX() + ", " + getCrateLocation().getY());
    }
    
}

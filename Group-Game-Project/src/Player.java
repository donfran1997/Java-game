import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Player class which holds information of player location.
 */
public class Player {
    private ArrayList<Crates> crates;
    private Point2D playerLocation;
    private int xLocation;
    private int yLocation;
    private MapGenerator mg;

    public Player(ArrayList<Node> map, int BD, MapGenerator mg, ArrayList<Crates> c) {
    	this.crates = c;
    	this.mg = mg;
    	// spawn the player on a non-walled/non-crate/non-goal tile
    	for (Node n : map) {
    		//System.out.println("initial POS: " + n.getX() + " " + n.getY() + " " + n.getC());
    		if (n.getWall() == true) {
    			continue;
    		} else if (n.getGoal()) {
    			continue;
    		} else if (n.getCrate() == true) {
    			continue;
    		} else {
    			this.xLocation = n.getX(); this.yLocation = n.getY();
    	        playerLocation = new Point2D.Double(n.getX() * BD, n.getY() * BD);
    	        break;
    		}
    	}
    	
		System.out.println(playerLocation);
    }
    
    public void setPlayerLocation(Point2D newLocation) {
        playerLocation = newLocation;
    }
    
    public Point2D getPlayerLocation() {
        return playerLocation;
    }
    
    public int getPlayerXCoordinate() {
    	return this.xLocation;
    }
    
    public int getPlayerYCoordinate() {
    	return this.yLocation;
    }
    
    /**
     * move player character up
     * @param BD: boxDimension
     */
    public void moveUp(int BD) {
        Point2D moveTo = new Point2D.Double(getPlayerLocation().getX(), 
                							getPlayerLocation().getY() - BD);
        boolean crateMoved = false;  // is the box movable?
		this.yLocation -= 1;
		// wall collision detection
		if (this.mg.getNode(xLocation, yLocation).getWall() == false) {
			// check if there is a crate on the tile we want to move to
			if (this.mg.getNode(xLocation, yLocation).getCrate() == true) {
				System.out.println("Encountered Crate");
				// check if crate is movable and if so move it
				for (Crates c : this.crates) {
					if (c.getCrateXCoordinate() == xLocation && 
						c.getCrateYCoordinate() == yLocation) {
						crateMoved = c.moveUp(BD);
						break;
					}
				}
				
				// crate has been Moved
				if (crateMoved == true) {
					setPlayerLocation(moveTo);
					//System.out.println("boxCoordinate: " + this.xLocation +  " " + this.yLocation);
				} else {
					this.yLocation += 1;
				}
			// else there are no obstructions
			} else {
				setPlayerLocation(moveTo);
			}			
		} else {
			System.out.println("Blocked by wall");
			this.yLocation += 1;
		}
		System.out.println(getPlayerLocation().getX() + ", " + getPlayerLocation().getY());
		System.out.println("Player Coordinate: x = " + getPlayerXCoordinate() + ", y = " + getPlayerYCoordinate());
    }
    
    /**
     * move player character down
     * @param BD: boxDimension
     */
    public void moveDown(int BD) {
        Point2D moveTo = new Point2D.Double(getPlayerLocation().getX(), 
        									getPlayerLocation().getY() + BD);
        boolean crateMoved = false;  // is the box movable?
		this.yLocation += 1;
		// wall collision detection
		if (this.mg.getNode(xLocation, yLocation).getWall() == false) {
			// check if there is a crate on the tile we want to move to
			if (this.mg.getNode(xLocation, yLocation).getCrate() == true) {
				System.out.println("Encountered Crate");
				// check if crate is movable and if so move it
				for (Crates c : this.crates) {
					if (c.getCrateXCoordinate() == xLocation && 
						c.getCrateYCoordinate() == yLocation) {
						crateMoved = c.moveDown(BD);
						break;
					}
				}
				
				// crate has been Moved
				if (crateMoved == true) {
					setPlayerLocation(moveTo);
					//System.out.println("boxCoordinate: " + this.xLocation +  " " + this.yLocation);
				} else {
					this.yLocation -= 1;
				}
			} else {
				setPlayerLocation(moveTo);
			}
		} else {
			System.out.println("Blocked by wall");
			this.yLocation -= 1;
		}
        System.out.println(getPlayerLocation().getX() + ", " + getPlayerLocation().getY());
		System.out.println("Player Coordinate: x = " + getPlayerXCoordinate() + ", y = " + getPlayerYCoordinate());

    }
    
    /**
     * move player character left
     * @param BD: boxDimension
     */
    public void moveLeft(int BD) {
        Point2D moveTo = new Point2D.Double(getPlayerLocation().getX() - BD, 
                							getPlayerLocation().getY());
        boolean crateMoved = false;  // is the box movable?
		this.xLocation -= 1;
		// wall collision detection
		if (this.mg.getNode(xLocation, yLocation).getWall() == false) {
			// check if there is a crate on the tile we want to move to
			if (this.mg.getNode(xLocation, yLocation).getCrate() == true) {
				System.out.println("Encountered Crate");
				// check if crate is movable and if so move it
				for (Crates c : this.crates) {
					if (c.getCrateXCoordinate() == xLocation && 
						c.getCrateYCoordinate() == yLocation) {
						crateMoved = c.moveLeft(BD);
						break;
					}
				}
				
				// crate has been Moved
				if (crateMoved == true) {
					setPlayerLocation(moveTo);
					//System.out.println("boxCoordinate: " + this.xLocation +  " " + this.yLocation);
				} else {
					this.xLocation += 1;
				}
			} else {
				setPlayerLocation(moveTo);
			}
		} else {
			System.out.println("Blocked by wall");
			this.xLocation += 1;
		}
        System.out.println(getPlayerLocation().getX() + ", " + getPlayerLocation().getY());
		System.out.println("Player Coordinate: x = " + getPlayerXCoordinate() + ", y = " + getPlayerYCoordinate());

    }
    
    /**
     * move player character right
     * @param BD: boxDimension
     */
    public void moveRight(int BD) {
        Point2D moveTo = new Point2D.Double(getPlayerLocation().getX() + BD, 
                							getPlayerLocation().getY());
        boolean crateMoved = false;  // is the box movable?
		this.xLocation += 1;       
		// wall collision detection
		if (this.mg.getNode(xLocation, yLocation).getWall() == false) {
			// check if there is a crate on the tile we want to move to
			if (this.mg.getNode(xLocation, yLocation).getCrate() == true) {
				System.out.println("Encountered Crate");
				// check if crate is movable and if so move it
				for (Crates c : this.crates) {
					if (c.getCrateXCoordinate() == xLocation && 
						c.getCrateYCoordinate() == yLocation) {
						crateMoved = c.moveRight(BD);
						break;
					}
				}
				
				// crate has been Moved
				if (crateMoved == true) {
					setPlayerLocation(moveTo);
					//System.out.println("boxCoordinate: " + this.xLocation +  " " + this.yLocation);
				} else {
					this.xLocation -= 1;
				}
			} else {
				setPlayerLocation(moveTo);
			}
		} else {
			System.out.println("Blocked by wall");
			this.xLocation -= 1;
		}
        System.out.println(getPlayerLocation().getX() + ", " + getPlayerLocation().getY());
		System.out.println("Player Coordinate: x = " + getPlayerXCoordinate() + ", y = " + getPlayerYCoordinate());

    }
}

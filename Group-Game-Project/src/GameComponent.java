import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class GameComponent extends JComponent implements KeyListener {

	private MapGenerator mg;
	private int frameWidth;
	private int frameHeight;
	private Player p;
	private Image playerImage;
	private Image crateImage;
	private Image winImage;
	private int boxDimensions;
	private ArrayList<Crates> crate;

	public GameComponent(int numOfXTiles, int numOfYTiles, int FW, int FH) {
		// determine the number of crates to be spawned according to the total
		// size of the map
		// this was something i threw together, so probably need to re-evaluate
		int areaOfMap = numOfXTiles * numOfYTiles;
		int numOfCrates;
		if (areaOfMap <= 25) {
			numOfCrates = 1;
		} else if (areaOfMap >= 64 && areaOfMap < 121) {
			numOfCrates = 2;
		} else {
			numOfCrates = 4;
		}

		// add keyListener to GameComponent
		this.addKeyListener(this);

		// generate map
		do {
			mg = new MapGenerator(numOfXTiles, numOfYTiles, numOfCrates);
			System.out.println("discarding map...");
		} while (!mg.isValid());
		
		this.frameWidth = FW;
		this.frameHeight = FH;

		// number of pixels per tile: use FW or FH, doesn't matter
		this.boxDimensions = (FW / numOfYTiles);

		// create crate objects
		this.crate = new ArrayList<Crates>(numOfCrates);
		for (int i = 0; i < numOfCrates; i++) {
			this.crate.add(new Crates(mg.getTilesArray(), mg, this.boxDimensions, numOfXTiles, numOfYTiles));
		}

		// initialize player
		p = new Player(mg.getTilesArray(), this.boxDimensions, mg, this.crate);

		ImageIcon img = new ImageIcon("resource/Images/crate.png");
		this.crateImage = img.getImage();

		img = new ImageIcon("resource/Images/linkDown.png");
		this.playerImage = img.getImage();
		
		img = new ImageIcon("resource/Images/win.jpg");
		this.winImage = img.getImage();
	}

	/**
	 * A function that is used to draw the maze on the frame
	 * 
	 * @param g
	 *            The graphics being used
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		mg.paintMap(g2, this.frameWidth, this.frameHeight);
		
		mg.getLocationOfGoal(mg);

		// draw/display player on the board
		// "this.p.getPlayerLocation().getX() + 1"
		// is implemented so that the player appears to be more centered on the
		// tile
		g2.drawImage(this.playerImage, (int) this.p.getPlayerLocation().getX() + 1,
				(int) this.p.getPlayerLocation().getY(), this.boxDimensions - 1, this.boxDimensions - 1, null);

		// draw/display all crates on the board
		for (Crates c : crate) {
			g2.drawImage(this.crateImage, (int) c.getCrateLocation().getX() + 1, (int) c.getCrateLocation().getY(),
					this.boxDimensions - 1, this.boxDimensions - 1, null);
		}
		
		if (mg.getLocationOfGoal(mg) == true) {
			g2.drawImage(this.winImage, frameHeight/2-(410/2), frameWidth/2-(519/2), null);
		}
	}

	/**
	 * Checks for arrow key presses, updates player's location and repaints
	 * player location also changes sprite direction according to key pressed
	 */
	public void keyPressed(KeyEvent e) {
		int BD = this.boxDimensions;
		ImageIcon img; // image variable used to update player's image
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			p.moveUp(BD);
			img = new ImageIcon("resource/Images/linkUp.png");
			playerImage = img.getImage();
			// System.out.println(p.getPlayerLocation());
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			p.moveDown(BD);
			img = new ImageIcon("resource/Images/linkDown.png");
			playerImage = img.getImage();
			// System.out.println(p.getPlayerLocation());
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			p.moveLeft(BD);
			img = new ImageIcon("resource/Images/linkLeft.png");
			playerImage = img.getImage();
			// System.out.println(p.getPlayerLocation());
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			p.moveRight(BD);
			img = new ImageIcon("resource/Images/linkRight.png");
			playerImage = img.getImage();
			// System.out.println(p.getPlayerLocation());
			break;
		}
		repaint();
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	public MapGenerator getMG() {
		return this.mg;
	}

}

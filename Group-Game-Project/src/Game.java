import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class Game extends JFrame {
	GameComponent gameComponent;
	int frameWidth = 580;
	int frameHeight = 580;
	String s;
	JFrame game = new JFrame();

	public Game(String string) {
		super("Warehouse Boss");
		String[] dim = string.split(" ");
		s = string;
		gameComponent = new GameComponent(Integer.parseInt(dim[0]), Integer.parseInt(dim[1]), frameWidth,
				frameHeight);

		gameComponent.setFocusable(true);
		game.add(gameComponent); // add the gameComponent object
		game.setSize(frameWidth, frameHeight); // set the JFrame's width and height
		game.setResizable(false); // JFrame cannot be resized
		game.setLocationRelativeTo(null); // JFrame is centered on the screen
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame should close
														// upon clicking the
														// exit button
		game.setJMenuBar(createMenuBar());
		game.setVisible(true);
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem exitItem = new JMenuItem("Exit");
		JMenuItem mainMenu = new JMenuItem("Main Menu");

		fileMenu.add(exitItem);
		fileMenu.add(mainMenu);

		JMenu gameMenu = new JMenu("Game");
		JMenuItem Restart = new JMenuItem("Restart");
		
		gameMenu.add(Restart);

		menuBar.add(fileMenu);
		menuBar.add(gameMenu);
		
		fileMenu.setMnemonic(KeyEvent.VK_F);
		exitItem.setMnemonic(KeyEvent.VK_X);
		
		gameMenu.setMnemonic(KeyEvent.VK_G);
		Restart.setMnemonic(KeyEvent.VK_R);

		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		Restart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		mainMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int action = JOptionPane.showConfirmDialog(Game.this,
						"Do you really want to exit the game?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);

				if (action == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		});
		
		Restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int action = JOptionPane.showConfirmDialog(Game.this,
						"Do you really want to Restart the game?", "Confirm restart", JOptionPane.OK_CANCEL_OPTION);

				if (action == JOptionPane.OK_OPTION) {
					game.dispose();
					Game g = new Game(s);
				}
			}
		});
		
		mainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int action = JOptionPane.showConfirmDialog(Game.this,
						"Are you sure you want to exit and go to main menu?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);

				if (action == JOptionPane.OK_OPTION) {
					game.dispose();
					StartMenuFrame sf = new StartMenuFrame(null);
				}
			}
		});

		return menuBar;
	}
	
}

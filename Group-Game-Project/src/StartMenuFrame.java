import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * Custom JFrame containing the start menu components.
 * 
 * @author rowantw
 *
 */
public class StartMenuFrame extends JFrame {

	// StartMenuPanel contains the menu components and the game title
	private String diffLevel;
	private StartMenuPanel startMenuPanel;

	/**
	 * Constructor that instantiates this JFrame and its components.
	 * 
	 * @param inputArgs
	 *            the app's input arguments that are passed to a Game's
	 *            constructor when its instantiated
	 */
	public StartMenuFrame(String[] inputArgs) {
		super("Warehouse Boss");

		setLayout(new BorderLayout());

		startMenuPanel = new StartMenuPanel();

		// Attach a listener to the StartMenuPanel that will send its events to
		// this frame. Depending on button selection, will start a new
		// game or exit the application.
		startMenuPanel.setStartMenuListener(new StartMenuListener() {
			public void startMenuEventOccurred(StartMenuEvent e) {
				if (e.getBtnSelected().equals("Play")) {
					new Game(e.getDiffLevel());
					diffLevel = e.getDiffLevel();
					setVisible(false);
				} else if (e.getBtnSelected().equals("Quit")) {
					setVisible(false);
					StartMenuFrame.this.dispatchEvent(new WindowEvent(StartMenuFrame.this, WindowEvent.WINDOW_CLOSING));
				}
			}
		});

		add(startMenuPanel, BorderLayout.CENTER);
		
		setJMenuBar(createMenuBar());

		setMinimumSize(new Dimension(500, 400));
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem exitItem = new JMenuItem("Exit");

		fileMenu.add(exitItem);

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

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int action = JOptionPane.showConfirmDialog(StartMenuFrame.this,
						"Do you really want to exit the game?", "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);

				if (action == JOptionPane.OK_OPTION) {
					System.exit(0);
				}
			}
		});
		
		Restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int action = JOptionPane.showConfirmDialog(StartMenuFrame.this,
						"You need to start a game to restart.", "Cancel", JOptionPane.OK_CANCEL_OPTION);
			}
		});

		return menuBar;
	}
}

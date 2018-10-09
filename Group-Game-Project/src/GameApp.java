import javax.swing.SwingUtilities;

public class GameApp {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new StartMenuFrame(args);
			}
		});
	}
}

import java.util.EventListener;

/**
 * Custom EventListener interface for StartMenuPanel.
 * 
 * @author rowantw
 */
public interface StartMenuListener extends EventListener {
	/**
	 * Process an event created in StartMenuPanel when a component receives a
	 * recognized action event.
	 * 
	 * @param e
	 *            the StartMenuEvent generated
	 */
	public void startMenuEventOccurred(StartMenuEvent e);
}

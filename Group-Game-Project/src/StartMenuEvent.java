import java.util.EventObject;

/**
 * Custom event created by StartMenuPanel.
 * 
 * @author rowantw
 *
 */
public class StartMenuEvent extends EventObject {

	// Set according to what button the action event was triggered on
	private String btnSelected;
	private String diffLevel;

	/**
	 * Default constructor for StartMenuEvent.
	 * 
	 * @param source
	 *            the source of the event
	 */
	public StartMenuEvent(Object source) {
		super(source);
	}

	/**
	 * Overloaded constructor for StartMenuEvent to include the selected menu
	 * button.
	 * 
	 * @param source
	 *            the source of the event
	 * @param btnSelected
	 *            the name of the button selected
	 */
	public StartMenuEvent(Object source, String btnSelected) {
		super(source);

		this.btnSelected = btnSelected;
	}

	/**
	 * Overloaded constructor for StartMenuEvent to include the selected
	 * difficulty level.
	 * 
	 * @param source
	 *            the source of the event
	 * @param btnSelected
	 *            the name of the button selected
	 * @param diffLevel
	 *            the selected difficulty level
	 */
	public StartMenuEvent(Object source, String btnSelected, String diffLevel) {
		super(source);

		this.btnSelected = btnSelected;
		this.diffLevel = diffLevel;
	}

	/**
	 * Getter for the button selected attribute of the StartMenuEvent.
	 * 
	 * @return the name of the button selected
	 */
	public String getBtnSelected() {
		return btnSelected;
	}

	/**
	 * Getter for the difficulty level attribute of the StartMenuEvent.
	 * 
	 * @return the difficulty level selected
	 */
	public String getDiffLevel() {
		return diffLevel;
	}
}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.awt.font.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.imageio.ImageIO;


import com.sun.glass.events.KeyEvent;


/**
 * JPanel containing the game's title and start menu controls
 */
public class StartMenuPanel extends JPanel {

	private StartMenuListener startMenuListener;
	private JButton startBtn;
	private JButton helpBtn;
	private JButton soundBtn;
	private JButton quitBtn;
	private JLabel title;
	private JLabel sTitle;
	private JLabel iTitle;
	private JRadioButton easyRadio;
	private JRadioButton intermediateRadio;
	private JRadioButton hardRadio;
	private ButtonGroup difficultyGroup;
	private JButton musicOn;
	private JButton musicOff;
	private JLabel im;
	private JButton back;
	private ButtonGroup mOptionsGroup;
	private JButton playBtn;
	private JButton backBtn;
	private Sound s;
	private boolean enableMusic = true;
	private JButton b;

	/**
	 * Instantiates a StartMenuPanel within the parent frame and attaches action
	 * listeners to menu buttons.
	 */
	public StartMenuPanel() {
		// Size this JPanel correctly within the parent frame
		Dimension dim = getPreferredSize();
		setPreferredSize(dim);

				
		// Instantiate components within this StartMenuPanel
		startBtn = new JButton("Start");
		helpBtn = new JButton("Help");
		soundBtn = new JButton("Sound");
		quitBtn = new JButton("Quit");
		title = new JLabel("Warehouse Boss");
		title.setFont(new Font("Comic Sans MS", Font.PLAIN, 32));

		// Instantiate components for difficulty selection
		easyRadio = new JRadioButton("Easy");
		intermediateRadio = new JRadioButton("Intermediate");
		hardRadio = new JRadioButton("Hard");
		playBtn = new JButton("Play");
		backBtn = new JButton("Back");
		difficultyGroup = new ButtonGroup();

		difficultyGroup.add(easyRadio);
		difficultyGroup.add(intermediateRadio);
		difficultyGroup.add(hardRadio);

		easyRadio.setSelected(true);

		// Instantiate components for help menu
		sTitle = new JLabel("Sound Options");
		musicOn = new JButton("Music On");
		musicOff = new JButton("Music Off");
		back = new JButton("Back");
		mOptionsGroup = new ButtonGroup();
		
		iTitle = new JLabel("Instructions");
		iTitle.setFont(new Font("Comic Sans MS", Font.PLAIN, 34));
		im = new JLabel();
		im.setIcon(new ImageIcon("resource/Images/ins.png"));
		b = new JButton("I got it!!");
		
		mOptionsGroup.add(musicOn);
		mOptionsGroup.add(musicOff);
		mOptionsGroup.add(back);

		// Set up mnemonics
		startBtn.setMnemonic(KeyEvent.VK_S);
		helpBtn.setMnemonic(KeyEvent.VK_H);
		quitBtn.setMnemonic(KeyEvent.VK_Q);

		easyRadio.setMnemonic(KeyEvent.VK_1);
		intermediateRadio.setMnemonic(KeyEvent.VK_2);
		hardRadio.setMnemonic(KeyEvent.VK_3);
		playBtn.setMnemonic(KeyEvent.VK_P);
		backBtn.setMnemonic(KeyEvent.VK_B);
		back.setMnemonic(KeyEvent.VK_B);

		
		// Set dimensions of the game map
		easyRadio.setActionCommand("5 5");
		intermediateRadio.setActionCommand("8 8");
		hardRadio.setActionCommand("11 11");

		// Instantiate game sound
		s = new Sound();
		s.startSound(!enableMusic);
		//s.main();
		
			
		// Add action listeners to components to send events to StartMenuFrame
		// event listener or rearrange component layout 
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layoutDiffSelect();
			}
		});
		
		soundBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				layoutSoundSelect();
			}
		});
		
		musicOn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				enableMusic = true;
				layoutMenu();
			}
		});
		
		musicOff.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				enableMusic = false;
				layoutMenu();
			}
		});
		
		
		helpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				layoutHelpSelect();
			}
		});

		quitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartMenuEvent ev = new StartMenuEvent(this, "Quit");

				if (startMenuListener != null) {
					startMenuListener.startMenuEventOccurred(ev);
				}
			}
		});
		
		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartMenuEvent ev = new StartMenuEvent(this, "Play", difficultyGroup.getSelection().getActionCommand());
				Sound s1 = new Sound();
				
				if (enableMusic == true) {
					if(easyRadio.isSelected()){
						s1.easyS();
					} else if(intermediateRadio.isSelected()){
						s1.intermediateS();
					} else if(hardRadio.isSelected()){
						s1.hardS();
					}
				}
				if (startMenuListener != null) {
					startMenuListener.startMenuEventOccurred(ev);
				}
			}
		});

		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layoutMenu();
			}
		});
		
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layoutMenu();
			}
		});

		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layoutMenu();
			}
		});
		// Create a padded border around the StartMenuPanel
		Border innerBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
		Border outerBorder = BorderFactory.createEmptyBorder(40, 40, 40, 40);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		setLayout(new GridBagLayout());
		layoutMenu();
	}

	/**
	 * Layout components within StartMenuPanel according to set layout.
	 */
	public void layoutMenu() {
		removeAll();
		repaint();
		revalidate();

		
		GridBagConstraints gc = new GridBagConstraints();

		//////// First row ////////
		gc.gridy = 1;

		gc.weightx = 1;
		gc.weighty = 0;

		gc.ipady = 80;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 5, 0);
		add(title, gc);

		//////// Next row ////////
		gc.gridy++;

		gc.gridwidth = 1;

		gc.weightx = 1;
		gc.weighty = 0;

		gc.ipadx = 40;
		gc.ipady = 20;

		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 5, 0);
		add(startBtn, gc);

		///////// Next row////////
		gc.gridy++;

		gc.gridwidth = 1;

		gc.weightx = 1;
		gc.weighty = 0;

		gc.ipadx = 40;
		gc.ipady = 20;

		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 5, 0);
		add(soundBtn, gc);
		
		////////Next row/////
		gc.gridy++;

		gc.gridwidth = 1;

		gc.weightx = 1;
		gc.weighty = 0;

		gc.ipadx = 40;
		gc.ipady = 20;

		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 5, 0);
		add(helpBtn, gc);

		//////// Next row ////////
		gc.gridy++;

		gc.gridwidth = 1;

		gc.weightx = 1;
		gc.weighty = 0;

		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 5, 0);
		add(quitBtn, gc);
	}

	/**
	 * Replaces previous start menu layout with the difficulty selection screen
	 * and play button.
	 */
	public void layoutDiffSelect() {
		removeAll();
		repaint();
		revalidate();

		GridBagConstraints gc = new GridBagConstraints();

		//////// First row ////////
		gc.gridy = 0;

		gc.weightx = 1;
		gc.weighty = 0;

		gc.fill = GridBagConstraints.NONE;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Select difficulty: "), gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 20, 10, 0);
		s.startSound(false);
		add(easyRadio, gc);

		//////// Next row ////////
		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 0;

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 20, 10, 0);
		s.startSound(false);
		add(intermediateRadio, gc);

		//////// Next row ////////
		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 0;

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0, 20, 10, 0);
		s.startSound(false);
		add(hardRadio, gc);

		//////// Next row ////////
		gc.gridy++;

		gc.weightx = 1;
		gc.weighty = 0;

		// Resize button
		gc.ipadx = 40;
		gc.ipady = 20;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(100, 0, 0, 10);
		add(playBtn, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(100, 10, 0, 0);
		add(backBtn, gc);
	}
	
	public void layoutSoundSelect(){
		removeAll();
		repaint();
		revalidate();

		GridBagConstraints gc = new GridBagConstraints();

		//////// First row ////////
		gc.gridy = 0;
		
		gc.weightx = 1;
		gc.weighty = 0;

		gc.ipady = 40;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 5, 0);
		add(sTitle, gc);
		////next row///
		
		gc.gridy++;
		
		gc.gridwidth = 1;

		gc.weightx = 1;
		gc.weighty = 0;
		
		gc.ipadx = 40;
		gc.ipady = 20;

		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 5, 0);
		add(musicOn, gc);

		//////// Next row ////////
		gc.gridy++;

		gc.gridwidth = 1;

		gc.weightx = 1;
		gc.weighty = 0;

		gc.ipadx = 40;
		gc.ipady = 20;

		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 5, 0);
		add(musicOff, gc);

		//////// Next row ////////
		gc.gridy++;

		gc.gridwidth = 1;

		gc.weightx = 1;
		gc.weighty = 0;

		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 5, 0);
		add(back, gc);
	}
	
	public void layoutHelpSelect(){
		removeAll();
		repaint();
		revalidate();

		GridBagConstraints gc = new GridBagConstraints();

		//////// First row ////////
		gc.gridy = 0;

		gc.weightx = 1;	
		gc.weighty = 0;
		
		gc.ipady = 40;


		gc.gridx = 0;

		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0,0,5,0);
		add(iTitle , gc);
		
		//rules section
		gc.gridy++;
		
		gc.weightx = 1;
		gc.weighty = 0;
		
		gc.ipadx = 40;
		gc.ipady = 20;
		
		gc.anchor = GridBagConstraints.LINE_END;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0,0,5,0);
		add(im, gc);
		
		//back button
		gc.gridy++;
		
		gc.gridwidth = 1;
		
		gc.weightx = 1;
		gc.weighty = 0;
		
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;
		gc.insets = new Insets(0, 0, 2, 0);
		add(b, gc);
	}

	/**
	 * Sets listener interface for StartMenuPanel.
	 * 
	 * @param startMenuListener
	 *            the attached listener interface
	 */
	public void setStartMenuListener(StartMenuListener startMenuListener) {
		this.startMenuListener = startMenuListener;
	}
	
	public void stopMusic(){
		s.stopSound();
	}

	/**
	 * Instantiates sound for game start if enabled. 
	 */
	public void startMusic() {
		if (enableMusic) {
			Sound s = new Sound();
			s.easyS();
		}
	}

}

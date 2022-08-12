package view.GUIresources;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Game;
import model.GameObserver;

public class ToolBar extends JToolBar implements GameObserver {

	private static final long serialVersionUID = 1L;

	private static final double MIN_SPEED = 0.5;
	private static final double MAX_SPEED = 10;
	private static final double SPEED_EXP = 3;//Speed exponentiation in the slider
	private static final Color DEFAULT_COLOR = new Color(255, 255, 230);

	private Game game;
	private JButton stepBack;
	private JButton step;
	private JButton clear;
	private JButton playPause;
	private JLabel speedLabel = new JLabel("Speed:");
	private JSlider speedSlider;
	private JButton openColorChooser;
	private JButton load;
	private JToggleButton selectionMode;
	private JButton save;

	private String playLabel = "PLAY";
	private String pauseLabel = "PAUSE";

	private ArrayList<ToolBarObserver> observers = new ArrayList<ToolBarObserver>();

	public ToolBar(Game game) {
		this.game = game;
		game.addObserver(this);

		this.setBackground(DEFAULT_COLOR);
		initTools();
	}

	private void initTools() {
		initStepBackButton();
		initStepButton();
		initClearButton();
		initPlayPauseButton();
		initSpeedSetter();
		initColorChooser();
		initLoadButton();
		initSelectionMode();
		initSaveButton();
	}

	private void initSaveButton() {
		save = new JButton("Save");
		save.setToolTipText("Save the copied structure into a game of life file");
		save.setVisible(false);//Not visible when you can't save anything
		this.add(save);

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Game of life files", "rle");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(load);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						game.saveToBeInserted(chooser.getSelectedFile());
					}
					catch(FileNotFoundException e1) {
						e1.printStackTrace();
					}
					catch(FileAlreadyExistsException e2) {
						JOptionPane.showMessageDialog(null, e2.getReason(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}

	private void initSelectionMode() {
		selectionMode = new JToggleButton("Selection mode", false);
		selectionMode.setToolTipText("Enable a mode where you can select a certain area of the board");
		this.add(selectionMode);

		selectionMode.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(selectionMode.isSelected() == true) {
					game.setSelectionState(1);//An enum would be more intuitive
					game.deleteToBeInserted();
				}
				else {
					game.setSelectionState(0);
					game.deleteToBeInserted();
				}
			}
		});
	}

	private void initLoadButton() {
		load = new JButton("Load file");
		load.setToolTipText("Load a game of life file");
		this.add(load);

		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Game of life files", "rle", "txt");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(load);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					//Here things will be done
					try {
						game.readFromFile(chooser.getSelectedFile());
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					//					System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
				}
			}
		});
	}

	private void initColorChooser() {
		openColorChooser = new JButton("Color configuration");
		openColorChooser.setToolTipText("Configure the color that the squares will use when switching on");
		this.add(openColorChooser);

		openColorChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(null, "Choose the main color", Color.YELLOW);
				if(c != null) {
					updateFirstColor(c);
				}
			}
		});
	}

	private void initSpeedSetter() {
		this.add(speedLabel);
		speedSlider = new JSlider(SwingConstants.HORIZONTAL, (int) (Math.pow(MIN_SPEED, 1/SPEED_EXP)*100), (int) (Math.pow(MAX_SPEED, 1/SPEED_EXP)*100), (int) (game.getSpeed()*100));
		speedSlider.setToolTipText("x" + game.getSpeed());

		speedSlider.setBackground(DEFAULT_COLOR);
		speedSlider.setMaximumSize(new Dimension(150, 15));

		this.add(speedSlider);
		speedSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				Runnable myRunnable = new Runnable(){
					public void run(){
						// exponential scale for speed values
						double result = (double)speedSlider.getValue() / 100;
						result = Math.pow(result, SPEED_EXP);
						
						game.setSpeed(result);
						
						String actSpeed = new DecimalFormat("#.##").format(game.getSpeed());//Format the double with two decimal values
						speedSlider.setToolTipText("x" + actSpeed);
					}
				};

				Thread thread = new Thread(myRunnable);
				thread.start();
			}
		});
	}

	private void initPlayPauseButton() {
		playPause = new JButton(playLabel);
		playPause.setToolTipText("Execute consecutive steps over the time");
		this.add(playPause);
		playPause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Runnable myRunnable = new Runnable(){//Create a new thread to change the game. This way you can listen to more actions while the game is being updated.
					public void run(){
						//System.out.println("Runnable running");
						if(playPause.getText() == playLabel) {
							game.play();
						}
						else {
							game.pause();
						}

						stepBack.setEnabled(true);
					}
				};

				Thread thread = new Thread(myRunnable);
				thread.start();
			}
		});
	}

	private void initClearButton() {
		clear = new JButton("Clear");
		clear.setToolTipText("Clear the board");
		this.add(clear);
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Runnable myRunnable = new Runnable(){
					public void run(){
						game.clearBoard();

						if(game.isTherePast()) {
							stepBack.setEnabled(true);
						}
					}
				};

				Thread thread = new Thread(myRunnable);
				thread.start();
			}
		});
	}

	private void initStepButton() {
		step = new JButton("Step");
		step.setToolTipText("Process one step in the game");
		this.add(step);
		step.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Runnable myRunnable = new Runnable(){
					public void run(){
						game.step();
						if(game.isTherePast()) {
							stepBack.setEnabled(true);
						}
					}
				};

				Thread thread = new Thread(myRunnable);
				thread.start();
			}
		});
	}

	private void initStepBackButton() {
		stepBack = new JButton("Step back");
		stepBack.setToolTipText("Undo one step in the game");
		stepBack.setEnabled(false);
		this.add(stepBack);
		stepBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Runnable myRunnable = new Runnable(){
					public void run(){
						if(!game.stepBack())
							System.out.println("ERROR stepBack");

						if(game.isTherePast()) {
							stepBack.setEnabled(true);
						}
						else {
							stepBack.setEnabled(false);
						}
					}
				};

				Thread thread = new Thread(myRunnable);
				thread.start();
			}
		});
	}

	public void addObserver(ToolBarObserver c) {
		observers.add(c);
	}

	public void updateFirstColor(Color c) {
		for(ToolBarObserver o : observers) {
			o.onFirstColorUpdate(c);
		}
	}

	@Override
	public void onRunningUpdate(boolean running) {
		if(running) {
			playPause.setText(pauseLabel);
			step.setEnabled(false);
			clear.setEnabled(false);
//			speedSlider.setEnabled(false);
			load.setEnabled(false);
			selectionMode.setEnabled(false);

			game.setSelectionState(0);//The selection mode gets restarted
			selectionMode.setSelected(false);
		}
		else {
			playPause.setText(playLabel);
			step.setEnabled(true);
			clear.setEnabled(true);
			speedSlider.setEnabled(true);
			load.setEnabled(true);
			selectionMode.setEnabled(true);
		}
	}

	@Override
	public void onBoardUpdate() {
		if(game.isTherePast() && !game.isRunning() && game.getSelectionState() == 0) {
			stepBack.setEnabled(true);
		}
		else {
			stepBack.setEnabled(false);
		}

		if(game.getSelectionState() == 3) {
			save.setVisible(true);
		}
		else {
			save.setVisible(false);
		}
	}

	@Override
	public void onSelectionMode(int mode) {
		if(mode == 0) {
			save.setVisible(false);
			selectionMode.setSelected(false);

			if(game.isRunning() == false) {
				playPause.setEnabled(true);
				if(game.isTherePast() && !game.isRunning()) {
					stepBack.setEnabled(true);
				}
				step.setEnabled(true);
				clear.setEnabled(true);
				speedSlider.setEnabled(true);
				load.setEnabled(true);
			}
		}
		else {
			selectionMode.setSelected(true);
			playPause.setEnabled(false);
			step.setEnabled(false);
			clear.setEnabled(false);
			speedSlider.setEnabled(false);
			load.setEnabled(false);

			if(mode == 3) {
				save.setVisible(true);
			}
			else {
				save.setVisible(false);
			}
		}
	}
}

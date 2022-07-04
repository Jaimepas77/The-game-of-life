package view.GUIresources;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Game;
import model.GameObserver;

public class ToolBar extends JToolBar implements GameObserver {

	private static final long serialVersionUID = 1L;

	private static final double MIN_SPEED = 0.25;
	private static final double MAX_SPEED = 4;
	private static final Color DEFAULT_COLOR = new Color(255, 255, 230);

	private Game game;
	private JButton stepBack;
	private JButton step;
	private JButton clear;
	private JButton playPause;
	private JLabel speedLabel = new JLabel("Speed:");
	private JSlider speedSlider;
	private JButton openColorChooser;

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
		speedSlider = new JSlider(SwingConstants.HORIZONTAL, (int) (MIN_SPEED*100), (int) (MAX_SPEED*100), (int) (game.getSpeed()*100));
		speedSlider.setToolTipText("x" + game.getSpeed());

		speedSlider.setBackground(DEFAULT_COLOR);
		speedSlider.setMaximumSize(new Dimension(150, 15));

		this.add(speedSlider);
		speedSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				Runnable myRunnable =
						new Runnable(){
					public void run(){
						game.setSpeed((double)speedSlider.getValue()/100);
						speedSlider.setToolTipText("x" + game.getSpeed());
					}
				};

				Thread thread = new Thread(myRunnable);
				thread.start();
			}
		});
	}

	private void initPlayPauseButton() {
		playPause = new JButton(playLabel );
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
			speedSlider.setEnabled(false);
		}
		else {
			playPause.setText(playLabel);
			step.setEnabled(true);
			clear.setEnabled(true);
			speedSlider.setEnabled(true);
		}
	}
	
	@Override
	public void onBoardUpdate() {
		if(game.isTherePast() && !game.isRunning()) {
			stepBack.setEnabled(true);
		}
		else {
			stepBack.setEnabled(false);;
		}
	}
}

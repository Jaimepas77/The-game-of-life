package view.GUIresources;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Game;
import model.GameObserver;
import view.ColorObserver;

public class ToolBar extends JToolBar implements GameObserver {

	private static final long serialVersionUID = 1L;

	private static final long minDelta = 50;
	private static final long maxDelta = 3000;
	private static final Color defaultColor = new Color(255, 255, 230);

	private Game game;
	private JButton stepBack;
	private JButton step;
	private JButton clear;
	private JButton playPause;
	private JLabel deltaLabel = new JLabel("Delay:");
	private JSlider deltaSlider;
	private JButton openColorChooser;

	private String playLabel = "PLAY";
	private String pauseLabel = "PAUSE";

	private ArrayList<ColorObserver> observers = new ArrayList<ColorObserver>();

	public ToolBar(Game game) {
		this.game = game;
		game.addObserver(this);

		this.setBackground(defaultColor);
		initTools();
	}

	private void initTools() {
		initStepBackButton();
		initStepButton();
		initClearButton();
		initPlayPauseButton();
		initDeltaSetter();
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

	private void initDeltaSetter() {
		this.add(deltaLabel);
		deltaSlider = new JSlider(SwingConstants.HORIZONTAL, (int)minDelta, (int)maxDelta, (int)game.getDelta());
		deltaSlider.setToolTipText((double)game.getDelta()/1000 + " seconds");

		deltaSlider.setBackground(defaultColor);
		deltaSlider.setMaximumSize(new Dimension(150, 15));

		this.add(deltaSlider);
		deltaSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				Runnable myRunnable =
						new Runnable(){
					public void run(){
						game.setDelta(deltaSlider.getValue());
						deltaSlider.setToolTipText((double)game.getDelta()/1000 + " seconds");
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

	public void addObserver(ColorObserver c) {
		observers.add(c);
	}

	public void updateFirstColor(Color c) {
		for(ColorObserver o : observers) {
			o.onFirstColorUpdate(c);
		}
	}

	@Override
	public void onRunningUpdate(boolean running) {
		if(running) {
			playPause.setText(pauseLabel);
			step.setEnabled(false);
			clear.setEnabled(false);
			deltaSlider.setEnabled(false);
			stepBack.setEnabled(false);
		}
		else {
			playPause.setText(playLabel);
			step.setEnabled(true);
			clear.setEnabled(true);
			deltaSlider.setEnabled(true);

			if(game.isTherePast()) {
				stepBack.setEnabled(true);
			}
		}
	}
}

package view.GUIresources;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class ToolBar extends JToolBar implements GameObserver {

	private static final long serialVersionUID = 1L;
	
	private static final long minDelta = 50;
	private static final long maxDelta = 3000;
	private static final Color defaultColor = new Color(255, 255, 230);

	private Game game;
	private JButton step;
	private JButton clear;
	private JButton playPause;
	private JLabel deltaLabel = new JLabel("Delay:");
	private JSlider deltaSlider;
	private JColorChooser colorChooser;

	private String playLabel = "PLAY";
	private String pauseLabel = "PAUSE";

	public ToolBar(Game game) {
		this.game = game;
		game.addObserver(this);

		this.setBackground(defaultColor);
		initTools();
	}

	private void initTools() {
		initStepButton();
		initClearButton();
		initPlayPauseButton();
		initDeltaSetter();
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
						for(int i = 0; i < game.getRows(); i++)
							for(int j = 0; j < game.getColumns(); j++)
								game.setSquareState(false, i, j);
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
					}
				};

				Thread thread = new Thread(myRunnable);
				thread.start();
			}
		});
	}

	@Override
	public void onRunningUpdate(boolean running) {
		if(running) {
			playPause.setText(pauseLabel);
			step.setEnabled(false);
			clear.setEnabled(false);
			deltaSlider.setEnabled(false);
		}
		else {
			playPause.setText(playLabel);
			step.setEnabled(true);
			clear.setEnabled(true);
			deltaSlider.setEnabled(true);
		}
	}
}

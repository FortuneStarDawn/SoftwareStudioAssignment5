package main;

import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameStage implements Runnable
{
	private int score, winScore, bg_X, ball_X, ball_Y, duck_X, duck_Y, word_Y, direction;
	private Thread gameThread;
	private ImagePanel myPanel;
	private JFrame myFrame;
	private JLabel scoreLabel, waitLabel, wrongLabel;
	private JTextField myField;
	private boolean bgMove, ballMove, duckMove;
	
	public GameStage()
	{
		myFrame = new JFrame(); //set Frame
		myFrame.setSize(1000, 500);
		myFrame.setTitle("HumanOCRun");
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myField = new JTextField(); //set text input area
		myField.setBounds(10,420,295,25);
		myFrame.add(myField);
		scoreLabel = new JLabel(); //set score label
		scoreLabel.setBounds(860, 10, 100, 40);
		scoreLabel.setFont(new Font("Calibri", 1, 24));
		myFrame.add(scoreLabel);
		waitLabel = new JLabel(); //set wait label
		waitLabel.setBounds(100, 200, 100, 30);
		waitLabel.setFont(new Font("Calibri", 1, 24));
		myFrame.add(waitLabel);
		wrongLabel = new JLabel(); //set wrong answer label
		wrongLabel.setBounds(5, 200, 350, 30);
		wrongLabel.setFont(new Font("Calibri", 1, 20));
		myFrame.add(wrongLabel);
		myPanel = new ImagePanel(this); //set panel
		myFrame.add(myPanel);
		myFrame.setVisible(true);
		initial(); //initial variable value
	}
	
	public void start()
	{
		gameThread = new Thread(this); //create gameThread
		gameThread.start(); //start
	}
	
	public void addScore()
	{
		score++;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void setWinScore(int winScore)
	{
		this.winScore = winScore;
	}
	
	public int getWinScore()
	{
		return winScore;
	}
	
	public void run()
	{
		int delay1 = 0, delay2 = 0, delay3 = 0;
		Thread currentThread = Thread.currentThread();
		while (currentThread == gameThread)
		{
			myFrame.repaint(); //repaint
			
			//if there is input, don't show the wrong answer label
			if(!myField.getText().equals("")) wrongLabel.setText("");
				
			if(bgMove) //if background need to move
			{
				if(delay1<25)
				{
					bg_X--;
					delay1++;
				}
				else
				{
					delay1 = 0;
					bgMove = false;
				}
			}
			
			if(ballMove) //if ball need to move
			{
				if(delay2<25)
				{
					ball_X--;
					delay2++;
				}
				else
				{
					delay2 = 0;
					ballMove = false;
				}
			}
			
			if(duckMove) //if duck need to move
			{
				if(delay3<30)
				{
					duck_X++;
					delay3++;
				}
				else
				{
					delay3 = 0;
					duckMove = false;
				}
			}
			
			 //if still not win, let duck and ball floating and words move from up to down
			if(score<winScore)
			{
				floating();
				//when the state is wait or wrong, stop moving the word
				if(waitLabel.getText().equals("") && wrongLabel.getText().equals("")) upToDown();
			}
			
			try 
			{
				Thread.sleep(40); //delay
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public int getbg_X()
	{
		return bg_X;
	}
	
	public int getBall_X()
	{
		return ball_X;
	}
	
	public int getBall_Y()
	{
		return ball_Y;
	}
	
	public int getDuck_X()
	{
		return duck_X;
	}
	
	public int getDuck_Y()
	{
		return duck_Y;
	}
	
	public int getWord_Y()
	{
		return word_Y;
	}
	
	public void setWord_Y(int in)
	{
		word_Y = in;
	}
	
	public void floating()
	{
		//let duck and ball float
		if (direction == 0 && duck_Y > 250) direction = 1;
		else if (direction == 1 && duck_Y < 230) direction = 0;
		if (direction == 0)
		{
			duck_Y++;
			ball_Y++;
		}
		else if(direction == 1)
		{
			duck_Y--;
			ball_Y--;
		}
	}
	
	public void upToDown()
	{
		//let words move from up to down
		if(word_Y>400) word_Y = 0;
		else word_Y+=3;
	}
	
	public ImagePanel getPanel()
	{
		return myPanel;
	}
	
	public JTextField getField()
	{
		return myField;
	}
	
	public void initial()
	{
		//initial value
		score = 0;
		bgMove = false;
		ballMove = false;
		duckMove = false;
		direction = 0;
		bg_X = 320;
		duck_X = 580;
		duck_Y = 240;
		ball_X = 1000;
		ball_Y = 240;
		myField.setText("");
		scoreLabel.setText("Score: 0");
		waitLabel.setText("");
		wrongLabel.setText("");
	}
	
	public JLabel getWrongLabel()
	{
		return wrongLabel;
	}
	
	public JLabel getWaitLabel()
	{
		return waitLabel;
	}
	
	public void right()
	{
		//when the two input is the same, add score and move duck, ball or background
		waitLabel.setText("");
		if(score<winScore)
		{
			addScore();
			scoreLabel.setText("Score: " + getScore());
			if(getScore() >= getWinScore()-10)
			{
				if(getScore() >= getWinScore()-6)
				{
					duckMove = true;
				}
				else
				{
					bgMove = true;
					ballMove = true;
				}
			}
			else bgMove = true;
		}
	}
	
}

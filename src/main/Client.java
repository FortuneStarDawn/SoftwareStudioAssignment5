package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class Client
{	
	private String destinationIPAddr;
	private int destinationPortNum;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	
	public Client(String IPAddress, int portNum) //constructor
	{
		this.destinationIPAddr = IPAddress;
		this.destinationPortNum = portNum;
	}
	
	public void connect() //create a socket and connect to server
	{
		try 
		{
			socket = new Socket(destinationIPAddr, destinationPortNum);
		} 
		catch (UnknownHostException e1)
		{
			e1.printStackTrace();
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		try 
		{
			this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		ClientThread client = new ClientThread();
		client.start();
	}
	
	//This Thread is used to write input to server and read result from server
	class ClientThread extends Thread implements KeyListener
	{
		private JTextField text;
		private GameStage gs;
		private String line;
		private int mode, number;
		private JLabel waitLabel, wrongLabel;
		
		public ClientThread()
		{
			//create a GameStage and take something we need
			gs = new GameStage();
			gs.setWinScore(20);
			text = gs.getField();
			text.addKeyListener(this);
			waitLabel = gs.getWaitLabel();
			wrongLabel = gs.getWrongLabel();
		}
		public void run() 
		{
			line = new String("");
			gs.start();
			while(true) 
			{
				//if score < win_score, read the result comes from server
				if(gs.getScore()<gs.getWinScore())
				{
					try 
					{
						line = reader.readLine();
						//N means newWord, it will take a new word from known or unknown.
						if(line.equals("N"))
						{
							waitLabel.setText("");
							mode = reader.read();
							number = reader.read();
							gs.getPanel().setWord(mode, number);
						}
						//W means wait, set the wait_label to show
						else if(line.equals("W")) waitLabel.setText("Waiting...");
						//T means true, call right to add score
						else if(line.equals("T")) gs.right();
						//F means false, set the wrong_label to show
						else if(line.equals("F"))
						{
							waitLabel.setText("");
							wrongLabel.setText("Wrong answer,Please re-enter again!");
						}
					} 
					catch (IOException e)
					{
						
					}
				}
			}
		}
		
		public void keyPressed(KeyEvent e) 
		{
			//sent input to server
			if(e.getKeyCode()==KeyEvent.VK_ENTER)
			{
				if(!line.equals("W")) //if the state is wait, don't sent the message to server
				{
					if(!text.getText().equals("")) //if there is no input, don't sent the message
					{
						writer.println(text.getText());
						writer.flush();
					}
				}
				text.setText("");
			}
		}

		public void keyReleased(KeyEvent e)
		{
			
		}

		public void keyTyped(KeyEvent e)
		{
			
		}
	}
	
	
	public static void main(String[] args) 
	{
		Client client = new Client("127.0.0.1", 8000); //new a client
		client.connect(); //connect it to server
	}

}

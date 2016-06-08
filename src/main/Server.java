package main;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;


public class Server extends JFrame
{
	private static final long serialVersionUID = 1L;
	private ServerSocket serverSocket;
	private ServerThread[] connections = new ServerThread[2];
	private JTextArea textArea;
	
	public Server(int portNum) 
	{
		try 
		{
			this.serverSocket = new ServerSocket(portNum);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//create the server frame and text area
		this.setSize(500, 300);
		this.setTitle("Server");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.textArea.setPreferredSize(new Dimension(500, 300));
		this.add(textArea);
		this.setVisible(true);
	}
	
	public void waitClient() 
	{
		//get the date
		SimpleDateFormat nowdate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy\n", Locale.US);
		nowdate.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String sdate = nowdate.format(new Date());
		Socket client;
		try 
		{
			textArea.append("server started at: " + sdate); //print the date
			textArea.append("server is waiting...\n");
			client = serverSocket.accept(); //wait for connect
			connections[0] = new ServerThread(client); //put it into the connections array
			textArea.append("server is connecting!\n");
			textArea.append("Player1's host name is 127.0.0.1\n");
			textArea.append("Player1's IP Address is 127.0.0.1\n");
			connections[0].start();
			connections[0].writer.println("W"); //set the state to wait
			connections[0].writer.flush();
			
			textArea.append("server is waiting...\n");
			client = serverSocket.accept();
			connections[1] = new ServerThread(client);
			textArea.append("server is connecting!\n");
			textArea.append("Player2's host name is 127.0.0.1\n");
			textArea.append("Player2's IP Address is 127.0.0.1\n");
			connections[1].start();
			
			textArea.append("game start!\n");
			newWord(); //get first word
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void newWord()
	{
		int mode, number;
		Random random = new Random();
		mode = random.nextInt(2);
		if(mode==0) number = random.nextInt(51);
		else number = random.nextInt(73);
		
		//set state to N(new word), and give the random number to two clients
		connections[0].writer.println("N");
		connections[0].writer.write(mode);
		connections[0].writer.write(number);
		connections[0].writer.flush();
		
		connections[1].writer.println("N");
		connections[1].writer.write(mode);
		connections[1].writer.write(number);
		connections[1].writer.flush();
	}
	
	class ServerThread extends Thread 
	{
		private Socket socket;
		private BufferedReader reader;
		private PrintWriter writer;
		private String line;
		
		public ServerThread(Socket socket)
		{
			this.socket = socket;
			this.line = new String("");
			try 
			{
				reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
				writer = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		public void run() 
		{
			String str1, str2;
			while(true) 
			{
				try
				{
					line = reader.readLine(); //read input comes from client
					str1 = connections[0].line;
					str2 = connections[1].line;
					if(!str1.equals("")) //compare two input
					{
						if(!str2.equals(""))
						{
							if(str1.equals(str2)) //same
							{
								connections[0].writer.println("T");
								connections[0].writer.flush();
								connections[1].writer.println("T");
								connections[1].writer.flush();
								newWord();
							}
							else //wrong
							{
								connections[0].writer.println("F");
								connections[0].writer.flush();
								connections[1].writer.println("F");
								connections[1].writer.flush();
							}
							connections[0].line = "";
							connections[1].line = "";
						}
						else //wait for another input
						{
							connections[0].writer.println("W");
							connections[0].writer.flush();
						}
					}
					else //wait for another input
					{
						connections[1].writer.println("W");
						connections[1].writer.flush();
					}
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args)
	{
		Server server = new Server(8000); //create a server
		server.waitClient(); //wait for clients
	}
}

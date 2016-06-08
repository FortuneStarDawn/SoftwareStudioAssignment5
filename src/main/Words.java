package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Words
{
	private BufferedImage[] known;
	private BufferedImage[] unknown;
	private File f1, f2;
	private String[] s1;
	private String[] s2;
	
	public Words()
	{
		int i;
		known = new BufferedImage[51];
		unknown = new BufferedImage[73];
		f1 = new File("known");
		f2 = new File("unknown");
		s1 = f1.list(); //s1 has all known word file name
		s2 = f2.list(); //s2 has all unknown word file name
		
		for(i=0; i<51; i++)
		{
			try
			{
				known[i] = ImageIO.read(new File("./known/" + s1[i])); //put all known file into known[]
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		for(i=0; i<73; i++)
		{
			try
			{
				unknown[i] = ImageIO.read(new File("./unknown/" + s2[i])); //put all unknown file into unknown[]
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public BufferedImage getKnown(int number)
	{
		return known[number]; //number is a random number comes from client_Thread
	}
	
	public BufferedImage getUnknown(int number)
	{
		return unknown[number]; //number is a random number comes from client_Thread
	}
	
}

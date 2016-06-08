package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private BufferedImage b, duck, h, win, word;
	private GameStage gs;
	private Words myWords;
	private boolean drawWord;
	
	public ImagePanel(GameStage g)
	{
		gs = g;
		setLayout(null);
		myWords = new Words(); //new a word object
		try
		{
			//load in the pictures
			b = ImageIO.read(new File("b.png"));
			duck = ImageIO.read(new File("duck.png"));
			h = ImageIO.read(new File("h.png"));
			win = ImageIO.read(new File("win.png"));
			drawWord = false;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	protected void paintComponent(Graphics g) //override paintComponent
	{
        super.paintComponent(g);
        setBackground(new Color(130, 213, 253)); //put the background
        //draw image
        g.drawImage(h, gs.getbg_X(), 0, h.getWidth(), h.getHeight(), null);
        g.drawImage(b, gs.getBall_X(), gs.getBall_Y(), b.getWidth(), b.getHeight(), null);
        g.drawImage(duck, gs.getDuck_X(), gs.getDuck_Y(), duck.getWidth(), duck.getHeight(), null);
        //if win the game, draw the win image
        if(gs.getScore() >= gs.getWinScore()) g.drawImage(win, 500, 10, win.getWidth(), win.getHeight(), null);
        //draw the left block background
        g.setColor(new Color(209, 231, 252));
        g.fillRect(0, 0, 320, 500); 
        //draw the word which is chosen randomly
        if(drawWord) g.drawImage(word, 20, gs.getWord_Y(), word.getWidth(), word.getHeight(), null);
    }
	
	public void setWord(int mode, int number)
	{
		//random mode. if mode = 0, choose from known. Otherwise, choose unknown
		if(mode==0) word = myWords.getKnown(number);
		else word = myWords.getUnknown(number);
		gs.setWord_Y(0);
		drawWord = true;
	}
	
}

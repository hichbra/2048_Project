package ihm;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import metier.Terrain;

@SuppressWarnings("serial")
public class Controleur extends JFrame
{
	private Terrain terrain ;
	
	private Visu visu ;
	
	// Les commandes possibles
	private boolean activateQ ;
	private boolean activateD ;
	private boolean activateZ ;
	private boolean activateS ;
	
	
	public Controleur()
	{
		super("Agent");
		
		this.terrain = new Terrain() ;
		this.setLayout(new BorderLayout());
		this.addKeyListener(new ControleurKeyListener());
		
		this.activateQ = false ;
		this.activateD = false ;
		this.activateZ = false ;
		this.activateS = false ;

		this.visu = new Visu(this.terrain);
		this.add(visu, BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}


	private void actualisation() 
	{
		terrain.gravite();
		
		if ( activateQ )
		{
			terrain.getJoueur().recule();
		}
		else if ( activateD ) 
		{
			terrain.getJoueur().avance();
		}
		
		if ( activateZ )
		{
			if (!terrain.getJoueur().isTombe() && ! terrain.getJoueur().isSaut() )
				terrain.getJoueur().saute();
			
		}
		else if ( activateS )
		{
	
		}
		
		repaint();
	}


	class ControleurKeyListener implements KeyListener
	{
		public void keyPressed(KeyEvent e)
		{
			if ( e.getKeyCode() == KeyEvent.VK_LEFT || Character.toUpperCase(e.getKeyChar()) == 'Q' )
			{
				activateQ = true ;
			}
			else if ( e.getKeyCode() == KeyEvent.VK_RIGHT || Character.toUpperCase(e.getKeyChar()) == 'D' )
			{
				activateD = true ;
			}
			else if ( e.getKeyCode() == KeyEvent.VK_UP || Character.toUpperCase(e.getKeyChar()) == 'Z' )
			{
				activateZ = true ;
			}
			else if ( e.getKeyCode() == KeyEvent.VK_DOWN || Character.toUpperCase(e.getKeyChar()) == 'S' )
			{
				activateS = true ;				
			}
			
			visu.repaint();
		}

		@Override
		public void keyReleased(KeyEvent e) 
		{
			if ( e.getKeyCode() == KeyEvent.VK_LEFT || Character.toUpperCase(e.getKeyChar()) == 'Q' )
			{
				activateQ = false ;
			}
			else if ( e.getKeyCode() == KeyEvent.VK_RIGHT || Character.toUpperCase(e.getKeyChar()) == 'D' )
			{
				activateD = false ;
			}
			else if ( e.getKeyCode() == KeyEvent.VK_UP || Character.toUpperCase(e.getKeyChar()) == 'Z' )
			{
				activateZ = false ;
			}
			else if ( e.getKeyCode() == KeyEvent.VK_DOWN || Character.toUpperCase(e.getKeyChar()) == 'S' )
			{
				activateS = false ;				
			}
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}		
	}
	
	
	
	
	public static void main(String[] args) 
	{
		Controleur control = new Controleur() ;
		
		while(true)
		{
			control.actualisation();
			
			try {
				Thread.sleep(2);
			} 
			catch (InterruptedException e) {e.printStackTrace();}
		}
	}


}

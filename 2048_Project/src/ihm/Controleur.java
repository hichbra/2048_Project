package ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import metier.Expectimax;
import metier.Plateau;

@SuppressWarnings("serial")
public class Controleur extends JFrame
{
	public static int essai = 0 ;
	public static ArrayList<Integer> resultat = new ArrayList<Integer>() ;
	
	private FileWriter file ;
	private Plateau plateau ;
	private ArrayList<JLabel> labelCellules ;
	
	public Controleur()
	{
		super("2048_Project");
		
		try {
			this.file = new FileWriter(new File("test/Prof2.txt"), true);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		this.plateau = new Plateau();
		this.plateau.debut();
			
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(new ControleurListener());
		
		this.labelCellules = new ArrayList<JLabel>();
		
		JLabel lab ;
		for ( int cellule : plateau.getCellules() )
		{
			lab = new JLabel();
			
			if ( cellule != 0 )
				lab.setText(cellule+"");
			else
				lab.setText("");
			
			apparenceLabel(lab);
			labelCellules.add(lab);
		}
		
		JPanel panel = new JPanel(new GridLayout(4, 4));
		for ( JLabel label : labelCellules )
			panel.add(label);
		
		this.add(panel);
		this.pack();
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		lancerExpectimax();
		
	}
	
	private void lancerExpectimax() 
	{
		essai++ ;
		// Expectimax
		boolean fin = false ;
		boolean mouvPossible = true ;
		while(!fin) // Tant que l'on peut jouer
		{
			int dir = (int)Expectimax.expectimaxDirection(plateau.getShortTableau(), 2)[0];
			
			//direction : 1=gauche | 2=droite | 3=haut | 4=bas
			switch(dir)
			{
				case 1:
					mouvPossible = plateau.gauche();
					break;
				case 2:
					mouvPossible = plateau.droite();
					break;
				case 3:
					mouvPossible = plateau.haut();
					break;
				case 4:
					mouvPossible = plateau.bas();
					break;
				default:
					mouvPossible = false ;
					break;
			}

			if ( mouvPossible )
			{
				if ( ! plateau.tourSuivant()  )
				{
					fin = true ;
					finDuTest();
				}
			}
			
			actualiser();
		}

	}

	/**
	 * Donne l'apparence du label
	 * @param lab
	 */
	private void apparenceLabel(JLabel lab) 
	{
		lab.setPreferredSize(new Dimension(50,50));
		lab.setHorizontalAlignment(JLabel.CENTER);
		lab.setVerticalAlignment(JLabel.CENTER);
		lab.setBorder(BorderFactory.createBevelBorder(NORMAL));
		lab.setOpaque(true);
		
		if ( "".equals(lab.getText()) )
			lab.setBackground(new Color(119,110,101));
		else if ( lab.getText().equals("2") )
		{
			lab.setBackground(new Color(238,228,218));
			lab.setForeground(Color.BLACK);
		}
		else if ( lab.getText().equals("4") )
		{
			lab.setBackground(new Color(237,224,200));
			lab.setForeground(Color.BLACK);
		}
		else if ( lab.getText().equals("8") )
		{
			lab.setBackground(new Color(242,177,121));
			lab.setForeground(Color.WHITE);
		}
		else if ( lab.getText().equals("16") )
		{
			lab.setBackground(new Color(245,149,99));
			lab.setForeground(Color.WHITE);
		}
		else if ( lab.getText().equals("32") )
		{
			lab.setBackground(new Color(246,124,95));
			lab.setForeground(Color.WHITE);
		}
		else if ( lab.getText().equals("64") )
		{
			lab.setBackground(new Color(246,94,59));
			lab.setForeground(Color.WHITE);
		}
		else if ( lab.getText().equals("128") )
		{
			lab.setBackground(new Color(237,207,114));
			lab.setForeground(Color.GRAY);
		}
		else if ( lab.getText().equals("256") )
		{
			lab.setBackground(new Color(255,231,114));
			lab.setForeground(Color.GRAY);
		}
		else if ( lab.getText().equals("512") )
		{
			lab.setBackground(new Color(255,239,98));
			lab.setForeground(Color.BLACK);
		}
		else if ( lab.getText().equals("1024") )
		{
			lab.setBackground(new Color(255,241,90));
			lab.setForeground(Color.BLACK);
		}
		else
		{
			lab.setBackground(new Color(255,255,80));
			lab.setForeground(Color.MAGENTA);
		}
		
	}

	/**
	 * Classe ecouteur interne du controleur
	 * @author Hichbra
	 */
	class ControleurListener implements KeyListener
	{
		public void keyPressed(KeyEvent e)
		{
			if ( e.getKeyCode() == KeyEvent.VK_LEFT || Character.toUpperCase(e.getKeyChar()) == 'Q' )
			{
				if ( plateau.gauche() ) // si le mouvement est possible, on passe au tour suivant
					if ( ! plateau.tourSuivant() ) // Si on ne peut pas passer au tour suivant, le jeu est termin�
						finDuJeu();
			}
			else if ( e.getKeyCode() == KeyEvent.VK_RIGHT || Character.toUpperCase(e.getKeyChar()) == 'D' )
			{
				if ( plateau.droite() )
					if ( ! plateau.tourSuivant() )
						finDuJeu();
			}
			else if ( e.getKeyCode() == KeyEvent.VK_UP || Character.toUpperCase(e.getKeyChar()) == 'Z' )
			{
				if ( plateau.haut() )
					if ( ! plateau.tourSuivant() ) 
						finDuJeu();
			}
			else if ( e.getKeyCode() == KeyEvent.VK_DOWN || Character.toUpperCase(e.getKeyChar()) == 'S' )
			{
				if ( plateau.bas() )
					if ( ! plateau.tourSuivant() ) 
						finDuJeu();
			}
			actualiser();

		}

		public void keyReleased(KeyEvent e) {}

		public void keyTyped(KeyEvent e) {}

	}

	/**
	 * Actualise les valeurs des Labels
	 */
	public void actualiser()
	{
		int cptLabel = 0 ;
		for ( int cellule : plateau.getCellules() )
		{
			if ( cellule != 0 )
				labelCellules.get(cptLabel).setText(cellule+"");
			else
				labelCellules.get(cptLabel).setText("");
			
			apparenceLabel(labelCellules.get(cptLabel));
			cptLabel++ ;
		}
		
	}
	
	/**
	 * Permet de Quitter ou de Recommencer � la fin d'une partie
	 */
	public void finDuJeu() 
	{
		actualiser();
		
		int scoreMax = 0 ;
		for ( int val : plateau.getCellules())
			if (val > scoreMax) 
				scoreMax = val ;
			
		String[] options = {"Recommencer", "Expectimax", "Quitter"};
		int choix = JOptionPane.showOptionDialog(null,  "C'est termin�. Score Max: "+scoreMax, "Fin du Jeu", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, "Recommencer") ;
	
		if(choix == 0 )
		{
			this.plateau.debut();
			actualiser();
		}
		else if ( choix == 1 )
		{
			this.plateau.debut();
			actualiser();
			lancerExpectimax();
		}
		else
			System.exit(0);

	}
	
	/**
	 * Permet de Quitter ou de Recommencer � la fin d'une partie
	 */
	public void finDuTest() 
	{
		try 
		{
			actualiser();
			
			int scoreMax = 0 ;
			for ( int val : plateau.getCellules())
				if (val > scoreMax) 
					scoreMax = val ;
				
			if (essai <= 100)
			{
				file.write("Essai "+essai+": "+scoreMax+"\n");
				resultat.add(scoreMax);
				
				this.plateau.debut();
				actualiser();
				lancerExpectimax();
			}
			else		
			{
				int nb4096=0, nb2048=0, nb1024=0, nb512=0, nb256=0, nb128=0, nb64=0 ;
				for ( int i : resultat)
				{
					switch (i) 
					{
						case 4096:
							nb4096++;
							break;
						case 2048:
							nb2048++;
							break;
						case 1024:
							nb1024++;
							break;
						case 512:
							nb512++;
							break;
						case 256:
							nb256++;
							break;
						case 128:
							nb128++;
							break;
						case 64:
							nb64++;
							break;
						default:
							break;
					}
				}
				
				file.write("\nResultat : ");
				file.write("\n4096="+nb4096);
				file.write("\n2048="+nb2048);
				file.write("\n1024="+nb1024);
				file.write("\n512="+nb512);
				file.write("\n256="+nb256);
				file.write("\n128="+nb128);
				file.write("\n64="+nb64);
				
				file.close();

				System.exit(0);
			}
			
		}
		catch (IOException e) {e.printStackTrace();}

	}
	

	public static void main(String[] args)
	{
		if ( args.length == 0 )
			new Controleur() ;
		else if ( args[0].equals("-c"))
		{
			Plateau p = new Plateau() ;
			p.modeConsole();			
		}
		else
			System.out.println("Cette Argument n'existe pas.\nUsage:\t-c = Mode Console");
		
	}


}

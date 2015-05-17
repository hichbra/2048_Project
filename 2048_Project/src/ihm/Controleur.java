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
import metier.MonteCarlo;
import metier.Plateau;

@SuppressWarnings("serial")
public class Controleur extends JFrame
{
	public static int essai ;
	public int nbEssai ;
	public ArrayList<Integer> resultat ;
	public boolean logMode ;
	
	public int profondeur ;
	
	private FileWriter file ;
	private Plateau plateau ;
	private ArrayList<JLabel> labelCellules ;
	
	public Controleur(boolean log, String nomFichierLog, boolean graphique, int methode, int nbEssai, int prof)
	{
		super("2048_Project");
		
		this.logMode = log ;
		essai = 1 ;
		this.nbEssai = nbEssai ;
		this.resultat = new ArrayList<Integer>() ;
		this.profondeur = prof ;
		
		if ( log )
		{
			try {
				this.file = new FileWriter(new File(nomFichierLog), true);
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		this.plateau = new Plateau();
		
		if ( graphique )
		{	
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
		
			if ( methode == 1 )
				lancerExpectimax();
			else if ( methode == 2 )
				lancerMonteCarlo();
			else if ( methode == 3 )
				lancerAleatoire();
		}
		else
			this.plateau.modeConsole(methode, prof, essai, nbEssai, logMode, file, resultat);

	}
	
	private void lancerMonteCarlo() 
	{
		System.out.println(essai);
		long startTime = System.currentTimeMillis();
		boolean fin = false ;
		boolean mouvPossible = true ;
		//boolean egaliteDebloquer = false; // Lorsque MonteCarlo revoie toujours la meme direction et que celle ci est impossible : arrive dans le cas ou le dernier coup a jouer conluera la partie peut importe la direction, et que la direction renvoyer par defaut est impossible
		
		while(!fin) // Tant que l'on peut jouer
		{
			int dir = MonteCarlo.monteCarlo(plateau.getShortTableau()) ;

			
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
					System.out.println("Temps total = "+(System.currentTimeMillis()-startTime)+" ms");
					System.out.print("Copie="+Expectimax.tempsCopie);
					System.out.print(" TempsGradient="+Expectimax.tempsGradient);
					System.out.print(" GetPositionLibre="+Expectimax.tempsGetPositionLibre);
					System.out.println(" Deplacement="+Expectimax.tempsDeplacement+"\n");

					Expectimax.tempsCopie=0;
					Expectimax.tempsGradient=0;
					Expectimax.tempsGetPositionLibre=0;
					Expectimax.tempsDeplacement=0 ;
					finDuTest(2);
				}
			}
			
			actualiser();
		}
	}
	
	private void lancerAleatoire() 
	{
		System.out.println(essai);

		long startTime = System.currentTimeMillis();
		boolean fin = false ;
		boolean mouvPossible = true ;
		while(!fin) // Tant que l'on peut jouer
		{
			int dir = (int)( Math.random()*( 4 - 1 + 1 ) ) + 1;

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
					System.out.println("Temps total = "+(System.currentTimeMillis()-startTime)+" ms");
					System.out.print("Copie="+Expectimax.tempsCopie);
					System.out.print(" TempsGradient="+Expectimax.tempsGradient);
					System.out.print(" GetPositionLibre="+Expectimax.tempsGetPositionLibre);
					System.out.println(" Deplacement="+Expectimax.tempsDeplacement+"\n");

					Expectimax.tempsCopie=0;
					Expectimax.tempsGradient=0;
					Expectimax.tempsGetPositionLibre=0;
					Expectimax.tempsDeplacement=0 ;
					finDuTest(3);
				}
			}
			
			actualiser();
		}
	}
	
	private void lancerExpectimax() 
	{
		System.out.println(essai);

		// Expectimax
		long startTime = System.currentTimeMillis();
		boolean fin = false ;
		boolean mouvPossible = true ;
		while(!fin) // Tant que l'on peut jouer
		{
			int dir = (int)Expectimax.expectimaxDirection(plateau.getShortTableau(), profondeur)[0];
			
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
					System.out.println("Temps total = "+(System.currentTimeMillis()-startTime)+" ms");
					System.out.print("Copie="+Expectimax.tempsCopie);
					System.out.print(" TempsGradient="+Expectimax.tempsGradient);
					System.out.print(" GetPositionLibre="+Expectimax.tempsGetPositionLibre);
					System.out.println(" Deplacement="+Expectimax.tempsDeplacement+"\n");

					Expectimax.tempsCopie=0;
					Expectimax.tempsGradient=0;
					Expectimax.tempsGetPositionLibre=0;
					Expectimax.tempsDeplacement=0 ;

					finDuTest(1);
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
					if ( ! plateau.tourSuivant() ) // Si on ne peut pas passer au tour suivant, le jeu est terminï¿½
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
	 * Permet de Quitter ou de Recommencer ï¿½ la fin d'une partie
	 */
	public void finDuJeu() 
	{
		actualiser();
		
		int scoreMax = 0 ;
		for ( int val : plateau.getCellules())
			if (val > scoreMax) 
				scoreMax = val ;
			
		String[] options = {"Recommencer", "Expectimax", "Quitter"};
		int choix = JOptionPane.showOptionDialog(null,  "C'est terminé. Score Max: "+scoreMax, "Fin du Jeu", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, "Recommencer") ;
	
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
	 * Permet de Quitter ou de Recommencer ï¿½ la fin d'une partie
	 */
	public void finDuTest(int mode) 
	{
		try 
		{
			actualiser();
			
			int scoreMax = 0 ;
			for ( int val : plateau.getCellules())
				if (val > scoreMax) 
					scoreMax = val ;
				
			if (essai <= nbEssai)
			{
				if ( logMode )
				{
					file.write("Essai "+essai+": "+scoreMax+"\n");
					resultat.add(scoreMax);
				}
				essai++ ;
				
				this.plateau.debut();
				actualiser();
				if ( mode == 1 )
					lancerExpectimax();
				else if ( mode == 2 )
					lancerMonteCarlo();
				else if ( mode == 3 )
					lancerAleatoire();
			}
			else
			{
				if ( logMode )
				{
					int nb16384=0, nb8192=0, nb4096=0, nb2048=0, nb1024=0, nb512=0, nb256=0, nb128=0, nb64=0 ;
					for ( int i : resultat)
					{
						switch (i) 
						{
							case 16384:
								nb16384++;
								break;
							case 8192:
								nb8192++;
								break;
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
					file.write("\n16384="+nb16384);
					file.write("\n8192="+nb8192);
					file.write("\n4096="+nb4096);
					file.write("\n2048="+nb2048);
					file.write("\n1024="+nb1024);
					file.write("\n512="+nb512);
					file.write("\n256="+nb256);
					file.write("\n128="+nb128);
					file.write("\n64="+nb64);
					
					file.close();
				}
				System.exit(0);
			}
			
		}
		catch (IOException e) {e.printStackTrace();}

	}
	

	public static void main(String[] args)
	{		
		boolean log = false ;
		String nomFichierLog = null;
		boolean graphique = true;
		int methode = 0 ;
		int nbEssai = 100;
		int profondeur = 2;
		
		for (int i = 0 ; i < args.length ; i++)
		{
			if ( args[i].equals("-l") )
			{
				log = true ;
				i++;
				if ( i < args.length && ! args[i].contains("-") )
					nomFichierLog = args[i];
				else
				{
					System.out.println("Erreur !\nUsage:\tlog : -l <nom de fichier>");
					System.exit(1);
				}
			}
			else if ( args[i].equals("-c") )
				graphique = false ;
			else if ( args[i].equals("-e") )
			{
				if ( methode == 0 )
				{
					methode = 1;
					i++;
					if ( i < args.length &&  ! args[i].contains("-"))
					{
						try
						{
							nbEssai = Integer.parseInt(args[i]);
							i++;
							if (  i < args.length && ! args[i].contains("-"))
							{
								try
								{
									profondeur = Integer.parseInt(args[i]);
								}
								catch(NumberFormatException e)
								{
									System.out.println("Erreur !\nPronfondeur pour Expectimax invalide");
									System.exit(6);
								}
							}
							else
							{
								System.out.println("Erreur !\nUsage:\tExpectimax: -e <nbEssai> <profondeur>");
								System.exit(8);
							}
						
						}
						catch(NumberFormatException e)
						{
							System.out.println("Erreur !\nNombre d'essai invalide pour Expectimax invalide");
							System.exit(5);
						} 
					}
					else
					{
						System.out.println("Erreur !\nUsage:\tExpectimax: -e <nbEssai> <profondeur>");
						System.exit(7);
					}
				}
				else
				{
					System.out.println("Erreur !\nVous ne pouvez pas cumuler plusieurs méthodes");
					System.exit(2);
				}
				
			}
			else if ( args[i].equals("-m") )
			{
				if ( methode == 0 )
				{
					methode = 2;
					i++;
					if ( i < args.length &&  ! args[i].contains("-"))
					{
						try
						{
							nbEssai = Integer.parseInt(args[i]);
						}
						catch(NumberFormatException e){System.out.println("Erreur !\nNombre d'essai invalide pour Monte-Carlo invalide");
						System.exit(11);}
					}
				}
				else
				{
					System.out.println("Erreur !\nUsage:\tMonte-Carlo: -a <nbEssai>");
					System.exit(9);
				}
			}
			else if ( args[i].equals("-a") )
			{
				if ( methode == 0 )
				{
					methode = 3;
					i++;
					if ( i < args.length &&  ! args[i].contains("-"))
					{
						try
						{
							nbEssai = Integer.parseInt(args[i]);
						}
						catch(NumberFormatException e){System.out.println("Erreur !\nNombre d'essai invalide pour Aleatoire invalide");
						System.exit(10);}
					}
				}
				else
				{
					System.out.println("Erreur !\nUsage:\tAleatoire: -a <nbEssai>");
					System.exit(9);
				}
			}
			else
			{
				System.out.println("Cet Argument n'existe pas.\nUsage:\t-c = Mode Console\n\t-l <fichier> = logs\n\t-e <nbEssai> <profondeur> = Expectimax\n\t-m <nbEssai> = Monte-Carlo\n\t-a = Aleatoire <nbEssai>");
				System.exit(4);
			}
			
		}
		
		if ( log && methode == 0)
		{
			System.out.println("Erreur !\nVous devez appliquer une méthode pour faire des logs");
			System.exit(6);
		}
		
		if ( !log )
			nbEssai--;
		
		new Controleur(log, nomFichierLog, graphique, methode, nbEssai, profondeur) ;
	
	}
	/*
	-g graphique
	-e expectimax
	-l log suivie du nomfichier
	*/

}

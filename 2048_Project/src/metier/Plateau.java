package metier;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe métier du 2048
 * @author Hichbra
 *
 */
public class Plateau
{
	int[][] plateau ;
	
	public Plateau()
	{
		this.plateau = new int[4][4];
		
		initPlateau();
	}
	
	public Plateau(Plateau p)
	{
		this.plateau = new int[4][4] ;
		
		for( int ligne = 0 ; ligne < p.plateau.length ; ligne++)
		{
			int[] nouvelleLigne = new int[4];
			for ( int colonne = 0 ; colonne < p.plateau[0].length ; colonne++)
				nouvelleLigne[colonne] = p.plateau[ligne][colonne];
			
			plateau[ligne] = nouvelleLigne ;	
		}
	}
	
	/**
	 * Lance la partie en mode Console
	 * @param resultat 
	 * @param file 
	 */
	public void modeConsole(int methode, int prof, int essai, int nbEssai, boolean log, FileWriter file, ArrayList<Integer> resultat)
	{
		debut();
		Scanner scanner = new Scanner(System.in);
		
		boolean finDuJeu = false ;
		while ( ! finDuJeu )
		{
			System.out.println("\nMouvement\n");
            System.out.println("(Z) - Haut");
            System.out.println("(Q) - Gauche");
            System.out.println("(S) - Bas");
            System.out.println("(D) - Droite");
            System.out.println("Votre Action:\t");

            String mouvement = "" ;
            if ( methode == 0 )
            {
            	mouvement = scanner.nextLine();
            }
            else if ( methode == 1 )
            {
            	int dir = (int)Expectimax.expectimaxDirection(getShortTableau(), prof)[0];
            	switch(dir)
    			{
    				case 1:
    					mouvement = "Q";
    					break;
    				case 2:
    					mouvement = "D";
    					break;
    				case 3:
    					mouvement = "Z";
    					break;
    				case 4:
    					mouvement = "S";
    					break;
    				default:
    					mouvement = "";
    					break;
    			}
            }
            else if ( methode == 2 )
            {
            	int dir = (int) MonteCarlo.monteCarlo(getShortTableau());
            	switch(dir)
    			{
    				case 1:
    					mouvement = "Q";
    					break;
    				case 2:
    					mouvement = "D";
    					break;
    				case 3:
    					mouvement = "Z";
    					break;
    				case 4:
    					mouvement = "S";
    					break;
    				default:
    					mouvement = "";
    					break;
    			}
            }
            else if ( methode == 3 )
            {
            	int dir = (int)( Math.random()*( 4 - 1 + 1 ) ) + 1;
            	switch(dir)
    			{
    				case 1:
    					mouvement = "Q";
    					break;
    				case 2:
    					mouvement = "D";
    					break;
    				case 3:
    					mouvement = "Z";
    					break;
    				case 4:
    					mouvement = "S";
    					break;
    				default:
    					mouvement = "";
    					break;
    			}
            }
            	
        	mouvement = mouvement.toLowerCase();
			if ( mouvement.equals("q") )
			{
				if ( gauche() ) // si le mouvement est possible, on passe au tour suivant
					if ( ! tourSuivant() ) // Si on ne peut pas passer au tour suivant, le jeu est terminé
						if ( methode != 0 )
						{
							finDuJeu = finDuTestConsole(methode, prof, essai, nbEssai, log, file, resultat);
							essai++;
						}
						else
							finDuJeu = finDuJeuModeConsole();
			}
			else if ( mouvement.equals("d") )
			{
				if ( droite() )
					if ( ! tourSuivant() )
						if ( methode != 0 )
						{
							finDuJeu = finDuTestConsole(methode, prof, essai, nbEssai, log, file, resultat);
							essai++;
						}
						else
							finDuJeu = finDuJeuModeConsole();
			}
			else if ( mouvement.equals("z") )
			{
				if ( haut() )
					if ( ! tourSuivant() ) 
						if ( methode != 0 )
						{
							finDuJeu = finDuTestConsole(methode, prof, essai, nbEssai, log, file, resultat);
							essai++;
						}
						else
							finDuJeu = finDuJeuModeConsole();
			}
			else if ( mouvement.equals("s") )
			{
				if ( bas() )
					if ( ! tourSuivant() ) 
						if ( methode != 0 )
						{
							finDuJeu = finDuTestConsole(methode, prof, essai, nbEssai, log, file, resultat);
							essai++;
						}
						else
							finDuJeu = finDuJeuModeConsole();
			}
			else
			{
				System.out.println("Cette commande n'existe pas ! Veuillez réessayer !\n");
			}
			
			System.out.println(toString());
		}
		
		scanner.close();
		
	}
	
	/**
	 * Permet de Quitter ou de Recommencer à la fin d'une partie
	 * @return recommencer
	 */
	private boolean finDuJeuModeConsole() 
	{
		System.out.println("\nFin du Jeu !\n");
        System.out.println("(R) - Recommencer");
        System.out.println("Autre - Quitter");
        
		Scanner scanner = new Scanner(System.in);
		String choix = scanner.nextLine();
		scanner.close();
		
		if(choix.equals("R"))
		{
			this.debut();
			return false ;
		}
		else
			return true ;

	}
	
	public boolean finDuTestConsole(int mode, int prof, int essai, int nbEssai, boolean logMode, FileWriter file, ArrayList<Integer> resultat)
	{
		try 
		{							
			int scoreMax = 0 ;
			for ( int val : getCellules())
				if (val > scoreMax) 
					scoreMax = val ;
			
			if (essai <= nbEssai)
			{
				if ( logMode )
				{
					file.write("Essai "+essai+": "+scoreMax+"\n");
					resultat.add(scoreMax);
				}
				debut();

				return false ;

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
				return true ;
			}
			
		}
		catch (IOException e) {e.printStackTrace();return false ;}
	}

	/**
	 * Initialisation du tableau à 0
	 */
	private void initPlateau()
	{
		for( int[] ligne : plateau ) // pour toutes les lignes
		{
			// On met les cases à 0
			for( int i = 0 ; i < 4 ; i++ )
				ligne[i] = 0;
		}	
		
		// bug gauche
		/*plateau.get(0).set(0, 2048);
		plateau.get(0).set(1, 1024);
		plateau.get(0).set(2, 512);
		plateau.get(0).set(3, 256);
		plateau.get(1).set(0, 128);*/
	}
	
	/**
	 * Rempli un tableau de test
	 */
	public void plateauTest(int num)
	{
		initPlateau();
		
		if ( num == 1 )
		{
			plateau[0][0] = 0 ;
			plateau[0][1] = 128 ;
			plateau[0][2] = 256 ;
			plateau[0][3] = 512 ;
		}
		else if ( num == 2 )
		{
			plateau[0][0] = 0 ;
			plateau[0][1] = 128 ;
			plateau[0][2] = 256 ;
			plateau[0][3] = 512 ;
			plateau[1][0] = 0 ;
			plateau[1][1] = 128 ;
			plateau[1][2] = 256 ;
			plateau[1][3] = 512 ;
			plateau[2][0] = 0 ;
			plateau[2][1] = 128 ;
			plateau[2][2] = 256 ;
			plateau[2][3] = 512 ;
		}
		else if ( num == 3 )
		{
			plateau[0][0] = 2 ;
			plateau[0][1] = 0 ;
			plateau[0][2] = 2 ;
			plateau[0][3] = 2 ;
			
			plateau[2][2] = 2 ;
			
			plateau[3][0] = 2 ;
			plateau[3][1] = 0 ;
			plateau[3][2] = 2 ;
			plateau[3][3] = 2 ;
		}
	}
	
	/**
	 * Initialise le jeu en mettant 2 chiffres aleatoires sur le plateau.
	 */
	public void debut()
	{
		// Reset du tableau
		initPlateau() ;
		
		tourSuivant() ;
		tourSuivant() ;
	}
	
	
	/**
	 * Tire un nouveau nombre aleatoire et le met sur le plateau.
	 * Si c'est impossible, la methode renvoie false.
	 * @return possible
	 */
	public boolean tourSuivant()
	{
		int posAleatoire = positionAleatoire() ;
		
		if ( posAleatoire != -1 )
		{
			int lignePosAleatoire 	= (int) Math.ceil(((double)posAleatoire)/4);// recupère la ligne du nombre tirée
			int colPosAleatoire 	= posAleatoire%4 ;							// recupère la colonne du nombre tirée
			// Si le nombre tiré est en fin de ligne
			if ( colPosAleatoire == 0 )
				colPosAleatoire = 4 ;
	
			plateau[lignePosAleatoire-1][colPosAleatoire-1] = tirageAleatoire();
			
			//System.out.println(this);
			// On regarde si le nombre apparu complete la grille
			boolean dernierCoup = true ;
			if(plateauContient(0))
				dernierCoup = false ;
		
			// si la grille est pleine
			if (dernierCoup)
			{
				boolean finDuJeu = true ;
						
				for ( int ligne = 0 ; ligne < 4 ; ligne++ )
				{
					for ( int colonne = 0 ; colonne < 4 ; colonne++ )
					{
						// si on peut fusionner a Gauche
						if ( colonne != 0 && plateau[ligne][colonne] == plateau[ligne][colonne-1] )
							finDuJeu = false ; // Ce n'est pas fini
						
						// Ou a Droite
						if ( colonne != 3 && plateau[ligne][colonne] == plateau[ligne][colonne+1] )
							finDuJeu = false ; // Ce n'est pas fini
						
						// Ou en Haut
						if ( ligne != 0 && plateau[ligne][colonne] == plateau[ligne-1][colonne] )
							finDuJeu = false ; // Ce n'est pas fini

						// Ou en Bas
						if ( ligne != 3 && plateau[ligne][colonne]== plateau[ligne+1][colonne] )
							finDuJeu = false ; // Ce n'est pas fini
						
						if( ! finDuJeu )
							break ;
					}
				}
				//System.out.println("Fin du jeu = "+finDuJeu);
			
				if ( finDuJeu )
					return false ;
			}
					
			return true ;
		}
		else
			return false ;
	}
	
	/**
	 * Teste si le plateau contient le nombre passé en argument
	 * @param ligne
	 * @param i
	 * @return contient
	 */
	private boolean plateauContient(int i) 
	{
		for(int[] ligne: plateau)
			for(int cellule : ligne)
				if ( cellule == i )
					return true ;
		
		return false;
	}

	/**
	 * Tire un nouveau nombre aleatoire à mettre dans le plateau.
	 * 90% de chance de tomber sur un 2.
	 * 10% de chance de tomber sur un 4.
	 * @return tirage
	 */
	private int tirageAleatoire()
	{
		int tirage = (int)( Math.random()*( 9 - 0 + 1 ) ) + 0;
		
		if ( tirage < 9 )
			return 2 ;
		else
			return 4 ;
	}
	
	/**
	 * Tire un nombre aleatoire indiquant une position libre sur le plateau.
	 * En cas d'impossiblité de tirer une case libre, la méthode retourne -1.
	 * @return position
	 */
	private int positionAleatoire()
	{
		// On veut tirer le numero parmis les cases inoccupée, on commence donc par les récupérés
		ArrayList<Integer> caseLibre = positionLibres();
	
		
		// Tirage d'une cellule libre dans le tableau
		if (! caseLibre.isEmpty() )
		{
			int tirage = (int)( Math.random()*( caseLibre.size()-1 - 0 + 1 ) ) + 0; 
		
			int position = caseLibre.get(tirage);
			return position ;
		}
		else
			return -1;

	}
	
	/**
	 * Met le nombre entré ( 2 ou 4 ) en parametre sur le plateau à la position entré en deuxieme parametre.
	 * Si c'est impossible, la methode renvoie false.
	 * @return possible
	 */
	public void tourSuivantPrevu(int nombre, int position)
	{
		int lignePosAleatoire 	= (int) Math.ceil(((double)position)/4);// recupère la ligne du nombre 
		int colPosAleatoire 	= position%4 ;							// recupère la colonne du nombre 
		// Si le nombre est en fin de ligne
		if ( colPosAleatoire == 0 )
			colPosAleatoire = 4 ;

		plateau[lignePosAleatoire-1][colPosAleatoire-1] = nombre ;
		
		//System.out.println(this);
	}
	
	/**
	 * Renvoie la libre des cases inoccupée ( case numéroté de 1 à 16 )
	 * @return caseLibre
	 */
	public ArrayList<Integer> positionLibres()
	{
		ArrayList<Integer> caseLibre = new ArrayList<Integer>();
		
		int numCellule = 1 ;
		for ( int[] ligne : plateau )
		{
			for ( int cellule : ligne )
			{
				if ( cellule == 0 )
					caseLibre.add(numCellule);
				
				numCellule++ ;
			}
		}
		
		return caseLibre ;
	}
	
	/**
	 * Toutes les cellules vont à gauche. Si 2 cellules identique se rencontre,
	 * elle fusionne et sa valeur double.
	 * @return possible
	 */
	public boolean gauche()
	{
		boolean possible = false ;
		
		// Pour toutes les lignes
		for ( int[] ligne : plateau )
		{
			// Pour toutes les cellules de la ligne de gauche à droite
			for ( int cellule = 1 ; cellule <= 3 ; cellule++ )
			{
				// On ne déplace pas les cases vides
				if ( ligne[cellule] != 0 )
				{
					/* On décale la cellule vers la gauche jusqu'à rencontrer un obstacle 
					 * ( qui entraine un bloquage ou une fusion ) 
					 */
					int celluleGauche = cellule-1 ;

					while ( celluleGauche >= 0 && ligne[celluleGauche] == 0 )
					{
						possible = true ;

						ligne[celluleGauche] = ligne[celluleGauche+1] ;
						ligne[celluleGauche+1] = 0 ;
						
						celluleGauche--;
					}

					/* si la cellule n'est pas arrivé au bout, c'est parce qu'il a été bloqué
					 * par une autre cellule. Dans ce cas, possibilité de fusion.
					 */
					if ( celluleGauche != -1 )
					{
						// Si les 2 sont égales, elles fusionnent
						if (ligne[celluleGauche] == ligne[celluleGauche+1])
						{
							possible = true ;

							ligne[celluleGauche+1] = 0;
							ligne[celluleGauche] = ligne[celluleGauche]*2;
						}
					}
					

				}
			}
		}
		
		return possible ;
	}
	
	/**
	 * Toutes les cellules vont à droite. Si 2 cellules identique se rencontre,
	 * elle fusionne et sa valeur double.
	 * @return possible
	 */
	public boolean droite()
	{
		boolean possible = false ;
		
		// Pour toutes les lignes
		for ( int[] ligne : plateau )
		{
			// Pour toutes les cellules de la ligne de droite à gauche
			for ( int cellule = 2 ; cellule >= 0 ; cellule-- )
			{
				// On ne déplace pas les cases vides
				if ( ligne[cellule] != 0 )
				{
					/* On décale la cellule vers la droite jusqu'à rencontrer un obstacle 
					 * ( qui entraine un bloquage ou une fusion ) 
					 */
					int celluleDroite = cellule+1 ;

					while ( celluleDroite <= 3 && ligne[celluleDroite] == 0 )
					{
						possible = true ;

						ligne[celluleDroite] = ligne[celluleDroite-1] ;
						ligne[celluleDroite-1] = 0 ;
						
						celluleDroite++;
						
					}

					/* Si la cellule n'est pas arrivé au bout, c'est parce qu'il a été bloqué
					 * par une autre cellule. Dans ce cas, possibilité de fusion.
					 */
					if ( celluleDroite != 4 )
					{
						// Si les 2 sont égales, elles fusionnent
						if (ligne[celluleDroite-1] == ligne[celluleDroite])
						{
							possible = true ;

							ligne[celluleDroite-1] = 0;
							ligne[celluleDroite] = ligne[celluleDroite]*2;
						}
					}
				}
			}
		}
		
		return possible ;
	}
	
	/**
	 * Toutes les cellules vont en haut. Si 2 cellules identique se rencontre,
	 * elle fusionne et sa valeur double.
	 * @return possible
	 */
	public boolean haut()
	{
		boolean possible = false ;
		
		// Pour toutes les colonnes
		for ( int colonne = 0 ; colonne < 4 ; colonne++ )
		{
			// Pour toutes les cellules de la colonne de haut en bas
			for( int ligne = 1 ; ligne < 4 ; ligne++ )
			{
				// On ne déplace pas les cases vides
				if ( plateau[ligne][colonne] != 0 )
				{
					/* On décale la cellule vers le haut jusqu'à rencontrer un obstacle 
					 * ( qui entraine un bloquage ou une fusion ) 
					 */
					int celluleHaut = ligne-1 ;

					while ( celluleHaut >= 0 && plateau[celluleHaut][colonne] == 0 )
					{
						possible = true ;

						plateau[celluleHaut][colonne] = plateau[celluleHaut+1][colonne];
						plateau[celluleHaut+1][colonne] = 0;
						
						celluleHaut--;
						
					}

					/* Si la cellule n'est pas arrivé au bout, c'est parce qu'il a été bloqué
					 * par une autre cellule. Dans ce cas, possibilité de fusion.
					 */
					if ( celluleHaut != -1 )
					{
						// Si les 2 sont égales, elles fusionnent
						
						if (plateau[celluleHaut][colonne] == plateau[celluleHaut+1][colonne] )
						{
							possible = true ;

							plateau[celluleHaut+1][colonne] = 0;
							plateau[celluleHaut][colonne] = plateau[celluleHaut][colonne]*2;
						}
					}
					
				}
			}
		}
		
		return possible ;
	}
	
	/**
	 * Toutes les cellules vont en bas. Si 2 cellules identique se rencontre,
	 * elle fusionne et sa valeur double.
	 * @return possible
	 */
	public boolean bas()
	{
		boolean possible = false ;
		
		// Pour toutes les colonnes
		for ( int colonne = 0 ; colonne < 4 ; colonne++ )
		{
			// Pour toutes les cellules de la colonne de bas en haut
			for( int ligne = 2 ; ligne >= 0 ; ligne-- )
			{
				// On ne déplace pas les cases vides
				if ( plateau[ligne][colonne] != 0 )
				{
					/* On décale la cellule vers le bas jusqu'à rencontrer un obstacle 
					 * ( qui entraine un bloquage ou une fusion ) 
					 */
					int celluleBas = ligne+1 ;

					while ( celluleBas <= 3 && plateau[celluleBas][colonne] == 0 )
					{
						possible = true ;

						plateau[celluleBas][colonne] = plateau[celluleBas-1][colonne];
						plateau[celluleBas-1][colonne] = 0;
						
						celluleBas++;
						
					}

					/* Si la cellule n'est pas arrivé au bout, c'est parce qu'il a été bloqué
					 * par une autre cellule. Dans ce cas, possibilité de fusion.
					 */
					if ( celluleBas != 4 )
					{
						// Si les 2 sont égales, elles fusionnent
						
						if (plateau[celluleBas][colonne] == plateau[celluleBas-1][colonne] )
						{
							possible = true ;

							plateau[celluleBas-1][colonne] = 0;
							plateau[celluleBas][colonne] = plateau[celluleBas][colonne]*2;
						}
					}
				}
			}
		}
		
		return possible ;
	}
	
	/**
	 * Retourne un tableau contenant la valeur des cases du plateau 
	 * @return cellules
	 */
	public ArrayList<Integer> getCellules()
	{
		ArrayList<Integer> cellules = new ArrayList<Integer>() ;
		
		for ( int[] ligne : plateau )
			for ( int cellule : ligne )
				cellules.add(cellule);
		
		return cellules ;
		
	}
	
	public int[][] getPlateau()
	{
		return this.plateau;
	}
	
	public String toString()
	{
		String s = "" ;
		
		for( int[] ligne : plateau ) // pour toutes les lignes
		{
			for ( int cellule : ligne ) // pour toutes les cellules de la ligne
				s += cellule+" ";
			
			s += "\n" ;
		}
		
		return s ;
	}
	
	
	/**
	 * Convertie le plateau en un tableau de short
	 * @return shortTableau
	 */
	public short[] getShortTableau() {
		int i=0;
		short [] shortTableau = new short[16];
		for (int []ligne : plateau){
			for (int valeur : ligne){
				shortTableau[i]=(short) valeur;
				i++;
			}
		}
		return shortTableau;
	}
	
	
}

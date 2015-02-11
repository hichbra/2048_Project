package metier;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe métier du 2048
 * @author Hichbra
 *
 */
public class Plateau
{
	ArrayList<ArrayList<Integer>> plateau ;
	
	public Plateau()
	{
		this.plateau = new ArrayList<ArrayList<Integer>>();
		
		// Creation des lignes du plateau
		for( int i = 0 ; i < 4 ; i++ )
			this.plateau.add(new ArrayList<Integer>());
		
		initPlateau();
	}
	
	/**
	 * Lance la partie en mode Console
	 */
	public void modeConsole()
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

			String mouvement = scanner.nextLine();
			
			if ( mouvement.equals("Q") )
			{
				if ( gauche() ) // si le mouvement est possible, on passe au tour suivant
					if ( ! tourSuivant() ) // Si on ne peut pas passer au tour suivant, le jeu est terminé
						finDuJeu = finDuJeuModeConsole();
			}
			else if ( mouvement.equals("D") )
			{
				if ( droite() )
					if ( ! tourSuivant() )
						finDuJeu = finDuJeuModeConsole();
			}
			else if ( mouvement.equals("Z") )
			{
				if ( haut() )
					if ( ! tourSuivant() ) 
						finDuJeu = finDuJeuModeConsole();
			}
			else if ( mouvement.equals("S") )
			{
				if ( bas() )
					if ( ! tourSuivant() ) 
						finDuJeu = finDuJeuModeConsole();
			}
			else
			{
				System.out.println("Cette commande n'existe pas ! Veuillez réessayer !\n");
			}
			
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

	/**
	 * Initialisation du tableau à 0
	 */
	private void initPlateau()
	{
		for( ArrayList<Integer> ligne : plateau ) // pour toutes les lignes
		{
			// On les cases à 0
			for( int i = 0 ; i < 4 ; i++ )
				ligne.add(0);
		}	
		
		// bug gauche
		/*plateau.get(0).set(0, 2048);
		plateau.get(0).set(1, 1024);
		plateau.get(0).set(2, 512);
		plateau.get(0).set(3, 256);
		plateau.get(1).set(0, 128);*/
	
	}
	
	/**
	 * Initialise le jeu en mettant 2 chiffres aleatoires sur le plateau.
	 */
	public void debut()
	{
		// Reset du tableau
		for( ArrayList<Integer> ligne : plateau ) // pour toutes les lignes
			for( int i = 0 ; i < 4 ; i++ )
				ligne.set(i, 0) ;
		
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
	
			plateau.get(lignePosAleatoire-1).set(colPosAleatoire-1, tirageAleatoire());
			
			System.out.println(this);
			// On regarde si le nombre apparu complete la grille
			boolean dernierCoup = true ;
			for ( ArrayList<Integer> ligne : plateau )
				if( ligne.contains(0))
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
						if ( colonne != 0 && plateau.get(ligne).get(colonne).intValue() == plateau.get(ligne).get(colonne-1).intValue() )
							finDuJeu = false ; // Ce n'est pas fini
						
						// Ou a Droite
						if ( colonne != 3 && plateau.get(ligne).get(colonne).intValue() == plateau.get(ligne).get(colonne+1).intValue() )
							finDuJeu = false ; // Ce n'est pas fini
						
						// Ou en Haut
						if ( ligne != 0 && plateau.get(ligne).get(colonne).intValue() == plateau.get(ligne-1).get(colonne).intValue() )
							finDuJeu = false ; // Ce n'est pas fini

						// Ou en Bas
						if ( ligne != 3 && plateau.get(ligne).get(colonne).intValue() == plateau.get(ligne+1).get(colonne).intValue() )
							finDuJeu = false ; // Ce n'est pas fini
						
						if( ! finDuJeu )
							break ;
					}
				}
				System.out.println("Fin du jeu = "+finDuJeu);
			
				if ( finDuJeu )
					return false ;
			}
					
			return true ;
		}
		else
			return false ;
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
		ArrayList<Integer> caseLibre = new ArrayList<Integer>();
		
		// On ajoute les cases inoccupée dans l'arraylist pour tirer le numero parmis elles
		int numCellule = 1 ;
		for ( ArrayList<Integer> ligne : plateau )
		{
			for ( int cellule : ligne )
			{
				if ( cellule == 0 )
					caseLibre.add(numCellule);
				
				numCellule++ ;
			}
		}
		
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
	 * Toutes les cellules vont à gauche. Si 2 cellules identique se rencontre,
	 * elle fusionne et sa valeur double.
	 * @return possible
	 */
	public boolean gauche()
	{
		boolean possible = false ;
		
		// Pour toutes les lignes
		for ( ArrayList<Integer> ligne : plateau )
		{
			// Pour toutes les cellules de la ligne de gauche à droite
			for ( int cellule = 1 ; cellule <= 3 ; cellule++ )
			{
				// On ne déplace pas les cases vides
				if ( ligne.get(cellule) != 0 )
				{
					/* On décale la cellule vers la gauche jusqu'à rencontrer un obstacle 
					 * ( qui entraine un bloquage ou une fusion ) 
					 */
					int celluleGauche = cellule-1 ;

					while ( celluleGauche >= 0 && ligne.get(celluleGauche) == 0 )
					{
						possible = true ;

						ligne.set(celluleGauche, ligne.get(celluleGauche+1)) ;
						ligne.set(celluleGauche+1, 0) ;
						
						celluleGauche--;
					}

					/* si la cellule n'est pas arrivé au bout, c'est parce qu'il a été bloqué
					 * par une autre cellule. Dans ce cas, possibilité de fusion.
					 */
					if ( celluleGauche != -1 )
					{
						// Si les 2 sont égales, elles fusionnent
						if (ligne.get(celluleGauche).intValue() == ligne.get(celluleGauche+1).intValue())
						{
							possible = true ;

							ligne.set(celluleGauche+1, 0);
							ligne.set(celluleGauche, ligne.get(celluleGauche)*2);
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
		for ( ArrayList<Integer> ligne : plateau )
		{
			// Pour toutes les cellules de la ligne de droite à gauche
			for ( int cellule = 2 ; cellule >= 0 ; cellule-- )
			{
				// On ne déplace pas les cases vides
				if ( ligne.get(cellule) != 0 )
				{
					/* On décale la cellule vers la droite jusqu'à rencontrer un obstacle 
					 * ( qui entraine un bloquage ou une fusion ) 
					 */
					int celluleDroite = cellule+1 ;

					while ( celluleDroite <= 3 && ligne.get(celluleDroite) == 0 )
					{
						possible = true ;

						ligne.set(celluleDroite, ligne.get(celluleDroite-1)) ;
						ligne.set(celluleDroite-1, 0) ;
						
						celluleDroite++;
						
					}

					/* Si la cellule n'est pas arrivé au bout, c'est parce qu'il a été bloqué
					 * par une autre cellule. Dans ce cas, possibilité de fusion.
					 */
					if ( celluleDroite != 4 )
					{
						// Si les 2 sont égales, elles fusionnent
						if (ligne.get(celluleDroite-1).intValue() == ligne.get(celluleDroite).intValue())
						{
							possible = true ;

							ligne.set(celluleDroite-1, 0);
							ligne.set(celluleDroite, ligne.get(celluleDroite)*2);
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
				if ( plateau.get(ligne).get(colonne) != 0 )
				{
					/* On décale la cellule vers le haut jusqu'à rencontrer un obstacle 
					 * ( qui entraine un bloquage ou une fusion ) 
					 */
					int celluleHaut = ligne-1 ;

					while ( celluleHaut >= 0 && plateau.get(celluleHaut).get(colonne) == 0 )
					{
						possible = true ;

						plateau.get(celluleHaut).set(colonne, plateau.get(celluleHaut+1).get(colonne));
						plateau.get(celluleHaut+1).set(colonne, 0);
						
						celluleHaut--;
						
					}

					/* Si la cellule n'est pas arrivé au bout, c'est parce qu'il a été bloqué
					 * par une autre cellule. Dans ce cas, possibilité de fusion.
					 */
					if ( celluleHaut != -1 )
					{
						// Si les 2 sont égales, elles fusionnent
						
						if (plateau.get(celluleHaut).get(colonne).intValue() == plateau.get(celluleHaut+1).get(colonne).intValue() )
						{
							possible = true ;

							plateau.get(celluleHaut+1).set(colonne, 0);
							plateau.get(celluleHaut).set(colonne, plateau.get(celluleHaut).get(colonne)*2);
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
				if ( plateau.get(ligne).get(colonne) != 0 )
				{
					/* On décale la cellule vers le bas jusqu'à rencontrer un obstacle 
					 * ( qui entraine un bloquage ou une fusion ) 
					 */
					int celluleBas = ligne+1 ;

					while ( celluleBas <= 3 && plateau.get(celluleBas).get(colonne) == 0 )
					{
						possible = true ;

						plateau.get(celluleBas).set(colonne, plateau.get(celluleBas-1).get(colonne));
						plateau.get(celluleBas-1).set(colonne, 0);
						
						celluleBas++;
						
					}

					/* Si la cellule n'est pas arrivé au bout, c'est parce qu'il a été bloqué
					 * par une autre cellule. Dans ce cas, possibilité de fusion.
					 */
					if ( celluleBas != 4 )
					{
						// Si les 2 sont égales, elles fusionnent
						
						if (plateau.get(celluleBas).get(colonne).intValue() == plateau.get(celluleBas-1).get(colonne).intValue() )
						{
							possible = true ;

							plateau.get(celluleBas-1).set(colonne, 0);
							plateau.get(celluleBas).set(colonne, plateau.get(celluleBas).get(colonne)*2);
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
		
		for ( ArrayList<Integer> ligne : plateau )
			for ( int cellule : ligne )
				cellules.add(cellule);
		
		return cellules ;
		
	}
	
	public String toString()
	{
		String s = "" ;
		
		for( ArrayList<Integer> ligne : plateau ) // pour toutes les lignes
		{
			for ( int cellule : ligne ) // pour toutes les cellules de la ligne
				s += cellule+" ";
			
			s += "\n" ;
		}
		
		return s ;
	}
	
}

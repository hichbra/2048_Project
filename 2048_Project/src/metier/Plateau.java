package metier;

import java.util.ArrayList;

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
		
		
		
		// Config Test 
		plateau.get(0).set(0,2);
		plateau.get(1).set(0,2);
		plateau.get(2).set(0,2);
		plateau.get(3).set(0,2);
		
	}
	
	/**
	 * Initialise le jeu en mettant 2 nouveaux chiffres aleatoires.
	 */
	public void debut()
	{
		int posAleatoire 		= positionAleatoire() ;
		int lignePosAleatoire 	= (int) Math.ceil(((double)posAleatoire)/4);// recupère la ligne du nombre tirée
		int colPosAleatoire 	= posAleatoire%4 ;							// recupère la colonne du nombre tirée
		// Si le nombre tiré est en fin de ligne
		if ( colPosAleatoire == 0 )
			colPosAleatoire = 4 ;

		plateau.get(lignePosAleatoire-1).set(colPosAleatoire-1, tirageAleatoire());
		
		posAleatoire 		= positionAleatoire() ;
		lignePosAleatoire 	= (int) Math.ceil(((double)posAleatoire)/4);// recupère la ligne du nombre tirée
		colPosAleatoire 	= posAleatoire%4 ;							// recupère la colonne du nombre tirée
		// Si le nombre tiré est en fin de ligne
		if ( colPosAleatoire == 0 )
			colPosAleatoire = 4 ;

		plateau.get(lignePosAleatoire-1).set(colPosAleatoire-1, tirageAleatoire());
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
					System.out.println("celluleGauche = "+celluleGauche);

					while ( celluleGauche >= 0 && ligne.get(celluleGauche) == 0 )
					{
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
						if (ligne.get(celluleGauche) == ligne.get(celluleGauche+1))
						{
							ligne.set(celluleGauche+1, 0);
							ligne.set(celluleGauche, ligne.get(celluleGauche)*2);
						}
					}
					
					possible = true ;

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
					System.out.println("celluleDroite = "+celluleDroite);

					while ( celluleDroite <= 3 && ligne.get(celluleDroite) == 0 )
					{
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
						if (ligne.get(celluleDroite-1) == ligne.get(celluleDroite))
						{
							ligne.set(celluleDroite-1, 0);
							ligne.set(celluleDroite, ligne.get(celluleDroite)*2);
						}
					}
					
					possible = true ;

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
						
						if (plateau.get(celluleHaut).get(colonne) == plateau.get(celluleHaut+1).get(colonne) )
						{
							plateau.get(celluleHaut+1).set(colonne, 0);
							plateau.get(celluleHaut).set(colonne, plateau.get(celluleHaut).get(colonne)*2);
						}
					}
					
					possible = true ;
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
						
						if (plateau.get(celluleBas).get(colonne) == plateau.get(celluleBas-1).get(colonne) )
						{
							plateau.get(celluleBas-1).set(colonne, 0);
							plateau.get(celluleBas).set(colonne, plateau.get(celluleBas).get(colonne)*2);
						}
					}
					
					possible = true ;
				}
			}
		}
		
		return possible ;
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
	
	public static void main(String[] args)
	{
		Plateau p = new Plateau() ;
		System.out.println(p);
			
		System.out.println(p.bas()) ;
		System.out.println(p);
		
		System.out.println(p.bas()) ;
		System.out.println(p);
		
		System.out.println(p.bas()) ;
		System.out.println(p);
	}

}

package metier;


public class Expectimax 
{	
	
	public static boolean dernierDeplacement = false ;
	
	public static double[] expectimax(short [] grille, int profondeur)
	{
		//System.out.println("Expectimax profondeur: "+profondeur);
		double scoreMax = -999999 ;
		int meilleurDir = 0 ;
		
		for ( int direction = 1 ; direction <= 4 ; direction++ )
		{
			short [] grilleCopie = copie(grille);
			
			// Direction : 1=gauche | 2=droite | 3=haut | 4=bas
			// Effectue un mouvement sans faire apparaitre les nouveaux nombres
			boolean directionPossible = true ;
			switch(direction)
			{
				case 1:
					grilleCopie=deplacementGauche(grilleCopie);
					break;
				case 2:
					grilleCopie=deplacementDroite(grilleCopie);	
					break;
				case 3:
					grilleCopie=deplacementHaut(grilleCopie);
					break;
				case 4:
					grilleCopie=deplacementBas(grilleCopie);
					break;
				default:
					break;
			}
						
			if ( dernierDeplacement )
			{
				double score = eval(grilleCopie, profondeur-1) ;
						
				if ( score > scoreMax )
				{
					scoreMax = score;
					meilleurDir = direction ;
				}
			}
		}
		
		double[] result = {meilleurDir, scoreMax} ;
		return result;
	}
	
	

//	private static short[] deplacementDroite(short[] grille) {
//		
//		dernierDeplacement = false ;
//		
//		for( int ligne = 0 ; ligne <= 12 ; ligne += 4)
//		{
//			for ( int indice = ligne ; indice < ligne+4 ; indice++ )
//			{
//				if ( indice%4 != 0 )
//				{
//					boolean fusion = false ;
//					for ( int indiceDroite = ligne ; indiceDroite > indice ; indiceDroite++ )
//					{
//						System.out.println("indice D "+indiceDroite);
//						
//						if ( grille[indiceDroite+1] == 0 )
//						{
//							grille[indiceDroite+1] = grille[indiceDroite];
//							grille[indiceDroite] = 0 ;
//							dernierDeplacement = true ;
//						}
//						else if ( grille[indiceDroite+1] == grille[indiceDroite])
//						{
//							if (!fusion)
//							{
//								grille[indiceDroite+1] = (short)(grille[indiceDroite]*2);
//								grille[indiceDroite] = 0 ;
//								fusion = true ;
//								dernierDeplacement = true ;
//
//								System.out.println("fusion "+indice);
//							}
//						}
//						else
//							break;
//					}
//				}
//			}
//		}
//		
//		for( int i = 1 ; i <= 16; i++)
//		{
//			System.out.print(grille[i-1]+" ");
//			if(i%4 ==0)
//				System.out.println();
//			
//		}
//		return grille ;
//	}



	private static short[] deplacementGauche(short[] grille) 
	{
		dernierDeplacement = false ;
		
		for( int ligne = 0 ; ligne <= 12 ; ligne += 4)
		{
			for ( int indice = ligne ; indice < ligne+4 ; indice++ )
			{
				if ( indice%4 != 0 )
				{
					boolean fusion = false ;
					for ( int indiceGauche = indice ; indiceGauche > ligne ; indiceGauche-- )
					{
						if ( grille[indiceGauche-1] == 0 )
						{
							grille[indiceGauche-1] = grille[indiceGauche];
							grille[indiceGauche] = 0 ;
							dernierDeplacement = true ;
							System.out.println("depl "+indice);
						}
						else if ( grille[indiceGauche-1] == grille[indiceGauche])
						{
							if (!fusion)
							{
								grille[indiceGauche-1] = (short)(grille[indiceGauche]*2);
								grille[indiceGauche] = 0 ;
								fusion = true ;
								dernierDeplacement = true ;

								System.out.println("fusion "+indice);
							}
						}
						else
							break;
					}
				}
			}
		}
		
		for( int i = 1 ; i <= 16; i++)
		{
			System.out.print(grille[i-1]+" ");
			if(i%4 ==0)
				System.out.println();
			
		}
		return grille ;
	}


	public 

	private static short[] copie(short[] grille) {
		return grille.clone();
	}



	private static double eval(short [] grille, int profondeur)
	{
		//System.out.println("Eval profondeur: "+profondeur);

		double score = 0 ;
		
		// Calcul des apparitions de 2
		for (int emplacement : grille.positionLibres())
		{
			Plateau grilleCopie = new Plateau(grille);
			grilleCopie.tourSuivantPrevu(2, emplacement) ;
			
			// calcul du score de la grille en ponderant le score avec la probabilitï¿½ d'avoir un 2
			score += ( regle1(grilleCopie)+regle2(grilleCopie)+regle3(grilleCopie)+regle4(grilleCopie)+regle5(grilleCopie) ) * (9.0/10.0) ;
			
			// calcul du score des autres grilles
			if ( profondeur > 0 )
				score += expectimax(grilleCopie, profondeur-1)[1] ;
			
		}
		
		// Calcul des apparitions de 4
		for (int emplacement : grille.positionLibres())
		{
			Plateau grilleCopie = new Plateau(grille);
			grilleCopie.tourSuivantPrevu(4, emplacement) ;
			
			// calcul du score de la grille en ponderant le score avec la probabilitï¿½ d'avoir un 4
			score += ( regle1(grilleCopie)+regle2(grilleCopie)+regle3(grilleCopie)+regle4(grilleCopie)+regle5(grilleCopie) ) * (1.0/10.0) ;
			
			// calcul du score des autres grilles
			if ( profondeur > 0 )
				score += expectimax(grilleCopie, profondeur-1)[1] ;
			
		}
		
		score /= (grille.positionLibres().size()*2) ; // score = score / nb Appartion Possible ( en comptant les 2 et 4 )
		//System.out.println(score);
		
		return score;
	}

	
	
	/**
	 * Regle du coin
	 * @param grille
	 * @return score
	 */
	public static int regle5(Plateau grille) 
	{
		int[][] plateau = grille.getPlateau() ;
		
		int max = 0 ;
		for( int[] ligne : plateau )
			for(int cellule : ligne)
				if( cellule > max )
					max = cellule ;
		
		if( plateau[0][0] == max || plateau[0][3] == max || plateau[3][0] == max || plateau[3][3] == max )
			return 500 ;
		else
			return 0 ;
					
	}
	
	/**
	 * Panalité de mort
	 * @param grille
	 * @return score
	 */
	public static int regle4(Plateau grille) 
	{
		if (grille.positionLibres().size() == 0 )
			return -1000 ;
		else
			return 0 ;
	}

	/**
	 * Maximiser les espaces libres
	 * @param grille
	 * @return score
	 */
	public static int regle3(Plateau grille) 
	{
		return grille.positionLibres().size()*2 ;

	}

	/**
	 * Ligne/colonne dans l'ordre croissant/décroissant + régularité des cellules
	 * @param grille
	 * @return score
	 */
	public static int regle2(Plateau grille)
	{
		int[][] plateau = grille.getPlateau() ;
		
		int score = 0 ;
		
		for( int[] ligne : plateau )
			if ( ( (ligne[1] == ligne[0] || ligne[1] == ligne[0]*2) && (ligne[2] == ligne[1] || ligne[2] == ligne[1]*2) && ( ligne[3] == ligne[2] || ligne[3] == ligne[2]*2) ) // ligne croissante
					|| ( (ligne[3] == ligne[2] || ligne[3]*2 == ligne[2]) && (ligne[2] == ligne[1] || ligne[2]*2 == ligne[1]) && ( ligne[1] == ligne[0] || ligne[1]*2 == ligne[0]) ) ) // ligne decroissante
				score+= 3 ;
				
		
		
		for ( int colonne = 0 ; colonne < 4 ; colonne++ )
			if (  ( (plateau[1][colonne] == plateau[0][colonne] || plateau[1][colonne] == plateau[0][colonne]*2) && (plateau[2][colonne] == plateau[1][colonne] || plateau[2][colonne] == plateau[1][colonne]*2) && ( plateau[3][colonne] == plateau[2][colonne] || plateau[3][colonne] == plateau[2][colonne]*2) ) // colonne croissante
				||  ( (plateau[3][colonne] == plateau[2][colonne] || plateau[3][colonne]*2 == plateau[2][colonne]) && (plateau[2][colonne] == plateau[1][colonne] || plateau[2][colonne]*2 == plateau[1][colonne]) && ( plateau[1][colonne] == plateau[0][colonne] || plateau[1][colonne]*2 == plateau[0][colonne]) ) )  // colonne decroissante
				score+= 3 ;
				
		
		return score;
	}

	/**
	 * Ligne/colonne dans l'ordre croissant/décroissant
	 * @param grille
	 * @return score
	 */
	public static int regle1(Plateau grille) 
	{
		int[][] plateau = grille.getPlateau() ;
		
		int score = 0 ;
		
		for( int[] ligne : plateau )
			if ( (ligne[0] >= ligne[1] && ligne[1] >= ligne[2] && ligne[2] >= ligne[3]) // ligne croissante
					|| (ligne[0] <= ligne[1] && ligne[1] <= ligne[2] && ligne[2] <= ligne[3]) ) // ligne decroissante
				score++ ;
		
		for ( int colonne = 0 ; colonne < 4 ; colonne++ )
			if ( (plateau[0][colonne] >= plateau[1][colonne] && plateau[1][colonne] >= plateau[2][colonne] && plateau[2][colonne] >= plateau[3][colonne]) // colonne croissante
				|| (plateau[0][colonne] <= plateau[1][colonne] && plateau[1][colonne] <= plateau[2][colonne] && plateau[2][colonne] <= plateau[3][colonne]) ) // colonne decroissante
				score++ ;
				
		
		return score;
	}


	public static void main ( String[] args)
	{
		Plateau p = new Plateau() ;
		p.plateauTest(3);
		
		System.out.println(p+"\nEnsuite\n");
		
		Expectimax.deplacementDroite(p.getShortTableau());
				
		/*System.out.println("Regle 1 = "+Expectimax.regle1(p));
		System.out.println("Regle 2 = "+Expectimax.regle2(p));
		System.out.println("Regle 3 = "+Expectimax.regle3(p));
		System.out.println("Regle 4 = "+Expectimax.regle4(p));
		System.out.println("Regle 5 = "+Expectimax.regle5(p));
		
		p.plateauTest(2);
		
		System.out.println(p);
		System.out.println("Regle 1 = "+Expectimax.regle1(p));
		System.out.println("Regle 2 = "+Expectimax.regle2(p));
		System.out.println("Regle 3 = "+Expectimax.regle3(p));
		System.out.println("Regle 4 = "+Expectimax.regle4(p));
		System.out.println("Regle 5 = "+Expectimax.regle5(p));
		*/
	}
}

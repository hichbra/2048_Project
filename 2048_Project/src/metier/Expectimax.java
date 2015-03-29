package metier;

import java.util.ArrayList;


public class Expectimax 
{		
	public static boolean dernierDeplacement = false ;
	/*
	public static double[] expectimax(short[] grille, int profondeur)
	{
		double scoreMax = -999999 ;
		int meilleurDir = 0 ;
		
		if (profondeur != 1)
			System.out.println();
		else
		{
			for ( int direction = 1 ; direction <= 4 ; direction++ )
			{
				short[] grilleCopie = copie(grille);
				
				// Direction : 1=gauche | 2=droite | 3=haut | 4=bas
				// Effectue un mouvement sans faire apparaitre les nouveaux nombres
				switch(direction)
				{
					case 1:
						grilleCopie = deplacementGauche(grilleCopie);
						meilleurDir = 1;
						break;
					case 2:
						grilleCopie = deplacementDroite(grilleCopie);
						meilleurDir = 2;	
						break;
					case 3:
						grilleCopie = deplacementHaut(grilleCopie);
						meilleurDir = 3;
						break;
					case 4:
						grilleCopie = deplacementBas(grilleCopie);
						meilleurDir = 4;
						break;
					default:
						break;
				}
				
				if ( dernierDeplacement )
				{
					double score = eval(grilleCopie, profondeur-1) ;
					//System.out.println("score direction = "+score );		
					if ( score > scoreMax )
					{
						scoreMax = score;
						meilleurDir = direction ;
					}
				}
			}
		}
		
		double[] result = {meilleurDir, scoreMax} ;
		return result;
	}
	
	private static double eval(short[] grille, int profondeur)
	{
		//System.out.println("Eval profondeur: "+profondeur);

		double score = 0 ;
		
		// Calcul des apparitions de 2
		for (int emplacement : getPositionLibres(grille))
		{
			short[] grilleCopie = copie(grille);
			grilleCopie = tourSuivantPrevu(grilleCopie, (short)2, emplacement) ;
			
			// calcul du score de la grille en ponderant le score avec la probabilitï¿½ d'avoir un 2
			score += ( regle1(grilleCopie)+regle2(grilleCopie)+regle3(grilleCopie))/* * (9.0/10.0) ;
			
			// calcul du score des autres grilles
			if ( profondeur > 0 )
				score += expectimax(grilleCopie, profondeur-1)[1] ;
			
			//System.out.println("score emplacement = "+score+" profondeur = "+profondeur );		
		}
		
		// Calcul des apparitions de 4
		/*for (int emplacement : getPositionLibres(grille))
		{
			short[] grilleCopie = copie(grille);
			grilleCopie = tourSuivantPrevu(grilleCopie, (short)4, emplacement) ;
			
			// calcul du score de la grille en ponderant le score avec la probabilitï¿½ d'avoir un 4
			score += ( regle1(grilleCopie)+regle2(grilleCopie)+regle3(grilleCopie)) * (1.0/10.0) ;
			
			// calcul du score des autres grilles
			if ( profondeur > 0 )
				score += expectimax(grilleCopie, profondeur-1)[1] ;
			
		}
		
		score /= (getPositionLibres(grille).size()*2) ; // score = score / nb Appartion Possible ( en comptant les 2 et 4 )
		//System.out.println(score);
		
		return score;
	}*/
	

	
	public static double[] expectimax(short[] grille, int profondeur)
	{
		double scoreMax = -999999 ;
		int meilleurDir = 0 ;
		
		
		for ( int direction = 1 ; direction <= 4 ; direction++ )
		{
			short[] grilleCopie = copie(grille);
			
			// Direction : 1=gauche | 2=droite | 3=haut | 4=bas
			// Effectue un mouvement sans faire apparaitre les nouveaux nombres
			switch(direction)
			{
				case 1:
					grilleCopie = deplacementGauche(grilleCopie);
					break;
				case 2:
					grilleCopie = deplacementDroite(grilleCopie);	
					break;
				case 3:
					grilleCopie = deplacementHaut(grilleCopie);
					break;
				case 4:
					grilleCopie = deplacementBas(grilleCopie);
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
	
	private static double eval(short[] grille, int profondeur)
	{
		//System.out.println("Eval profondeur: "+profondeur);

		double score = 0 ;
		double moyBranche = 0 ;
		
		// Calcul des apparitions de 2
		for (int emplacement : getPositionLibres(grille))
		{
			short[] grilleCopie = copie(grille);
			grilleCopie = tourSuivantPrevu(grilleCopie, (short)2, emplacement) ;
			
			if ( profondeur <= 0 )	
				score += ( regle1(grilleCopie)+regle2(grilleCopie)+regle3(grilleCopie)) ;	// calcul du score de la grille
			else					
				moyBranche += expectimax(grilleCopie, profondeur-1)[1] ;					// calcul du score des autres grilles
			
			
			//System.out.println("score emplacement = "+score+" profondeur = "+profondeur );		
		}
		
		// Calcul des apparitions de 4
		/*for (int emplacement : getPositionLibres(grille))
		{
			short[] grilleCopie = copie(grille);
			grilleCopie = tourSuivantPrevu(grilleCopie, (short)4, emplacement) ;
			
			// calcul du score de la grille en ponderant le score avec la probabilitï¿½ d'avoir un 4
			score += ( regle1(grilleCopie)+regle2(grilleCopie)+regle3(grilleCopie)) * (1.0/10.0) ;
			
			// calcul du score des autres grilles
			if ( profondeur > 0 )
				score += expectimax(grilleCopie, profondeur-1)[1] ;
			
		}*/
		if ( profondeur <= 0 )
		{
			score /= (getPositionLibres(grille).size()*2) ; // score = score / nb Appartion Possible
			return score;	
		}
		else
		{
			moyBranche /= (getPositionLibres(grille).size()*2) ; // branche = branche / nb branche
			return moyBranche;	
		}
		//System.out.println(score);
		
	}

	private static short[] tourSuivantPrevu(short[] grilleCopie, short nombre, int position)
	{
		 grilleCopie[position] = nombre ;
		 
		 return grilleCopie ;
	}

	/*private static byte[] getPositionLibres(short[] grille) 
	{
		byte[] caseLibre = new byte[16];
		
		for ( byte i = 0 , j = 0; i < grille.length ; i++ )
		{
			if ( grille[i] == 0 )
			{
				caseLibre[j] = i ;
				j++;
			}
		}
		
		return caseLibre ;
	}*/
	
	private static ArrayList<Byte> getPositionLibres(short[] grille) 
	{
		ArrayList<Byte> caseLibre = new ArrayList<Byte>();
		
		for ( byte i = 0 ; i < grille.length ; i++ )
		{
			if ( grille[i] == 0 )
			{
				caseLibre.add(i) ;
			}
		}
		
		return caseLibre ;
	}

	private static short[] deplacementBas(short[] grille) 
	{
		dernierDeplacement = false ;
		
		for( int colonne = 3 ; colonne >= 0 ; colonne--)
		{
			for( int indice = 15-colonne ; indice >= 0 ; indice -= 4 )
			{
				boolean fusion = false ;
				for ( int indiceBas = indice+4 ; indiceBas <= 15-colonne ; indiceBas+=4 )
				{
					if ( grille[indiceBas] == 0 )
					{
						grille[indiceBas] = grille[indiceBas-4];
						grille[indiceBas-4] = 0 ;
						
						if ( grille[indiceBas] != 0 || grille[indiceBas-4] != 0 )
							dernierDeplacement = true ;
					}
					else if ( grille[indiceBas-4] == grille[indiceBas])
					{
						if (!fusion)
						{
							grille[indiceBas] = (short)(grille[indiceBas-4]*2);
							grille[indiceBas-4] = 0 ;
							//scoreGlobal += (int)(grille[indiceBas]);
							fusion = true ;
							
							if ( grille[indiceBas] != 0 || grille[indiceBas-4] != 0 )
								dernierDeplacement = true ;
						}
					}
					else
						break;
				}
			}
		}

		return grille;
	}

	private static short[] deplacementHaut(short[] grille) 
	{
		dernierDeplacement = false ;
		
		for( int colonne = 0 ; colonne < 4 ; colonne++)
		{
			for( int indice = colonne ; indice < 16 ; indice += 4 )
			{
				boolean fusion = false ;
				for ( int indiceHaut = indice-4 ; indiceHaut >= 0 ; indiceHaut-=4 )
				{
					if ( grille[indiceHaut] == 0 )
					{
						grille[indiceHaut] = grille[indiceHaut+4];
						grille[indiceHaut+4] = 0 ;
						
						if ( grille[indiceHaut] != 0 || grille[indiceHaut+4] != 0 )
							dernierDeplacement = true ;
					}
					else if ( grille[indiceHaut+4] == grille[indiceHaut])
					{
						if (!fusion)
						{
							grille[indiceHaut] = (short)(grille[indiceHaut+4]*2);
							grille[indiceHaut+4] = 0 ;
							//scoreGlobal += (int)(grille[indiceHaut]);
							fusion = true ;
							
							if ( grille[indiceHaut] != 0 || grille[indiceHaut+4] != 0 )
								dernierDeplacement = true ;
						}
					}
					else
						break;
				}
			}
		}

		return grille;
	}

	private static short[] deplacementDroite(short[] grille)
	{
		dernierDeplacement = false ;
		
		for( int ligne = 0 ; ligne <= 12 ; ligne += 4)
		{
			for ( int indice = ligne+3 ; indice >= ligne ; indice-- )
			{
				if ( indice%4 != 0 )
				{
					boolean fusion = false ;
					for ( int indiceDroite = indice ; indiceDroite < ligne+4 ; indiceDroite++ )
					{
						if ( grille[indiceDroite] == 0 )
						{
							grille[indiceDroite] = grille[indiceDroite-1];
							grille[indiceDroite-1] = 0 ;
							
							if ( grille[indiceDroite] != 0 || grille[indiceDroite-1] != 0)
								dernierDeplacement = true ;
						}
						else if ( grille[indiceDroite-1] == grille[indiceDroite])
						{
							if (!fusion)
							{
								grille[indiceDroite] = (short)(grille[indiceDroite-1]*2);
								grille[indiceDroite-1] = 0 ;
								//scoreGlobal += (int)(grille[indiceDroite]);
								fusion = true ;
								
								if ( grille[indiceDroite] != 0 || grille[indiceDroite-1] != 0)
									dernierDeplacement = true ;
							}
						}
						else
							break;
					}
				}
			}
		}

		return grille ;
	}

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

							if ( grille[indiceGauche] != 0 || grille[indiceGauche-1] != 0 )
								dernierDeplacement = true ;
						}
						else if ( grille[indiceGauche-1] == grille[indiceGauche])
						{
							if (!fusion)
							{
								grille[indiceGauche-1] = (short)(grille[indiceGauche]*2);
								grille[indiceGauche] = 0 ;
								//scoreGlobal += (int)(grille[indiceGauche-1]);
								fusion = true ;
								
								if ( grille[indiceGauche] != 0 || grille[indiceGauche-1] != 0 )
									dernierDeplacement = true ;
							}
						}
						else
							break;
					}
				}
			}
		}

		return grille ;
	}

	private static short[] copie(short[] grille) {
		return grille.clone();
	}
	
	public static int regleGrad(short[] grille) 
	{
		int score = 0 ;
		
		for( int ligne = 0 ; ligne <= 12 ; ligne += 4)
		{
			switch (ligne) {
			case 0:
				score += ( grille[ligne]*0 + grille[ligne+1]*1 + grille[ligne+2]*2 + grille[ligne+3]*3 );
				break;
			case 4:
				score += ( grille[ligne]*(-1) + grille[ligne+1]*0 + grille[ligne+2]*1 + grille[ligne+3]*2 );
				break;
			case 8:
				score += ( grille[ligne]*(-2) + grille[ligne+1]*(-1) + grille[ligne+2]*0 + grille[ligne+3]*1 );
				break;
			case 12:
				score += ( grille[ligne]*(-3) + grille[ligne+1]*(-2) + grille[ligne+2]*(-1) + grille[ligne+3]*0 );
				break;
			default:
				break;
			}
			score++ ;
		}
		
		
		return score ;
	}

	/**
	 * Regle valeur max sur les bords + bonus cases vides
	 * @param grille
	 * @return
	 */
	/*public static int regle(short[] grille) 
	{
		int score = 0 ;
		
		int max1 = 0, max2 = 0, max3 = 0, max4 = 0 ;
		for(short cellule : grille)
		{
			if( cellule > max1 )
				max1 = cellule ;
			else if ( cellule > max2 )
				max2 = cellule ;
			else if ( cellule > max3 )
				max3 = cellule ;
			else if ( cellule > max4 )
				max4 = cellule ;
		}
		
		for (int i = 0 ; i <= 15 ; i++)
		{
			if ( i != 5 && i != 6 && i != 9 && i != 10 )
				if ( grille[i] == max1 || grille[i] == max2 || grille[i] == max3 || grille[i] == max4 )
					score++ ;
		
			if ( grille[i] == 0 )
				score += 2 ;
		}
		
		
		
		return score ;
	}*/

	/**
	 * Regle du coin
	 * @param grille
	 * @return score
	 */
	/*public static int regle5(short[] grille) 
	{
		int max = 0 ;
		for(short cellule : grille)
			if( cellule > max )
				max = cellule ;
		
		if( grille[3] == max  )
			return 1 ;
		else
			return 0 ;
					
	}*/
	
	/**
	 * Panalité de mort
	 * @param grille
	 * @return score
	 *//*
	public static int regle4(Plateau grille) 
	{
		if (grille.positionLibres().size() == 0 )
			return -1000 ;
		else
			return 0 ;
	}*/

	
	
	/**
	 * Maximiser les espaces libres
	 * @param grille
	 * @return score
	 */
	public static int regle3(short[] grille) 
	{
		return getPositionLibres(grille).size()*2 ;
	}

	/**
	 * Ligne/colonne dans l'ordre croissant/décroissant + régularité des cellules
	 * @param grille
	 * @return score
	 */
	public static int regle2(short[] grille)
	{		
		int score = 0 ;
		
		// Ligne
		for( int ligne = 0 ; ligne <= 12 ; ligne += 4)
		{
			if ( ( (grille[ligne+1] == grille[ligne+0] || grille[ligne+1] == grille[ligne+0]*2) && (grille[ligne+2] == grille[ligne+1] || grille[ligne+2] == grille[ligne+1]*2) && ( grille[ligne+3] == grille[ligne+2] || grille[ligne+3] == grille[ligne+2]*2) ) // ligne croissante
					|| ( (grille[ligne+3] == grille[ligne+2] || grille[ligne+3]*2 == grille[ligne+2]) && (grille[ligne+2] == grille[ligne+1] || grille[ligne+2]*2 == grille[ligne+1]) && ( grille[ligne+1] == grille[ligne+0] || grille[ligne+1]*2 == grille[ligne+0]) ) ) // ligne decroissante
				score+= 3 ;
		}
		
		// Colonne
		for( int colonne = 0 ; colonne < 4 ; colonne ++)
		{
			if ( ( (grille[colonne] == grille[colonne+4] || grille[colonne] == grille[colonne+4]*2) && (grille[colonne+4] == grille[colonne+8] || grille[colonne+4] == grille[colonne+8]*2) && (grille[colonne+8] == grille[colonne+12] || grille[colonne+8] == grille[colonne+12]*2 ) ) // ligne croissante
					|| ( (grille[colonne] == grille[colonne+4] || grille[colonne]*2 == grille[colonne+4]) && (grille[colonne+4] == grille[colonne+8] || grille[colonne+4]*2 == grille[colonne+8]) && (grille[colonne+8] == grille[colonne+12] || grille[colonne+8]*2 == grille[colonne+12] )) ) // ligne decroissante
				score+=3 ;
		}
				
		
		return score;
	}

	/**
	 * Ligne/colonne dans l'ordre croissant/décroissant
	 * @param grille
	 * @return score
	 */
	public static int regle1(short[] grille) 
	{	
		// Avoir une regularité ENTRE les lignes ( si croissant, alors les autre lignes croissant, sinon perte de point. La ligne de réference utilisé est celle de la plus grande valeur)

		int score = 0 ;
		
		for( int ligne = 0 ; ligne <= 12 ; ligne += 4)
		{
			if ( (grille[ligne] >= grille[ligne+1] && grille[ligne+1] >= grille[ligne+2] && grille[ligne+2] >= grille[ligne+3]) // ligne croissante
					|| (grille[ligne] <= grille[ligne+1] && grille[ligne+1] <= grille[ligne+2] && grille[ligne+2] <= grille[ligne+3]) ) // ligne decroissante
				score++ ;
		}
		
		for( int colonne = 0 ; colonne < 4 ; colonne ++)
		{
			if ( (grille[colonne] >= grille[colonne+4] && grille[colonne+4] >= grille[colonne+8] && grille[colonne+8] >= grille[colonne+12]) // ligne croissante
					|| (grille[colonne] <= grille[colonne+4] && grille[colonne+4] <= grille[colonne+8] && grille[colonne+8] <= grille[colonne+12]) ) // ligne decroissante
				score++ ;
		}
		
		return score;
	}


	/*public static void main (String[] args)
	{
		Plateau pl = new Plateau() ;
		pl.plateauTest(2);
		short[] p = pl.getShortTableau();
		System.out.println(pl+"\nEnsuite\n");
		
		System.out.println("Regle 1 = "+Expectimax.regle1(p));
		System.out.println("Regle 2 = "+Expectimax.regle2(p));
		System.out.println("Regle 3 = "+Expectimax.regle3(p)+"\n\n");*/
		/*System.out.println("Regle 4 = "+Expectimax.regle4(p));
		System.out.println("Regle 5 = "+Expectimax.regle5(p));
		*//*
		pl.plateauTest(1);
		p = pl.getShortTableau();
		System.out.println(pl);
		System.out.println("Regle 1 = "+Expectimax.regle1(p));
		System.out.println("Regle 2 = "+Expectimax.regle2(p));
		System.out.println("Regle 3 = "+Expectimax.regle3(p));*/
		/*
		System.out.println("Regle 4 = "+Expectimax.regle4(p));
		System.out.println("Regle 5 = "+Expectimax.regle5(p));
		*/
	//}
}

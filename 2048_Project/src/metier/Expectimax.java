package metier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Expectimax 
{		
	public static boolean dernierDeplacement = false ;
	
	public static long tempsCopie = 0 ;
	public static long tempsGradient = 0 ;
	public static long tempsGetPositionLibre = 0 ;
	public static long tempsDeplacement = 0 ;

	
	public static float[] expectimaxDirection(short[] grille, int profondeur)
	{
		float scoreMax = -999999 ;
		int meilleurDir = 0 ;

		
		for ( int direction = 1 ; direction <= 4 ; direction++ )
		{
			short[] grilleCopie = copie(grille);
			
			// Direction : 1=gauche | 2=droite | 3=haut | 4=bas
			// Effectue un mouvement sans faire apparaitre les nouveaux nombres
			long startTime = System.currentTimeMillis();
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
			tempsDeplacement += System.currentTimeMillis()-startTime;

			if ( dernierDeplacement )
			{
				float score = expectimaxApparition(grilleCopie, profondeur) ;

				if ( score > scoreMax )
				{
					scoreMax = score;
					meilleurDir = direction ;
				}
			}
		}

		float[] result = {meilleurDir, scoreMax} ;
		return result;
	}
	
	private static float expectimaxApparition(short[] grille, int profondeur)
	{
		float score = 0 ;
		float moyBranche = 0 ;

		// Calcul des apparitions de 2
		for (int emplacement : getPositionLibres(grille))
		{
			short[] grilleCopie = copie(grille);
			grilleCopie = tourSuivantPrevu(grilleCopie, (short)2, emplacement) ;
			
			if ( profondeur <= 0 )	
				score += eval(grilleCopie) ;	// calcul du score de la grille
			else					
				moyBranche += expectimaxDirection(grilleCopie, profondeur-1)[1] ;	// calcul du score des autres grilles
			
		}
		
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
		
	}

	private static float eval(short[] grilleCopie) 
	{
		
		float lamda = (float)0.8;//((getPositionLibres(grilleCopie).size()/1.6)/10);
		return (float) (lamda*regleGradN(grilleCopie)+(1-lamda)*(regle3(grilleCopie)/16));
	}

	public static float regleGradN(short[] grille) 
	{
		long startTime = System.currentTimeMillis();

		float score = 0 ;
		
		HashMap<Short, Byte> correspondanceNorme = new HashMap<Short, Byte>();
		short[] grilleCopie = copie(grille);
		short[] tableauTriee = new short[16];
		
		Arrays.sort(grilleCopie) ;
		
		for (int i=0; i<= 16-1; i=i+1)
			tableauTriee[i] = grilleCopie[16-1-i];
		
	
		byte valeurNormalisee = 16;
		short valeurDeLaNorme = tableauTriee[0] ;
		correspondanceNorme.put(valeurDeLaNorme, valeurNormalisee);
		
		for( int i = 1 ; i < grille.length ; i++ )
		{
			if ( tableauTriee[i] < valeurDeLaNorme )
			{
				valeurNormalisee-- ;
				valeurDeLaNorme = tableauTriee[i];
			}
		
			correspondanceNorme.put(tableauTriee[i], valeurNormalisee);
			tableauTriee[i] = (short) valeurNormalisee ;
		}
		
	
		for( int ligne = 0 ; ligne <= 12 ; ligne += 4)
		{
			switch (ligne) {
			case 0:
				score += ( correspondanceNorme.get(grille[ligne])*3 + grille[ligne+1]*4 + grille[ligne+2]*5 + grille[ligne+3]*6 );
				break;
			case 4:
				score += ( correspondanceNorme.get(grille[ligne])*(2) + grille[ligne+1]*3 + grille[ligne+2]*4 + grille[ligne+3]*5 );
				break;
			case 8:
				score += ( correspondanceNorme.get(grille[ligne])*(1) + grille[ligne+1]*(2) + grille[ligne+2]*3 + grille[ligne+3]*4 );
				break;
			case 12:
				score += ( correspondanceNorme.get(grille[ligne])*(0) + grille[ligne+1]*(1) + grille[ligne+2]*(2) + grille[ligne+3]*3 );
				break;
			default:
				break;
			}
			score++ ;
		}
		
		tempsGradient += System.currentTimeMillis()-startTime;

		return (float) (score/(16.0*48.0));//normalisation(score, 10*max) ;
	
	}
	
	 /**
	 * Maximiser les espaces libres
	 * @param grille
	 * @return score
	 */
	public static float regle3(short[] grille) 
	{
		float score = getPositionLibres(grille).size() ;
		
		//System.out.println(score);
		
		return score;
	}

	private static short[] tourSuivantPrevu(short[] grilleCopie, short nombre, int position)
	{
		 grilleCopie[position] = nombre ;
		 
		 return grilleCopie ;
	}

	
	private static ArrayList<Byte> getPositionLibres(short[] grille) 
	{
		long startTime = System.currentTimeMillis();

		ArrayList<Byte> caseLibre = new ArrayList<Byte>();
		
		for ( byte i = 0 ; i < grille.length ; i++ )
		{
			if ( grille[i] == 0 )
			{
				caseLibre.add(i) ;
			}
		}
		
		tempsGetPositionLibre += System.currentTimeMillis()-startTime;

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

	private static short[] copie(short[] grille) 
	{
		long startTime = System.currentTimeMillis();
		short[] grilleCopie = grille.clone();
		tempsCopie += System.currentTimeMillis()-startTime;
		return grilleCopie;
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
	 * Ligne/colonne dans l'ordre croissant/décroissant + régularité des cellules
	 * @param grille
	 * @return score
	 */
	public static float regle2(short[] grille)
	{		
		int score = 0 ;
		
		// Ligne
		for( int ligne = 0 ; ligne <= 12 ; ligne += 4)
		{
			if ( ( (grille[ligne+1] == grille[ligne+0] || grille[ligne+1] == grille[ligne+0]*2) && (grille[ligne+2] == grille[ligne+1] || grille[ligne+2] == grille[ligne+1]*2) && ( grille[ligne+3] == grille[ligne+2] || grille[ligne+3] == grille[ligne+2]*2) ) // ligne croissante
					|| ( (grille[ligne+3] == grille[ligne+2] || grille[ligne+3]*2 == grille[ligne+2]) && (grille[ligne+2] == grille[ligne+1] || grille[ligne+2]*2 == grille[ligne+1]) && ( grille[ligne+1] == grille[ligne+0] || grille[ligne+1]*2 == grille[ligne+0]) ) ) // ligne decroissante
				score+=1 ;
		}
		
		// Colonne
		for( int colonne = 0 ; colonne < 4 ; colonne ++)
		{
			if ( ( (grille[colonne] == grille[colonne+4] || grille[colonne] == grille[colonne+4]*2) && (grille[colonne+4] == grille[colonne+8] || grille[colonne+4] == grille[colonne+8]*2) && (grille[colonne+8] == grille[colonne+12] || grille[colonne+8] == grille[colonne+12]*2 ) ) // ligne croissante
					|| ( (grille[colonne] == grille[colonne+4] || grille[colonne]*2 == grille[colonne+4]) && (grille[colonne+4] == grille[colonne+8] || grille[colonne+4]*2 == grille[colonne+8]) && (grille[colonne+8] == grille[colonne+12] || grille[colonne+8]*2 == grille[colonne+12] )) ) // ligne decroissante
				score+=1 ;
		}
				
		
		return score/8;
	}

	/**
	 * Ligne/colonne dans l'ordre croissant/décroissant
	 * @param grille
	 * @return score
	 */
	public static float regle1(short[] grille) 
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
		
		return score/8;
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

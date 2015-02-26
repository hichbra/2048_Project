package metier;

public class Expectimax 
{
	private Plateau plateau ;
	
	public Expectimax(Plateau plateau)
	{
		this.plateau = plateau ;
	}
	

	public double[] expectimax(Plateau grille, int profondeur)
	{
		//System.out.println("Expectimax profondeur: "+profondeur);
		double scoreMax = -999999 ;
		int meilleurDir = 0 ;
		
		for ( int direction = 1 ; direction <= 4 ; direction++ )
		{
			Plateau grilleCopie =  new Plateau(this.plateau);
			
			// Direction : 1=gauche | 2=droite | 3=haut | 4=bas
			// Effectue un mouvement sans faire apparaitre les nouveaux nombres
			boolean directionPossible = true ;
			switch(direction)
			{
				case 1:
					directionPossible = grilleCopie.gauche();
					break;
				case 2:
					directionPossible = grilleCopie.droite();	
					break;
				case 3:
					directionPossible = grilleCopie.haut();
					break;
				case 4:
					directionPossible = grilleCopie.bas();
					break;
				default:
					break;
			}
						
			if ( directionPossible )
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
	
	

	private double eval(Plateau grille, int profondeur)
	{
		//System.out.println("Eval profondeur: "+profondeur);

		double score = 0 ;
		
		// Calcul des apparitions de 2
		for (int emplacement : grille.positionLibres())
		{
			Plateau grilleCopie = new Plateau(grille);
			grilleCopie.tourSuivantPrevu(2, emplacement) ;
			
			// calcul du score de la grille en ponderant le score avec la probabilit� d'avoir un 2
			score += ( regle1(grilleCopie)+regle2(grilleCopie)+regle3(grilleCopie)+regle4(grilleCopie) ) * (9.0/10.0) ;
			
			// calcul du score des autres grilles
			if ( profondeur > 0 )
				score += expectimax(grilleCopie, profondeur-1)[1] ;
			
		}
		
		// Calcul des apparitions de 4
		for (int emplacement : grille.positionLibres())
		{
			Plateau grilleCopie = new Plateau(grille);
			grilleCopie.tourSuivantPrevu(4, emplacement) ;
			
			// calcul du score de la grille en ponderant le score avec la probabilit� d'avoir un 4
			score += ( regle1(grilleCopie)+regle2(grilleCopie)+regle3(grilleCopie)+regle4(grilleCopie) ) * (1.0/10.0) ;
			
			// calcul du score des autres grilles
			if ( profondeur > 0 )
				score += expectimax(grilleCopie, profondeur-1)[1] ;
			
		}
		
		score /= (grille.positionLibres().size()*2) ; // score = score / nb Appartion Possible ( en comptant les 2 et 4 )
		//System.out.println(score);
		
		return score;
	}

	

	public static int regle4(Plateau grille) 
	{
		if (grille.positionLibres().size() == 0 )
			return -1000 ;
		else
			return 0 ;
	}


	public static int regle3(Plateau grille) 
	{
		return grille.positionLibres().size();
	}


	public static int regle2(Plateau grille)
	{
		int[][] plateau = grille.getPlateau() ;
		
		int score = 0 ;
		
		for( int[] ligne : plateau )
			if ( ( (ligne[1] == ligne[0] || ligne[1] == ligne[0]*2) && (ligne[2] == ligne[1] || ligne[2] == ligne[1]*2) && ( ligne[3] == ligne[2] || ligne[3] == ligne[2]*2) ) // ligne croissante
					|| ( (ligne[3] == ligne[2] || ligne[3]*2 == ligne[2]) && (ligne[2] == ligne[1] || ligne[2]*2 == ligne[1]) && ( ligne[1] == ligne[0] || ligne[1]*2 == ligne[0]) ) ) // ligne decroissante
				score++ ;
				
		
		
		for ( int colonne = 0 ; colonne < 4 ; colonne++ )
			if (  ( (plateau[1][colonne] == plateau[0][colonne] || plateau[1][colonne] == plateau[0][colonne]*2) && (plateau[2][colonne] == plateau[1][colonne] || plateau[2][colonne] == plateau[1][colonne]*2) && ( plateau[3][colonne] == plateau[2][colonne] || plateau[3][colonne] == plateau[2][colonne]*2) ) // colonne croissante
				||  ( (plateau[3][colonne] == plateau[2][colonne] || plateau[3][colonne]*2 == plateau[2][colonne]) && (plateau[2][colonne] == plateau[1][colonne] || plateau[2][colonne]*2 == plateau[1][colonne]) && ( plateau[1][colonne] == plateau[0][colonne] || plateau[1][colonne]*2 == plateau[0][colonne]) ) )  // colonne decroissante
				score++ ;
				
		
		return score;
	}


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
		p.plateauTest();
		
		System.out.println(p);
		System.out.println("Regle 1 = "+Expectimax.regle1(p));
		System.out.println("Regle 2 = "+Expectimax.regle2(p));
		System.out.println("Regle 3 = "+Expectimax.regle3(p));

	}
}

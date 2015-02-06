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
	
	private void initPlateau() 
	{
		for( ArrayList<Integer> ligne : plateau ) // pour toutes les lignes
		{
			// On les cases à 0
			for( int i = 0 ; i < 4 ; i++ )
				ligne.add(0);
		}
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
	}

}

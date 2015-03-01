package metier;

import java.awt.Point;

public class Joueur 
{
	public static final int SAUT_MAX = 150 ;
	
	private final int LARGEUR_JOUEUR = 50 ; 
	private final int HAUTEUR_JOUEUR = 100 ;
	
	private Point position ;
	private boolean orientation ; // false = gauche | true = droite
	
	private boolean saut ;
	private int tempsSaut ;
	
	private boolean tombe ;
	
	public Joueur(Point position)
	{
		this.position = position ;
		this.orientation = true ;

		this.saut = false ;
		this.tempsSaut = 0 ;
		
		this.tombe = false ;
	}
	
	public int getLargeur() {
		return LARGEUR_JOUEUR;
	}

	public int getHauteur() {
		return HAUTEUR_JOUEUR;
	}

	public Point getPosition() {
		return position;
	}
	
	public boolean isSaut() {
		return saut;
	}
	
	public void setSaut(boolean saut) {
		this.saut = saut;
	}

	public boolean isTombe() {
		return tombe;
	}

	public void setTombe(boolean tombe) {
		this.tombe = tombe;
	}
	
	public int getTempsSaut() {
		return tempsSaut;
	}

	public void setTempsSaut(int tempsSaut) {
		this.tempsSaut = tempsSaut;
	}

	

	public void avance()
	{
		if ( orientation )
			position.setLocation(position.getX()+1, position.getY());
	}
	
	public void recule()
	{
		if ( orientation )
			position.setLocation(position.getX()-1, position.getY());
	}
	
	public void saute()
	{
		if (! this.saut)
		{
			position.setLocation(position.getX(), position.getY()-1);
			this.saut = true ;		
		}
	}
	
	public void vole()
	{
		if ( this.tempsSaut < Joueur.SAUT_MAX )
			position.setLocation(position.getX(), position.getY()-1);
	}
	
	public void tombe()
	{
		position.setLocation(position.getX(), position.getY()+1);
	}



}

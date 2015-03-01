package metier.objet;

import java.awt.Point;

public class Plateforme extends Objet 
{
	private Point p1 ;
	private Point p2 ;
	
	
	public Plateforme(Point p1, Point p2)
	{
		this.p1 = p1 ;
		this.p2 = p2 ;
		
		initialisationObjet();
	}

	@Override
	protected void initialisationObjet() 
	{
		addLigne(new Ligne(this.p1, this.p2));
	}

	
}

package metier;

import java.awt.Point;
import java.util.ArrayList;

import metier.objet.Ligne;
import metier.objet.Objet;
import metier.objet.Plateforme;


public class Terrain
{
	private final int LARGEUR_TERRAIN = 1400 ; 
	private final int HAUTEUR_TERRAIN = 800 ;	
	
	private ArrayList<Objet> objets;
	private Joueur joueur ;
	
	public Terrain()
	{
		this.objets = new ArrayList<Objet>();
		this.objets.add(new Plateforme(new Point(0, HAUTEUR_TERRAIN-200), new Point(LARGEUR_TERRAIN, HAUTEUR_TERRAIN-200))) ;
		
		this.joueur = new Joueur(new Point(60, HAUTEUR_TERRAIN-800)) ;
	}

	public int getLargeur() {
		return LARGEUR_TERRAIN;
	}

	public int getHauteur() {
		return HAUTEUR_TERRAIN;
	}

	public Joueur getJoueur() {
		return joueur;
	}

	public ArrayList<Objet> getObjets() {
		return objets;
	}
	
	public void gravite()
	{
		Point hitboxSol = new Point((int)this.joueur.getPosition().getX()+(this.joueur.getLargeur()/2), (int)this.joueur.getPosition().getY()+this.joueur.getHauteur()) ;

		if( ! this.joueur.isSaut() )
		{
			boolean gravite = true ;
			for( Objet o : this.objets )
			{
				for( Ligne ligne : o.getLignes())
				{
					if ( pointDansLigne(hitboxSol, ligne) )
					{
						gravite = false ;
						break ;
					}
				}
				if (!gravite)
					break;
			}
			
			if ( gravite )
			{
				this.joueur.setTombe(true);
				this.joueur.tombe();
			}
			else
				this.joueur.setTombe(false);
		}
		else if (! this.joueur.isTombe())
		{
			boolean mur = false ;
			for( Objet o : this.objets )
			{
				for( Ligne ligne : o.getLignes())
				{
					if ( pointDansLigne(hitboxSol, ligne) )
					{
						mur = true ;
						break ;
					}
				}
				if (mur)
					break;
			}
			
			if ( ! mur && this.joueur.getTempsSaut() < Joueur.SAUT_MAX )
			{
				this.joueur.setSaut(true);
				this.joueur.vole();
				this.joueur.setTempsSaut(this.joueur.getTempsSaut()+1);

			}
			else
			{
				this.joueur.setSaut(false);
				this.joueur.setTempsSaut(0);
				this.joueur.setTombe(true);
			}
		}
	}
	
	

	/**
	 * A,B,C alignés => le determinant AB,AC est nul
	 * et
	 * C entre A et B => le produit scalaire CA,CB est négatif (ou nul)
	 * 
	 * A(x1,y1), B(x2,y2), C(x3,y3)
	 * @param c
	 * @param ligne
	 * @return result
	 */
	private boolean pointDansLigne(Point c, Ligne ligne)
	{
		boolean result = true ;
		
		Point ac = new Point((int)(c.getX()-ligne.getP1().getX()), (int)(c.getY()-ligne.getP1().getY()));	// AC=(x3-x1,y3-y1)
		Point ab = new Point((int)(ligne.getP2().getX()-ligne.getP1().getX()), (int)(ligne.getP2().getY()-ligne.getP1().getY()));	// AB=(x2-x1,y2-y1)
		
		// AC.x*AB.y - AB.x*AC.y = 0
		System.out.println(((ac.getX()*ab.getY()) + (ab.getX()*ac.getY())));
		if ( ((ac.getX()*ab.getY()) + (ab.getX()*ac.getY())) != 0 )
			result = false ;

		if (result)
		{		
			Point ca = new Point((int)(ligne.getP1().getX()-c.getX()), (int)(ligne.getP1().getY()-c.getY()));	// CA=(x1-x3,y1-y3)
			Point cb = new Point((int)(ligne.getP2().getX()-c.getX()), (int)(ligne.getP2().getY()-c.getY()));	// CB=(x2-x3,y2-y3)
			
			// CA.x*CB.x + CA.y*CB.y <= 0 
			if ( !( ((ca.getX()*cb.getX()) + (ca.getY()*cb.getY())) <= 0 ) )
				result = false ;
			
		}
		
		return result ;
	}
}

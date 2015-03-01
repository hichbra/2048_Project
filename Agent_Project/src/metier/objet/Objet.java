package metier.objet;

import java.util.ArrayList;

public abstract class Objet 
{
	private ArrayList<Ligne> lignes ; 
	
	public Objet()
	{
		this.lignes = new ArrayList<Ligne>();
	}
	/*
	public int getLargeur()
	{
		return this.hauteur ;
	}
	
	public int getHauteur()
	{
		return this.largeur ;
	}*/
	
	protected abstract void initialisationObjet();
	
	protected void addLigne(Ligne l)
	{
		this.lignes.add(l);
	}
	
	public ArrayList<Ligne> getLignes()
	{
		return lignes;
	}

}

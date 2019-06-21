package ttb.metier;

/**
 * Classe Robot
 * @author Jérémy Auzou
 * @author Matys Achart
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */

public class Robot
{
	private static int nbRobots = 0;

	private int[]  pos;
	/** Direction du robot, commence à 0 et va jusqu'à 5, sens horaire */
	private int    dir;
	private int    id;
	private Joueur monJoueur;
	private Tuile  cristal;

	/**
	 * Construit un nouveau robot et initialise sa position et sa direction.
	 * @param ligne la ligne où le robot est positionné;
	 * @param colonne la colonne où le robot est positionné.
	 * @param dir la direction du robot.
	 */
	public Robot(int ligne, int colonne, int dir)
	{
		this.pos = new int[] {ligne, colonne};
		this.dir = dir;
		this.id  = Robot.nbRobots++ % 2;
	}

	public int      getDir()    {return dir; }
	public int[]    getPos()    {return pos; }
	public int      getId()     {return id ; }
	public Joueur   getJoueur() {return this.monJoueur; }

	/**
	 * Retourne vrai si le robot possède un cristal sur lui.
	 * @return true si le robot possède un cristal sur lui, false sinon.
	 */
	public boolean hasCristal()
	{
		return cristal != null;
	}

	/**
	 * Ajoute un cristal sur le robot.
	 * @param t le cristal à ajouter.
	 * @return true si il a pu être ajouté, false sinon.
	 */
	public boolean chargerCristal(Tuile t)
	{
		if(cristal != null)
		{
			return false;
		}

		cristal = t;
		return true;
	}

	/**
	 * Dépose le cristal chargé.
	 * @return le cristal déposé.
	 */
	public Tuile deposerCristal()
	{
		Tuile c = cristal;
		cristal = null;
		return c;
	}

	/** Retourne le cristal dans l'inventaire du robot. */
	public Tuile getCristal() { return cristal; }

	/** Fait appartenir ce robot à un joueur. */
	public void setJoueur(Joueur j) {monJoueur = j;}

	/** Met à jour la position du robot sur le plateau. */
	public void setPos(int[] pos) {this.pos = pos;}

	/**
	 * Fait tourner le robot de 60°.
	 * @param left si true, tourne à gauche, sinon à droite.
	 */
	public void turnAround(boolean left)
	{
		if(left)
			dir = Math.floorMod((dir - 1), 6);
		else
			dir = (dir+1) % 6;
	}

	/** Met directement à jour la position du robot. */
	public void setDir(int dir) {this.dir = dir;}

	/** Retourne la couleur du robot. */
	public String getCouleur() { return this.monJoueur.getCouleur(); }
}
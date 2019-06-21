package ttb.metier;

import java.util.Arrays;
import java.util.ArrayList;

/**
 * Classe Joueur
 * Un Joueur possède possède des robots à qui il donne des ordres.
 * Il possède également une base qui contient des cristaux.
 * @author Jérémy Auzou
 * @author Matys Achart
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */

public class Joueur
{
	public  final static String[] COULEURS = {"Rouge", "Jaune", "Vert", "Bleu", "Violet", "Rose"};
	private final static char[] BASE_RESERVE =
		{'A', 'A', 'S', 'G', 'G', 'G', 'D', 'D', 'D', 'C', 'C', 'E', 'E'};

	static int nbJoueurs = 0;

	private int      id;
	private Robot[]  tabRobot;
	private int[]    posBase;
	private char[][] ordres;

	private ArrayList<Character> reserve;
	private ArrayList<Tuile>     cristaux;

	/**
	 * Constructeur par défaut.
	 * Initialise le numéro du joueur, sa main et les ordres de ses robots.
	 */
	public Joueur()
	{
		id = Joueur.nbJoueurs++;

		this.tabRobot = new Robot[] {null, null};
		ordres        = new char[2][3];

		Arrays.fill(ordres[0], '\0');
		Arrays.fill(ordres[1], '\0');

		reserve  = new ArrayList<Character>();
		cristaux = new ArrayList<Tuile>();

		initReserve();
	}

	/**
	 * La réserve contient :
	 * 2 Tuiles Avancer 1x 'A'
	 * 1 Tuile  Avancer 2x 'S'
	 * 3 Tuiles Tourner à Gauche 'G'
	 * 3 Tuiles Tourner à Droite 'D'
	 * 2 Tuiles Charger un crystal 'C'
	 * 2 Tuiles Déposer un crystal 'E'
	 */
	private void initReserve()
	{
		for (char ordre : Joueur.BASE_RESERVE)
			reserve.add(ordre);
	}

	/**
	 * Ajoute un ordre à un robot.
	 * @param idRobot le robot à modifier.
	 * @param ind l'indice de l'ordre à changer.
	 * @param c l'ordre à ajouter.
	 */
	public void ajouterOrdre(int idRobot, int ind, char c)
	{
		int i = 0;
		i = reserve.indexOf(c);
		if( i != -1)
		{
			char ordre = ordres[idRobot][ind];
			ordres[idRobot][ind] = reserve.remove(i);
			if(ordre != '\0')
				reserve.add(ordre);
		}
	}

	/**
	 * Enlève l'ordre d'un robot.
	 * @param idRobot le robot à modifier.
	 * @param ind l'indice de l'ordre à enlever.
	 */
	public void enleverOrdre(int idRobot, int ind)
	{
		if(ordres[idRobot][ind] != '\0')
		{
			reserve.add(ordres[idRobot][ind]);
			ordres[idRobot][ind] = '\0';
		}
	}

	/**
	 * Permute deux ordres pour le programme d'un même robot.
	 * @param idRobot le robot à modifier.
	 * @param ind1 l'indice du premier ordre à permuter.
	 * @param ind2 l'indice du deuxième ordre à permuter.
	 */
	public void permuterOrdre(int idRobot, int ind1, int ind2)
	{
		char c;
		c = ordres[idRobot][ind1];
		ordres[idRobot][ind1] = ordres[idRobot][ind2];
		ordres[idRobot][ind2] = c;
	}

	/**
	 * Supprime tous les ordres d'un robot.
	 * @param id l'id du robot pour lequel supprimer les ordres.
	 */
	public void resetOrdres(int id)
	{
		for(int i = 0; i < ordres[id].length; i++)
		{
			if(ordres[id][i] != '\0')
			{
				reserve.add(ordres[id][i]);
				ordres[id][i] = '\0';
			}
		}
	}

	/** Retourne tous les robots du joueur. */
	public Robot[] getRobots()    {return this.tabRobot;}

	/** Retourne le robot du joueur ayant l'id spécifié. */
	public Robot getRobot(int id) {return tabRobot[id];}

	/**
	 * Ajoute un robot à ce joueur.
	 * Un joueur peut posséder deux robots ;
	 * s'il en possède déjà un, il est ajouté après.
	 * @param r le Robot à ajouter.
	 */
	public void ajouterRobot(Robot r)
	{
		if(tabRobot[0] == null)
		{
			this.tabRobot[0] = r;
			r.setJoueur(this);
			return;
		}

		if(tabRobot[1] == null)
		{
			r.setJoueur(this);
			this.tabRobot[1] = r;
		}
	}

	/** Retourne l'id de ce joueur. */
	public int getId() {return id;}

	/**
	 * Calcule le nombre de points de ce joueur, en fonction des cristaux
	 * qu'il possède dans sa base.
	 * @return le nombre de points du joueur.
	 */
	public int getPoints()
	{
		int points = 0;
		for (Tuile cristal : cristaux)
			points += Integer.parseInt(cristal.toString());

		return points;
	}

	/**
	 * Permet d'obtenir un robot en fonction d'une position donnée.
	 * @param pos la position du robot
	 * @return le Robot avec la position recherchée s'il existe ou null.
	 */
	public Robot getRobotParPos(int[] pos)
	{
		for(Robot r : tabRobot)
			if(pos[0] == r.getPos()[0] && pos[1] == r.getPos()[1])
				return r;

		return null;
	}

	/** Ajoute un cristal à la base du joueur. */
	public void addCristal(Tuile t) {cristaux.add(t);}

	/** Retourne les cristaux possédés par le joueur. */
	public ArrayList<Tuile> getCristaux() { return this.cristaux; }

	/** Retourne la position de la base du joueur. */
	public int[] getBase() {return this.posBase;}

	/**
	 * Positionne la base du joueur.
	 * @param pos la position de la base.
	 */
	public void setBase(int[] pos)
	{
		if(posBase == null)
			this.posBase = pos;
	}

	/** Retourne les ordres du robot ayant l'id spécifié. */
	public char[] getOrdres(int id) {return Arrays.copyOf(ordres[id], ordres[id].length);}

	/** Retourne la réserve du joueur. */
	public ArrayList<Character> getReserve() {return this.reserve;}

	/** Retourne la couleur du joueur. */
	public String getCouleur(){return Joueur.COULEURS[this.id];}

	public String toString()
	{
		String retour = "Joueur " + this.getCouleur() + " ( Score :" + this.getPoints() + ") : \n";
		retour += "\tOrdres : ";
		for(int i = 0; i < 2; i++)
		{
			for(char c : this.getOrdres(i))
			{
				retour += "[";
				if(c == '\0')
				retour += " ";
				else
				retour += c;

				retour += "] ";
			}
		}

		retour += "\n\tReserve : ";
		for(Character c : this.getReserve())
			retour += c + ",";

		return retour.substring(0, retour.length() - 1) + "\n";
	}

}

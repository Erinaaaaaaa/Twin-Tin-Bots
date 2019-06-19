package ttb.metier;

import java.util.Arrays;
import java.util.ArrayList;

public class Joueur
{
	/** Tableau contenant les différentes couleurs pour chaque joueur. */
	public  final static String[] COULEURS = {"Rouge", "Jaune", "Vert", "Bleu", "Violet", "Rose"};

	/**
	 * La réserve contient :
	 * <ul>
	 * <li>2 Tuiles Avancer 1x 'A'</li>
	 * <li>1 Tuile  Avancer 2x 'S'</li>
	 * <li>3 Tuiles Tourner à Gauche 'G'</li>
	 * <li>3 Tuiles Tourner à Droite 'D'</li>
	 * <li>2 Tuiles Charger un cristal 'C'</li>
	 * <li>2 Tuiles Déposer un cristal 'E'</li>
	 * </ul>
	 */
	private final static char[] MAIN_BASE ={'A','A','S','G','G','G','D','D','D','C','C','E','E'};

	private static int nbJoueurs = 0;

	private int     id;
	private Robot[] tabRobot;
	private int[]   posBase;
	private int     points;
	private char[]  ordres;

	private ArrayList<Character> main;

	/**
	 * Constructeur par défaut.
	 * Initialise le numéro du joueur, sa main et les ordres de ses robots.
	 */
	public Joueur()
	{
		id = Joueur.nbJoueurs++;
		this.tabRobot = new Robot[] {null, null};
		this.points = 0;
		ordres = new char[6];
		Arrays.fill(ordres, '\0');
		main = new ArrayList<Character>();
		for(char c : MAIN_BASE)
			main.add(c);
	}

	/**
	 * Ajoute un ordre à un robot.
	 * @param ind l'indice de l'ordre à changer.
	 * @param c l'ordre à ajouter.
	 */
	public void ajouterOrdre(int ind, char c)
	{
		int i = 0;
		i = main.indexOf(c);
		if( i != -1)
		{
			char ordre = ordres[ind];
			ordres[ind] = main.remove(i);
			if(ordre != '\0')
				main.add(c);
		}
	}

	/**
	 * Enlève l'ordre d'un robot.
	 * @param ind l'indice de l'ordre à enlever.
	 */
	public void enleverOrdre(int ind)
	{
		if(ordres[ind] != '\0')
		{
			main.add(ordres[ind]);
			ordres[ind] = '\0';
		}
	}

	/**
	 * Permute deux ordres pour le programme d'un même robot.
	 * @param ind1 l'indice du premier ordre à permuter.
	 * @param ind2 l'indice du deuxième ordre à permuter.
	 */
	public void permuterOrdre(int ind1, int ind2)
	{
		if(ordres[ind1] == '\0' || ordres[ind2] == '\0')
			return;

		char c;
		c = ordres[ind1];
		ordres[ind1] = ordres[ind2];
		ordres[ind2] = c;
	}

	/**
	 * Supprime tous les ordres d'un robot.
	 * @param id l'id du robot pour lequel supprimer les ordres.
	 */
	public void resetOrdres(int id)
	{
		for(int i = 0; i < ordres.length / 2; i++)
		{
			if(ordres[i+(3*id)] != '\0')
			{
				main.add(ordres[i]);
				ordres[i+(3*id)] = '\0';
			}
		}
	}

	public Robot[] getRobots() {return this.tabRobot;}

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

	public int getId() {return id;}

	public Robot getRobotParPos(int[] pos)
	{
		for(Robot r : tabRobot)
			if(pos[0] == r.getPos()[0] && pos[1] == r.getPos()[1])
				return r;

		return null;
	}

	public void addPoint(Tuile t) {points += Integer.parseInt(t.toString());}

	public int[] getBase() {return this.posBase;}

	public void setBase(int[] pos)
	{
		if(posBase == null)
			this.posBase = pos;
	}

	public char[]               getOrdres() {return ordres;}
	public ArrayList<Character> getMain()   {return this.main;}
	public String               getCouleur(){return Joueur.COULEURS[this.id];}

}

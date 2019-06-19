package ttb.metier;

import java.awt.Color;
import java.util.Arrays;
import java.util.ArrayList;

public class Joueur
{
	public  final static String[] COULEURS = {"Rouge", "Jaune", "Vert", "Bleu", "Violet", "Rose"};
	private final static char[]   MAIN_BASE ={'A','A','S','G','G','G','D','D','D','C','C','E','E'};

	private static int nbJoueurs = 0;

	private int     id;
	private Robot[] tabRobot;
	private int[] posBase;
	private int points;
	private char[][] ordres;
	private ArrayList<Character> reserve;

	public Joueur()
	{
		id = Joueur.nbJoueurs++;
		this.tabRobot = new Robot[] {null, null};
		this.points = 0;
		ordres = new char[2][3];
		Arrays.fill(ordres[0], '\0');
		Arrays.fill(ordres[1], '\0');
		reserve = new ArrayList<Character>();
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
		reserve.add('A');
		reserve.add('A');
		reserve.add('S');
		reserve.add('G');
		reserve.add('G');
		reserve.add('G');
		reserve.add('D');
		reserve.add('D');
		reserve.add('D');
		reserve.add('C');
		reserve.add('C');
		reserve.add('E');
		reserve.add('E');
	}

	public void ajouterOrdre(int idRobot, int ind, char c)
	{
		int i = 0;
		i = reserve.indexOf(c);
		if( i != -1)
		{
			char ordre = ordres[idRobot][ind];
			ordres[idRobot][ind] = reserve.remove(i);
			if(ordre != '\0')
				reserve.add(c);
		}
	}

	public void enleverOrdre(int idRobot, int ind)
	{
		if(ordres[idRobot][ind] != '\0')
		{
			reserve.add(ordres[idRobot][ind]);
			ordres[idRobot][ind] = '\0';
		}
	}

	public void permuterOrdre(int idRobot, int ind1, int ind2)
	{
		if(ordres[idRobot][ind1] == '\0' || ordres[idRobot][ind2] == '\0')
			return;

		char c;
		c = ordres[idRobot][ind1];
		ordres[idRobot][ind1] = ordres[idRobot][ind2];
		ordres[idRobot][ind2] = c;
	}

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

	public Robot[] getRobots() {return this.tabRobot;}

	public Robot getRobot(int id) {return tabRobot[id];}

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

	public int getPoints() {return this.points;}

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

	public char[] getOrdres(int id) {return Arrays.copyOf(ordres[id], ordres[id].length);}
	public ArrayList<Character> getReserve() {return this.reserve;}
	public String getCouleur(){return Joueur.COULEURS[this.id];}

}

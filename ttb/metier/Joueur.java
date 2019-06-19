package ttb.metier;

import java.awt.Color;
import java.util.Arrays;
import java.util.ArrayList;

public class Joueur
{
	public  final static String[] COULEURS = {"Rouge", "Jaune", "Vert", "Bleu", "Violet", "Rose"};
	private static int nbJoueurs = 0;
	private int id;
	private Robot[] tabRobot;
	private int[] posBase;
	private int points;
	private char[] ordres;
	private ArrayList<Character> main;

	public Joueur()
	{
		id = Joueur.nbJoueurs++;
		this.tabRobot = new Robot[] {null, null};
		this.points = 0;
		ordres = new char[6];
		Arrays.fill(ordres, '\0');
		main = new ArrayList<Character>();
		initMain();
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
	private void initMain()
	{
		main.add('A');
		main.add('A');
		main.add('S');
		main.add('G');
		main.add('G');
		main.add('G');
		main.add('D');
		main.add('D');
		main.add('D');
		main.add('C');
		main.add('C');
		main.add('E');
		main.add('E');
	}

	public void ajouterOrdre(int ind, char c)
	{
		int i = 0;
		i = main.indexOf(c);
		if( i != -1)
		{
			char ordre = ordres[ind];
			ordres[i] = main.remove(i);
			if(ordre != '\0')
				ordres[i] = c;
		}
	}

	public void enleverOrdre(int ind)
	{
		if(ordres[ind] != '\0')
		{
			main.add(ordres[ind]);
			ordres[ind] = '\0';
		}
	}

	public void permuterOrdre(int ind1, int ind2)
	{
		if(ordres[ind1] == '\0' || ordres[ind2] == '\0')
			return;

		char c;
		c = ordres[ind1];
		ordres[ind1] = ordres[ind2];
		ordres[ind2] = c;
	}

	public void resetOrdres()
	{
		for(int i = 0; i < ordres.length; i++)
		{
			if(ordres[i] != '\0')
			{
				main.add(ordres[i]);
				ordres[i] = '\0';
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

	public char[] getOrdres() {return ordres;}
	public ArrayList<Character> getMain() {return this.main;}
	public String getCouleur(){return Joueur.COULEURS[this.id];}

}

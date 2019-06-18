package ttb;

import java.util.Arrays;

/**
 * Classe Plateau
 * @version 2019-06-18
 */

public class Plateau
{
	private final int NB_LIGNES;
	private final int NB_COLONNES;
	private Tuile[][] plateau;
	private Joueur[]  tabJoueurs;
	private Robot[] tabRobot;
	private int joueurActuel;
	private Robot r;
	private Robot autreR;
	private Robot uit;

	public Plateau(int nbJoueurs)
	{
		NB_LIGNES   = 9;
		NB_COLONNES = NB_LIGNES;

		plateau = new Tuile[NB_LIGNES][NB_COLONNES];
		for (Tuile[] ligne : plateau) // On remplit le plateau de tuiles inaccessibles
			Arrays.fill(ligne, Tuile.OUT_OF_BOUNDS);

		int nbCasesVoulu = plateau.length/2;
		int aRemplir;
		tabRobot = new Robot[3];
		tabRobot[0] = new Robot(NB_LIGNES/2, NB_COLONNES/2, 2);
		tabRobot[1] = new Robot(NB_LIGNES/2, NB_COLONNES/2+1, 5);
		tabRobot[2] = new Robot(NB_LIGNES/2, NB_COLONNES/2+2, 5);
		for (int i=0; i<plateau.length; i++)
		{
			aRemplir = plateau.length-nbCasesVoulu;
			if (i < plateau.length/2+1)
				nbCasesVoulu++;
			else {
				aRemplir+=2;
				nbCasesVoulu--;
			}
			for (int j=aRemplir/2; j<aRemplir/2+nbCasesVoulu; j++)
			{
				boolean bOk = false;
				for(Robot r : tabRobot)
				{
					if(i == r.getPos()[0] && j == r.getPos()[1])
					{
						plateau[i][j] = Tuile.ROBOT;
						bOk = true;
						break;
					}
				}
				if(!bOk)
					plateau[i][j] = Tuile.VIDE; // Tuile vide
			}
		}

		tabJoueurs = new Joueur[]{new Joueur()};
		tabJoueurs[0].ajouterRobot(tabRobot[0]);
		tabJoueurs[0].ajouterRobot(tabRobot[1]);
	}

	public Joueur getJoueurCourant() {return tabJoueurs[joueurActuel];}

	public void changerJoueur() {joueurActuel = joueurActuel++ % tabJoueurs.length;}

	public Robot getRobotAPosition(int[] pos)
	{
		if(plateau[pos[0]][pos[1]] != Tuile.ROBOT)
			return null;

		Robot r = null;

		for(Joueur j : tabJoueurs)
		{
			r = j.getRobotParPos(pos);
			if(r != null)
				break;
		}

		return r;
	}

	private int[] nextPos(int[] initPos, int dir)
	{
		int[] pos = Arrays.copyOf(initPos, initPos.length);
		switch(dir)
		{
			case 0 :
				if(pos[0] % 2 != 0)
				{
					pos[0]--;
					pos[1]--;
				}
				else
				{
					pos[0]--;
				}
				break;
			case 1 :
				if(pos[0] % 2 != 0)
				{
					pos[0]--;
				}
				else
				{
					pos[0]--;
					pos[1]++;
				}
				break;
			case 2 :
				pos[1]++;
				break;
			case 3 :
				if(pos[0] % 2 != 0)
				{
					pos[0]++;
				}
				else
				{
					pos[0]++;
					pos[1]++;
				}
				break;
			case 4 :
				if(pos[0] % 2 != 0)
				{
					pos[0]++;
					pos[1]--;
				}
				else
				{
					pos[0]--;
				}
				break;
			case 5 :
				pos[1]--;
				break;
		}

		if(pos[0] < 0 || pos[0] >= plateau.length ||
		   pos[1] < 0 || pos[1] >= plateau.length ||
		   plateau[pos[0]][pos[1]] == Tuile.OUT_OF_BOUNDS
		  )
		  	return initPos;

		return pos;
	}

	public boolean avancer(Robot r, boolean canPush)
	{
		int[] pos = nextPos(r.getPos(), r.getDir());
		int initDirR = 0;
		boolean retour = false;
		if(pos == r.getPos())
			return retour;

		Tuile t = plateau[pos[0]][pos[1]];

		if(t == Tuile.OUT_OF_BOUNDS)
			return false;

		Robot nextHex = null;
		if(t == Tuile.ROBOT)
		{
			nextHex = getRobotAPosition(pos);
			initDirR = nextHex.getDir();
			while(nextHex.getDir() != r.getDir())
			nextHex.turnAround(true);
		}

		if(t == Tuile.VIDE ||
		  (t == Tuile.ROBOT && canPush && avancer(nextHex, false)) ||
		  (Tuile.isCristal(t) && canPush && avancer(pos, r.getDir())))
		{
			plateau[r.getPos()[0]][r.getPos()[1]] = Tuile.VIDE;
			r.setPos(pos);
			plateau[pos[0]][pos[1]] = Tuile.ROBOT;
			retour = true;
		}

		if(nextHex != null)
			while(nextHex.getDir() != initDirR)
				nextHex.turnAround(true);
		return retour;
	}

	private boolean avancer(int[] pos, int dir)
	{
		int[] next = nextPos(pos, dir);
		if(plateau[next[0]][next[1]] == Tuile.VIDE)
		{
			plateau[next[0]][next[1]] = plateau[pos[0]][pos[1]];
			plateau[pos[0]][pos[1]] = Tuile.VIDE;
			return true;
		}
		return false;
	}

	public void chargerCristal(Robot r)
	{
		int[] next = nextPos(r.getPos(), r.getDir());
		Tuile t = plateau[next[0]][next[1]];
		if(!Tuile.isCristal(t))
			return;

		if(r.chargerCrystal(t))
			plateau[next[0]][next[1]] = Tuile.VIDE;
	}

	public void deposerCristal(Robot r)
	{
		Tuile cristal = r.deposerCrystal();
		int[] next = nextPos(r.getPos(), r.getDir());
		Tuile t = plateau[next[0]][next[1]];
		if(t == Tuile.BASE || (t == Tuile.ROBOT && getRobotAPosition(next).chargerCrystal(cristal)) || (t == Tuile.VIDE)
		{
			if(t == Tuile.BASE)
				getJoueurParBase(next).addPoint();
			if(t == Tuile.VIDE)
				plateau[next[0]][next[1]] = cristal;
			return;
		}

		r.chargerCrystal(cristal);
	}

	private Joueur getJoueurParBase(int[] pos)
	{
		for(Joueur j : tabJoueurs)
		{
			if(j.getBase()[0] == pos[0] && j.getBase()[1] == pos[1])
				return j;
		}
		return null;
	}

	public String toString()
	{
		String retour = "";
		for (int i=0; i<plateau.length; i++)
		{
			if (i%2 == 0)
				retour +="  ";
			for (int j=0; j<plateau[i].length; j++)
			{
				retour +="  "+ plateau[i][j]+ " " ;
			}
			retour += "\n";
		}
		return retour;
	}

	public Tuile[][] getTuiles()
	{
		return this.plateau;
	}

	public int getNbJoueurs()
	{
		return 2;
	}
}

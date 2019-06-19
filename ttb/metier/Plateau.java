package ttb.metier;

import java.util.Arrays;

/**
 * Classe Plateau
 * @version 2019-06-18
 */

public class Plateau
{

	private Tuile[][] plateau;
	private Joueur[]  tabJoueurs;
	private int       joueurActuel;
	private String    fileAttent;

	public Plateau(Tuile[][] grille, Joueur[] tabJoueur, String fileAttent)
	{
		this.plateau    = grille;
		this.tabJoueurs = tabJoueur;
		this.fileAttent = fileAttent;
	}

	public Joueur getJoueurCourant() {return tabJoueurs[joueurActuel];}

	public void changerJoueur() {joueurActuel = (joueurActuel + 1) % tabJoueurs.length;}

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
					pos[1]--;

				pos[0]--;
			break;
			case 1 :
				if(pos[0] % 2 == 0)
					pos[1]++;

				pos[0]--;
			break;
			case 2 :
				pos[1]++;
			break;
			case 3 :
				if(pos[0] % 2 == 0)
					pos[1]++;

				pos[0]++;
			break;
			case 4 :
				if(pos[0] % 2 != 0)
					pos[1]--;

				pos[0]++;
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

		System.out.println("ok");

		return pos;
	}

	public boolean avancer(Robot r, boolean canPush)
	{
		int[] pos = nextPos(r.getPos(), r.getDir());
		int initDirR = 0;
		System.out.println(r.getDir());
		boolean retour = false;
		if(pos == r.getPos())
			return retour;

		Tuile t = plateau[pos[0]][pos[1]];

		if(t == Tuile.OUT_OF_BOUNDS)
			return false;

		Robot nextHex = null;
		if(t == Tuile.ROBOT)
		{
			nextHex  = getRobotAPosition(pos);
			initDirR = nextHex.getDir();
			System.out.println("help");
			nextHex.setDir(r.getDir());
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
		{
			System.out.println("gneee");
			nextHex.setDir(initDirR);
		}

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
		if(cristal == null)
			return;
		int[] next = nextPos(r.getPos(), r.getDir());
		Tuile t = plateau[next[0]][next[1]];
		boolean bOk = false;
		if(t == Tuile.ROBOT)
			bOk = getRobotAPosition(next).chargerCrystal(cristal);

		System.out.println(bOk);
		if(t == Tuile.BASE || (t == Tuile.ROBOT && bOk) || (t == Tuile.VIDE))
		{
			if(t == Tuile.BASE)
				getJoueurParBase(next).addPoint(t);
			if(t == Tuile.VIDE)
				plateau[next[0]][next[1]] = cristal;
			return;
		}

		r.chargerCrystal(cristal);
	}

	public Joueur getJoueurParBase(int[] pos)
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
		return tabJoueurs.length;
	}
}

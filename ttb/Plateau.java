package ttb;

import java.util.Arrays;
import java.util.Scanner;

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

		for(Robot r : tabRobot)
			if(pos[0] == r.getPos()[0] && pos[1] == r.getPos()[1])
				return r;

		return null;
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

		Robot nextHex = getRobotAPosition(pos);
		if(nextHex != null)
		{
			initDirR = nextHex.getDir();
			while(nextHex.getDir() != r.getDir())
				nextHex.turnAround(true);
		}

		if(nextHex == null || (canPush && avancer(nextHex, false)))
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
}
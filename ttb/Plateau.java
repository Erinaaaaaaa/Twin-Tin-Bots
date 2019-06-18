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
				plateau[i][j] = Tuile.VIDE; // Tuile vide
			}
		}

		r = new Robot(NB_LIGNES/2, NB_COLONNES/2, 2);
		autreR = new Robot(NB_LIGNES/2, NB_COLONNES/2+1, 5);
		uit = new Robot(NB_LIGNES/2, NB_COLONNES/2+2, 5);
	}

	private Joueur getJoueurCourant() {return tabJoueurs[joueurActuel];}

	private void changerJoueur() {joueurActuel = joueurActuel++ % tabJoueurs.length;}

	public Robot isFree(int[] pos)
	{
		if(pos[0] == autreR.getPos()[0] && pos[1] == autreR.getPos()[1])
			return autreR;

		if(pos[0] == uit.getPos()[0] && pos[1] == uit.getPos()[1])
			return uit;

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
		if(pos == r.getPos())
			return false;

		Robot nextHex = isFree(pos);
		if(nextHex != null)
		{
			initDirR = nextHex.getDir();
			nextHex.setDir(r.getDir());
		}

		if(isFree(pos) == null || (canPush && avancer(nextHex, false)))
		{
			plateau[r.getPos()[0][r.getPos()[1] = "T";
			r.setPos(pos);
			plateau[pos[0]][pos[1]] = "R";
			if(nextHex != null)
				nextHex.setDir(initDirR);
			return true;
		}

		if(nextHex != null)
			nextHex.setDir(initDirR);
		return false;
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

	public static void main(String[] args) {
		new Plateau(2).jouer();
	}
}

package ttb.metier;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe Plateau
 * @author Jérémy Auzou
 * @author Matys Achart
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */

public class Plateau
{

	private Tuile[][] plateau;
	private Joueur[]  tabJoueurs;
	private int       joueurActuel;
	private String    fileAttent;
	private int       pointVictoire;
	private int       pionDecompte;
	private int       nbTours;

	/**
	 * Construit un plateau.
	 * Initialise la grille, les joueurs et la file d'attente de cristaux.
	 * @param grille la grille du jeu.
	 * @param tabJoueur le tableau contenant tous les joueurs.
	 * @param fileAttent la file d'attente de cristaux.
	 */
	public Plateau(Tuile[][] grille, Joueur[] tabJoueur, String fileAttent)
	{
		this.plateau    = grille;
		this.tabJoueurs = tabJoueur;
		this.fileAttent = fileAttent;
		pointVictoire   = 13 - tabJoueur.length;
		pionDecompte    = 3;
		nbTours         = 0;
	}

	/** Retourne le joueur devant jouer. */
	public Joueur getJoueurCourant() {return tabJoueurs[joueurActuel];}

	/** Change le joueur courant, après la fin du tour. */
	public void changerJoueur()
	{
		joueurActuel = (joueurActuel + 1) % tabJoueurs.length;
		nbTours++;
	}

	/**
	 * Retourne le robot, s'il existe, à la position donnée.
	 * @param pos la position du robot.
	 * @return le robot ayant cette position.
	 */
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

		if(pos[0] < 0 || pos[0]    >= plateau.length ||
		   pos[1] < 0 || pos[1]    >= plateau.length ||
		   plateau[pos[0]][pos[1]] == Tuile.OUT_OF_BOUNDS
		  )
		  	return initPos;

		return pos;
	}

	/**
	 * Fait avancer un robot.
	 * @param r le Robot à faire avancer.
	 * @param canPush true si le robot peut pousser, false sinon.
	 * @return true si le robot a avancé, false sinon.
	 */
	public boolean avancer(Robot r, boolean canPush)
	{
		int[] pos      = nextPos(r.getPos(), r.getDir());
		int initDirR   = 0;
		boolean retour = false;

		if(pos == r.getPos()) return retour;

		Tuile t = plateau[pos[0]][pos[1]];

		if(t == Tuile.OUT_OF_BOUNDS) return false;

		Robot nextHex = null;

		if(t == Tuile.ROBOT)
		{
			nextHex  = getRobotAPosition(pos);
			initDirR = nextHex.getDir();
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
			nextHex.setDir(initDirR);

		return retour;
	}

	private boolean avancer(int[] pos, int dir)
	{
		int[] next = nextPos(pos, dir);
		if(plateau[next[0]][next[1]] == Tuile.VIDE)
		{
			plateau[next[0]][next[1]] = plateau[pos[0]][pos[1]];
			plateau[pos[0]][pos[1]]   = Tuile.VIDE;
			return true;
		}
		return false;
	}

	/**
	 * Charge un cristal sur un robot.
	 * @param r le robot pour lequel charger le cristal.
	 */
	public void chargerCristal(Robot r)
	{
		int[] next = nextPos(r.getPos(), r.getDir());
		Tuile t    = plateau[next[0]][next[1]];

		if(!Tuile.isCristal(t) && t != Tuile.ROBOT)
			return;

		if(Tuile.isCristal(t))
		{
			if(r.chargerCristal(t))
				plateau[next[0]][next[1]] = Tuile.VIDE;
		}
		else
		{
			Robot tmp = getRobotAPosition(next);
			Tuile cristal = tmp.deposerCristal();
			if(cristal == null)
				return;

			if(!r.chargerCristal(cristal))
				tmp.chargerCristal(cristal);
		}
	}

	/**
	 * Dépose le cristal du robot en face de lui.
	 * @param r le robot pour lequel déposer le cristal.
	 */
	public void deposerCristal(Robot r)
	{
		Tuile cristal = r.deposerCristal();
		if(cristal == null)
			return;

		int[] next  = nextPos(r.getPos(), r.getDir());
		Tuile t     = plateau[next[0]][next[1]];
		boolean bOk = false;

		if(t == Tuile.ROBOT)
			bOk = getRobotAPosition(next).chargerCristal(cristal);

		if(t == Tuile.BASE || (t == Tuile.ROBOT && bOk) || (t == Tuile.VIDE))
		{
			if(t == Tuile.BASE)
			{
				getJoueurParBase(next).addCristal(cristal);
				listeAttente();
			}

			if(t == Tuile.VIDE)
				plateau[next[0]][next[1]] = cristal;

			return;
		}

		r.chargerCristal(cristal);
	}

	/**
	 * Prend un cristal de la file d'attente.
	 */
	public void listeAttente()
	{
		if(fileAttent.equals("")) return;

		Tuile cristal = Tuile.getCristal(fileAttent.charAt(0));

		if(fileAttent.length() > 1)
			fileAttent = fileAttent.substring(1);
		else
			fileAttent = "";

		if(plateau[plateau.length / 2][plateau[0].length / 2] == Tuile.VIDE)
		{
			plateau[plateau.length / 2][plateau[0].length / 2] = cristal;
			return;
		}

		for(int i = 0; i < 6; i++)
		{
			int[] nextCase = nextPos(new int[]{plateau.length / 2, plateau[0].length / 2}, i);
			if(plateau[nextCase[0]][nextCase[1]] == Tuile.VIDE)
			{
				plateau[nextCase[0]][nextCase[1]] = cristal;
				return;
			}
		}
	}

	/** Permet d'obtenir le Joueur grâce à la position de sa base. */
	public Joueur getJoueurParBase(int[] pos)
	{
		for(Joueur j : tabJoueurs)
		{
			if(j.getBase()[0] == pos[0] && j.getBase()[1] == pos[1])
				return j;
		}
		return null;
	}

	public Tuile[][] getTuiles(){     return this.plateau;}
	public int       getNbJoueurs(){  return tabJoueurs.length;}
	public String    getFileAttente(){return this.fileAttent;}
	public int       getNbLigne(){    return this.plateau.length;}
	public int       getNbColonne(){  return this.plateau[0].length;}

	/** Retourne le symbole utilisé pour définir une Tuile du plateau. */
	public String getSymbole(int lig, int col){return this.plateau[lig][col].toString();}

	/**
	 * Retourne la couleur de l'objet positionné dans le plateau,
	 * utilisé pour l'affichage des couleurs en CUI.
	 * @param lig
	 * @param col
	 * @return le nom de la couleur.
	 */
	public String getCouleur(int lig, int col)
	{
		switch(this.getSymbole(lig,col))
		{
			case "R" :return getRobotAPosition(new int[]{lig,col}).getCouleur();
			case "B" :return getJoueurParBase(new int[]{lig,col}).getCouleur();
			case "2" :return "Cyan";
			case "3" :return "Vert";
			case "4" :return "Violet";
		}
		return "";
	}

	public String toString()
	{
		String retour = "";
		for (int i=0; i < plateau.length; i++)
		{
			if (i%2 == 0)
				retour += "  ";

			for (int j=0; j<plateau[i].length; j++)
			{
				retour +="  "+ plateau[i][j]+ " " ;
			}

			retour += "\n";
		}
		return retour;
	}

	/**
	 * Détermine la fin de la partie. La partie peut être finie de deux façons :
	 * un des joueurs a suffisamment de points, ou 3 tours ont été effectués
	 * alors qu'il n'y avait plus de cristaux dans la file d'attente.
	 * @return true si la partie est finie, false sinon.
	 */
	public boolean estPartieFinie()
	{
		boolean fini = false;
		for(Joueur j : tabJoueurs)
			if(j.getPoints() >= pointVictoire)
				fini = true;

		return (fini || pionDecompte == 0);
	}

	/**
	 * Détermine le ou les gagnants de la partie.
	 * @return une liste du ou des gagnants.
	 */
	public List<Joueur> getGagnant()
	{
		List<Joueur> gagnants = new ArrayList<Joueur>();
		Joueur gagnant = null;
		if (pionDecompte != 0) // Cas de victoire par nombre de points
		{
			for (Joueur j : tabJoueurs)
				if (j.getPoints() >= pointVictoire)
					gagnant = j;

			gagnants.add(gagnant);
		}
		else // si il n'y a plus de pions décompte, on calcule comme ceci
		{
			int[] totalPoints = new int[Joueur.nbJoueurs];
			int ptsMax;
			boolean egalite = false;
			for (int i = 0; i < totalPoints.length; i++)
			{
				totalPoints[i] = tabJoueurs[i].getPoints();
				for (Robot r : tabJoueurs[i].getRobots())
					if (r.hasCristal())
						totalPoints[i] += Integer.parseInt(r.getCristal().toString())-1;
			}

			ptsMax = totalPoints[0];
			gagnant = tabJoueurs[0];
			for (int i = 1; i < tabJoueurs.length; i++)
			{
				if (ptsMax < totalPoints[i])
				{
					ptsMax = totalPoints[i];
					gagnant = tabJoueurs[i];
				}
				else if (ptsMax == totalPoints[i])
					egalite = true;
			}

			if (egalite)
			{
				// Le gagnant est celui qui a le plus de cristaux bleus.
				gagnant = getMaxPtsJoueur(Tuile.CRISTAL_BLEU);
				if (gagnant == null) // si encore égalité : le plus de cristaux verts
					gagnant = getMaxPtsJoueur(Tuile.CRISTAL_VERT);
				
				if (gagnant == null)
				{
					for (int i = 0; i < tabJoueurs.length; i++)
						totalPoints[i] = tabJoueurs[i].getPoints();

					for (int max : totalPoints)
						ptsMax = Math.max(ptsMax, max);

					for (int i = 0; i < tabJoueurs.length; i++)
						if (totalPoints[i] == ptsMax)
							gagnants.add(tabJoueurs[i]);
				}
				else
					gagnants.add(gagnant);
			}
			else
				gagnants.add(gagnant);
		}
		return gagnants;
	}

	private Joueur getMaxPtsJoueur(Tuile t)
	{
		Joueur retour = null;
		int max = 0, nbCristaux = 0;
		for (Joueur j : tabJoueurs)
		{
			nbCristaux = Collections.frequency(j.getCristaux(), t);
			if (max < nbCristaux)
			{
				max = nbCristaux;
				retour = j;
			}
			else if (max == nbCristaux)
				return null;
		}
		return retour;
	}

	/** Enlève un pion décompte. */
	public void enleverPionDecompte() { pionDecompte--; }

	/** Retourne le joueur en fonction de son id. */
	public Joueur getJoueur(int id) {return tabJoueurs[id];}

	/** Retourne le nombre de tours effectués depuis le début de la partie. */
	public int getNbTours()
	{
		return nbTours;
	}

	public void setJoueurCourant(int id)
	{
		if (id >= 0 && id < tabJoueurs.length)
			this.joueurActuel = id;
	}
}

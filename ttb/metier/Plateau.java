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
	private int       pointVictoire;
	private int       pionDecompte;

	public Plateau(Tuile[][] grille, Joueur[] tabJoueur, String fileAttent)
	{
		this.plateau    = grille;
		this.tabJoueurs = tabJoueur;
		this.fileAttent = fileAttent;
		pointVictoire   = 13 - tabJoueur.length;
		pionDecompte    = 3;
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

		if(pos[0] < 0 || pos[0]    >= plateau.length ||
		   pos[1] < 0 || pos[1]    >= plateau.length ||
		   plateau[pos[0]][pos[1]] == Tuile.OUT_OF_BOUNDS
		  )
		  	return initPos;

		return pos;
	}

	public boolean avancer(Robot r, boolean canPush)
	{
		int[] pos      = nextPos(r.getPos(), r.getDir());
		int initDirR   = 0;
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

	// TODO: prendre le cristal du robot en face
	public void chargerCristal(Robot r)
	{
		int[] next = nextPos(r.getPos(), r.getDir());
		Tuile t    = plateau[next[0]][next[1]];

		if(!Tuile.isCristal(t) && t != Tuile.ROBOT)
			return;

		if(Tuile.isCristal(t))
		{
			if(r.chargerCrystal(t))
				plateau[next[0]][next[1]] = Tuile.VIDE;
		}
		else
		{
			Robot tmp = getRobotAPosition(next);
			Tuile cristal = tmp.deposerCrystal();
			if(cristal == null)
				return;

			if(!r.chargerCrystal(cristal))
				tmp.chargerCrystal(cristal);
		}
	}

	public void deposerCristal(Robot r)
	{
		Tuile cristal = r.deposerCrystal();
		if(cristal == null)
			return;

		int[] next  = nextPos(r.getPos(), r.getDir());
		Tuile t     = plateau[next[0]][next[1]];
		boolean bOk = false;

		if(t == Tuile.ROBOT)
			bOk = getRobotAPosition(next).chargerCrystal(cristal);

		if(t == Tuile.BASE || (t == Tuile.ROBOT && bOk) || (t == Tuile.VIDE))
		{
			if(t == Tuile.BASE)
				getJoueurParBase(next).addCristal(t);

			if(t == Tuile.VIDE)
				plateau[next[0]][next[1]] = cristal;

			return;
		}

		r.chargerCrystal(cristal);
	}

	public boolean aPlusDeCristaux()
	{
		for(int i = 0; i < plateau.length; i++)
			for(int j = 0; j < plateau[i].length; j++)
				if(Tuile.isCristal(plateau[i][j]))
					return false;

		return true;
	}

	/* public void listeAttente()
	{
		if(fileAttent.equals(""))
			return;

		Tuile cristal = Tuile.getCristal(fileAttent.charAt(0));

		if(fileAttent.length > 1)
			fileAttent = fileAttent.substring(1);
		else
			fileAttent = "";

		if(plateau[plateau.length / 2][plateau[0].length / 2] == Tuile.VIDE)
			plateau[plateau.length / 2][plateau[0].length / 2] = cristal;
	} */

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

	public String getSymbole(int lig, int col){return this.plateau[lig][col].toString();}

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

	public boolean estPartieFinie()
	{
		boolean fini = false;
		for(Joueur j : tabJoueurs)
			if(j.getPoints() >= pointVictoire)
				fini = true;

		return (fini || pionDecompte == 0);
	}

	public Joueur getGagnant()
	{
		Joueur gagnant = null;
		if (pionDecompte != 0)
		{
			for (Joueur j : tabJoueurs)
				if (j.getPoints() >= pointVictoire)
					gagnant = j;
		}
		else
		{
			int[] totalPoints = new int[Joueur.nbJoueurs];
			int ptsMax, indGagne;
			for (int i = 0; i < totalPoints.length; i++)
			{
				totalPoints[i] = tabJoueurs[i].getPoints();
				for (Robot r : tabJoueurs[i].getRobots())
					if (r.hasCristal())
						totalPoints[i] += Integer.parseInt(r.getCristal().toString())-1;
			}

			indGagne = 0;
			ptsMax = totalPoints[indGagne];
			for (int i = 1; i < totalPoints.length; i++)
				if (ptsMax < totalPoints[i])
					indGagne = i;

			gagnant = tabJoueurs[indGagne];
			// TODO: gestion de victoire quand ptsMax égal pour 2 joueurs ou plus
			// plus gérer en fonction du nb de cristaux d'une certaine couleur.
		}

		return gagnant;
	}

	public void enleverPionDecompte() { pionDecompte--; }

	public Joueur getJoueur(int id) {return tabJoueurs[id];}
}

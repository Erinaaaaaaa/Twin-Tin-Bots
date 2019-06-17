import java.util.Arrays;
import java.util.Scanner;

public class Plateau
{
	private final int NB_LIGNES;
	private final int NB_COLONNES;
	private Tuile[][] plateau;
	//private Joueur[]  tabJoueurs;
	private int joueurActuel;
	private Robot r;

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

		r = new Robot(NB_LIGNES/2, NB_COLONNES/2, 6);
		jouer();
	}

	private void jouer()
	{
		Scanner sc = new Scanner(System.in);
		do
		{
			System.out.println(toString());
			System.out.println("Entrez une lettre (A, D, G)");
			String choix = sc.next();
			switch(choix)
			{
				case "A" :
					avancer(r, true);
					break;
				case "D" :
					r.turnAround(true);
					break;
				case "G" :
					r.turnAround(false);
					break;
			}
		} while (true);
	}

	//private Joueur getJoueurCourant() {return tabJoueurs[joueurActuel];}

	//private void changerJoueur() {joueurActuel = joueurActuel++ % tabJoueurs.length;}

	public boolean isNextFree(Robot r, int dir)
	{
		int[] pos = r.getPos();
		pos = nextPos(pos, dir);
		return isFree(pos) == null;
	}

	public Robot isFree(int[] pos)
	{
		/*for(Joueur j : tabJoueurs)
		{
			for(Robot r : j.getRobots())
			{
				if(r.getPos() == pos)
					return r;
			}
		}*/

		return null;
	}

	private int[] nextPos(int[] initPos, int dir)
	{
		int[] pos = Arrays.copyOf(initPos, initPos.length);
		switch(dir)
		{
			case 1 :
				if(pos[0] % 2 == 0)
				{
					pos[0]--;
					pos[1]--;
				}
				else
				{
					pos[0]--;
				}
				break;
			case 2 :
				if(pos[0] % 2 == 0)
				{
					pos[0]--;
				}
				else
				{
					pos[0]--;
					pos[1]++;
				}
				break;
			case 3 :
				pos[1]++;
				break;
			case 4 :
				if(pos[0] % 2 == 0)
				{
					pos[0]++;
				}
				else
				{
					pos[0]++;
					pos[1]++;
				}
				break;
			case 5 :
				if(pos[0] % 2 == 0)
				{
					pos[0]++;
					pos[1]--;
				}
				else
				{
					pos[0]--;
				}
				break;
			case 6 :
				pos[1]--;
				break;
		}

		if(pos[0] < 0 || pos[0] >= plateau.length ||
		   pos[1] < 0 || pos[1] >= plateau.length ||
		   plateau[pos[0]][pos[1]] == null
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
			r.setPos(pos);
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
			//if (i%2!=0)
			//	System.out.print("  ");
			for (int j=0; j<plateau[i].length; j++)
			{
				if(r.getPos()[0] == i && r.getPos()[1] == j)
					retour += "  R ";
				else
					retour += "  "+plateau[i][j] + " ";
			}
			retour += "\n";
		}
		return retour;
	}

	public static void main(String[] args) {
		new Plateau(2);
	}
}

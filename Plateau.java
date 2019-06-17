import java.util.Arrays;

public class Plateau
{
	private final int NB_LIGNES;
	private final int NB_COLONNES;
	private String[][] plateau;
	private Joueur[] tabJoueurs;
	private int joueurActuel;

	public Plateau(int nbJoueurs)
	{
		NB_LIGNES   = 9 + nbJoueurs > 4?2:0;
		NB_COLONNES = 9  + nbJoueurs > 4?3:0;
	}

	private Joueur getJoueurCourant() {return tabJoueurs[joueurActuel];}

	private void changerJoueur() {joueurActuel++ % tabJoueurs.length;}

	public boolean isNextFree(Robot r, int dir)
	{
		int[] pos = joueur.getPos();
		pos = nextPos(pos, dir);
		return isFree(pos);
	}

	public Robot isFree(int[] pos)
	{
		for(Joueur j : tabJoueurs)
		{
			for(Robot r : j.getRobots())
			{
				if(r.getPos() == pos)
					return r;
			}
		}

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
}

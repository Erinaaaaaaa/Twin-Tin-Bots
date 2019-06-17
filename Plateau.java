import java.util.Arrays;

public class Plateau
{
	private static char[] tabDir = {'N', 'O', 'S', 'E'};
	private final  int    NB_LIGNES;
	private final  int    NB_COLONNES;

	private char[]   plateau;
	private Joueur[] tabJoueurs;

	public Plateau(int nbJoueurs)
	{
		NB_LIGNES   = 11 + nbJoueurs > 4?2:0;
		NB_COLONNES = 9  + nbJoueurs > 4?3:0;
	}

	public boolean isNextFree(Joueur joueur, char dir)
	{
		int[] pos = joueur.getPos();
		pos = nextPos(pos, dir);
		for(Joueur j : tabJoueurs)
		{
			if(j != joueur && j.getPos() == pos)
				return false;
		}

		return true;
	}

	private int[] nextPos(int[] initPos, char dir)
	{
		int[] pos = Arrays.copyOf(initPos, initPos.length);
		for(int i = 0; i < Plateau.tabDir.length; i++)
		{
			if(dir == Plateau.tabDir[i])
			{
				if(i < 2)
					pos[i]--;

				if(i >= 2)
					pos[i % 2]++;
			}
		}

		return pos;
	}
}

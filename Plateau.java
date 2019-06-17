import java.util.Arrays;

public class Plateau
{
	private final int NB_LIGNES;
	private final int NB_COLONNES;
	private Joueur[] tabJoueurs;

	public Plateau(int nbJoueurs)
	{

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
		char[] tabDir = {'N', 'O', 'S', 'E'};
		int[] pos = Arrays.copyOf(initPos, initPos.length);
		for(int i = 0; i < tabDir.length; i++)
		{
			if(dir == tabDir[i])
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

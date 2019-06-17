public class Joueur
{
	private int[] pos;
	private char dir;

	public Joueur(int ligne, int colonne, char dir)
	{
		this.pos = {ligne, colonne};
		this.dir = dir;
	}

	public char getDir() {return dir;}
	public int[] getPos() {return pos;}

	public void chargeDir(char dir) {this.dir = dir;}
}

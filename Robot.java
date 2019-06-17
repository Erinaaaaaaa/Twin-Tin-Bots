public class Robot
{
	private int[] pos;
	/** Direction du robot, commence à 0 et va jusqu'à 5, sens horaire */
	private int   dir;

	public Robot(int ligne, int colonne, int dir)
	{
		this.pos = new int[] {ligne, colonne};
		this.dir = dir;
	}

	public int   getDir() {return dir;}
	public int[] getPos() {return pos;}

	public void setPos(int[] pos) {this.pos = pos;}

	public void setDir(int dir) {this.dir = dir;}

	public void turnAround(boolean right)
	{
		if(right)
			dir = dir-- % 6;
		else
			dir = dir++ % 6;
	}
}

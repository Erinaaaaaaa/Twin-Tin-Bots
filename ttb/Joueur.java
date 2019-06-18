package ttb;

public class Joueur
{
	public static Color[] tabCouleur = {Color.RED, Color.YELLOW, Color.GREEN};
	private static int nbJoueurs = 0;
	private int id;
	private Robot[] tabRobot;
	private int[] posBase;
	private int points;

	public Joueur()
	{
		id = Joueur.nbJoueurs++;
		this.tabRobot = new Robot[] {null, null};
		this.points = 0;
	}

	public Robot[] getRobots() {return this.tabRobot;}

	public Robot getRobot(int id) {return tabRobot[id];}

	public void ajouterRobot(Robot r)
	{
		if(tabRobot[0] == null)
		{
			this.tabRobot[0] = r;
			return;
		}
		if(tabRobot[1] == null)
			this.tabRobot[1] = r;
	}

	public int[] addPoint() {points++;}
}

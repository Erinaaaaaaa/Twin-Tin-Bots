package ttb;

public class Joueur
{
	private static int nbJoueurs = 1;
	private int id;
	private Robot[] tabRobot;
	private int[] posBase;

	public Joueur()
	{
		id = Joueur.nbJoueurs++;
		this.tabRobot = new Robot[] {null, null};
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

	public int[] getBase() {return this.posBase;}
}

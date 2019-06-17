package ttb;

import java.util.Arrays;
import javafx.scene.paint.Color;

public class Joueur
{
	private Color   couleur;
	private Robot[] tabRobot;
	private int[] posBase;

	public Joueur(Color couleur, Robot[] tabRobot, int[] posBase)
	{
		this.couleur = couleur;
		this.tabRobot = Arrays.copyOf(tabRobot, tabRobot.length);
		this.posBase = posBase;
	}

	public Robot[] getRobots() {return this.tabRobot;}

	public int[] getBase() {return this.posBase;}
}

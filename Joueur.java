import java.util.Arrays;
import javafx.scene.paint.Color;

public class Joueur
{
	private Color   couleur;
	private Robot[] tabRobot;

	public Joueur(Color couleur, Robot[] tabRobot)
	{
		this.couleur = couleur;
		this.tabRobot = Arrays.copyOf(tabRobot, tabRobot.length);
	}
}

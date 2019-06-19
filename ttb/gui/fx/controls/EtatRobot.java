package ttb.gui.fx.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ttb.metier.Joueur;
import ttb.metier.Robot;

import java.io.IOException;

public class EtatRobot extends TitledPane
{
	private Robot r1;
	private Robot r2;

	@FXML	private ImageView imgR1;
	@FXML	private ImageView imgR1A1;
	@FXML	private ImageView imgR1A2;
	@FXML	private ImageView imgR1A3;
	@FXML	private ImageView imgR2;
	@FXML	private ImageView imgR2A1;
	@FXML	private ImageView imgR2A2;
	@FXML   private ImageView imgR2A3;

	public EtatRobot(Joueur j)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EtatRobot.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		this.r1 = j.getRobot(0);
		this.r2 = j.getRobot(1);

		try
		{
			loader.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		this.setText("Joueur " + (j.getId() + 1) );
	}

	public void updateStatus()
	{
		this.imgR1.setImage(new Image(
				getClass().getResourceAsStream("../../images/robots/petit" + this.r1.getJoueur().getId() + ".png")
		));

		this.imgR2.setImage(new Image(
				getClass().getResourceAsStream("../../images/robots/gros" + this.r1.getJoueur().getId() + ".png")
		));
	}
}


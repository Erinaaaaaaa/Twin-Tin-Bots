package ttb.gui.fx.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ttb.metier.Robot;
import ttb.metier.Tuile;

import java.io.IOException;

public class ImageRobot extends Pane
{
	@FXML private ImageView imgRobot;
	@FXML private ImageView imgCristal;

	public ImageRobot(Robot r)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ImageRobot.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try
		{
			loader.load();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		int robotType = r.getId(); // 0: petit; 1: gros
		int idJoueur = r.getJoueur().getId();
		Tuile cristal = r.getCristal(); // 2/3/4 pour bleu/vert/violet

		String fic = "../../images/robots/";

		switch (robotType)
		{
			case 0:
				fic += "petit";
				break;
			case 1:
				fic += "gros";
				break;
			default:
				break;
		}

		Image robotImg = new Image(getClass().getResourceAsStream(fic + idJoueur + ".png"));
		this.imgRobot.setImage(robotImg);

		if (cristal == null)
			this.imgCristal.setImage(null);
		else
		{
			if (cristal.toString().matches("[234]"))
				this.imgCristal.setImage(
						new Image(getClass().getResourceAsStream(
								"../../images/cristaux/gem"+cristal.toString()+".png"
						))
				);
			else
				this.imgCristal.setImage(
						new Image(getClass().getResourceAsStream(
								"../../images/inconnu.png"
						))
				);
		}
	}
}

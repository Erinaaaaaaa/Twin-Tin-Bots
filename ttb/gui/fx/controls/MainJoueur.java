package ttb.gui.fx.controls;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import ttb.gui.fx.ControleurIhm;
import ttb.gui.fx.util.Action;
import ttb.metier.Joueur;

import java.io.IOException;
import java.util.ArrayList;

public class MainJoueur extends FlowPane
{
	private Joueur j;
	private ControleurIhm ctrl;

	public MainJoueur(Joueur j, ControleurIhm ctrl)
	{
		this.ctrl = ctrl;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainJoueur.fxml"));
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

		ArrayList<Character> reserve = j.getReserve();

		for (int i = 0; i < reserve.size(); i++)
		{
			ImageView img = creerImage(reserve.get(i));
			img.setUserData(i);

			FlowPane.setMargin(img, new Insets(5));

			this.getChildren().add(img);
		}
	}

	private ImageView creerImage(char c)
	{
		ImageView iv = new ImageView();
		iv.setFitWidth(60);
		iv.setFitHeight(60);

		String uri = getImageFromTuile(c);

		iv.setImage(new Image(getClass().getResourceAsStream(uri)));
		iv.addEventHandler(MouseEvent.MOUSE_CLICKED, this::imageClick);

		return iv;
	}

	static String getImageFromTuile(char c)
	{
		switch (c)
		{
			case 'A':
				return "../../images/actions/avancer_1.png";
			case 'S':
				return "../../images/actions/avancer_2.png";
			case 'G':
				return "../../images/actions/tourner_gauche.png";
			case 'D':
				return "../../images/actions/tourner_droite.png";
			case 'C':
				return "../../images/actions/charger.png";
			case 'E':
				return "../../images/actions/deposer.png";
			default:
				return "../../images/inconnu.png";
		}
	}

	void imageClick(MouseEvent event)
	{
		System.out.println((int)((ImageView)event.getSource()).getUserData());

		int indice = (int)((ImageView)event.getSource()).getUserData();

		this.ctrl.setSource(new Action(indice, -1, this.ctrl.getJoueurCourant().getReserve().get(indice)));
	}
}
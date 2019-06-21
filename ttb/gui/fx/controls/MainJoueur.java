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

/**
 * Classe MainJoueur
 *
 * Affiche visuellement la réserve du joueur courant
 *
 * @author Jérémy AUZOU
 * @author Matys ACHART
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */

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

			// Insère l'indice de l'action dans l'ImageView pour le passer au contrôleur
			img.setUserData(i);

			FlowPane.setMargin(img, new Insets(5));

			this.getChildren().add(img);
		}
	}

	/**
	 * Crée une ImageView en fonction du de l'action passée en paramètre
	 * @param c caractère représentant une action
	 * @return ImageView avec l'image représentant l'action
	 */
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

	/**
	 * Retourne le chemin d'accès à l'image correspondant au caractère passé en paramètre
	 * @param c caractère représentant une tuile d'action
	 * @return chemin d'accès à l'image
	 */
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

	/**
	 * Gestionnaire d'évènement de clic sur une image
	 */
	void imageClick(MouseEvent event)
	{
		int indice = (int)((ImageView)event.getSource()).getUserData();

		this.ctrl.setSource(new Action(indice, -1, this.ctrl.getJoueurCourant().getReserve().get(indice)));
	}
}

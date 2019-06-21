package ttb.gui.fx.controls;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ttb.gui.fx.ControleurIhm;
import ttb.gui.fx.util.Action;
import ttb.metier.Joueur;

import java.io.IOException;

import static ttb.gui.fx.util.Dialog.demander;

/**
 * Classe EtatRobot
 * @author Jérémy AUZOU
 * @author Matys ACHART
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */

public class EtatRobot extends TitledPane
{
	private Joueur j;
	private ControleurIhm ctrl;

	@FXML	private ImageView imgR1;
	@FXML	private ImageView imgR1A1;
	@FXML	private ImageView imgR1A2;
	@FXML	private ImageView imgR1A3;
	@FXML	private ImageView imgR2;
	@FXML	private ImageView imgR2A1;
	@FXML	private ImageView imgR2A2;
	@FXML   private ImageView imgR2A3;

	public EtatRobot(Joueur j, ControleurIhm ctrl)
	{
		this.ctrl = ctrl;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("EtatRobot.fxml"));
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

		this.j = j;

		this.setText("Joueur " + (j.getId() + 1) );
	}

	/**
	 * Met à jour le contenu de cet EtatRobot avec des informations à jour sur ce joueur.
	 * Le texte du panneau est mis en GRAS si celui-ci représente le joueur courant.
	 */
	public void updateStatus()
	{
		// Mettre à jour le titre de cette TitledPane
		this.setText("Joueur " + (j.getId() + 1) + " - " + j.getPoints() + " point" + (j.getPoints() > 1 ? "s" : ""));
		this.setFont(Font.font(
				this.getFont().getFamily(),
				(ctrl.getJoueurCourant() == j) ? FontWeight.BOLD : FontWeight.NORMAL,
				this.getFont().getSize()
		));

		// Définir l'image du des robots et des tuiles
		this.imgR1.setImage(new Image(
				getClass().getResourceAsStream("../../images/robots/petit" + this.j.getId() + ".png")
		));

		this.imgR1A1.setImage(getImageTuile(this.j.getOrdres(0)[0]));
		this.imgR1A2.setImage(getImageTuile(this.j.getOrdres(0)[1]));
		this.imgR1A3.setImage(getImageTuile(this.j.getOrdres(0)[2]));

		this.imgR2.setImage(new Image(
				getClass().getResourceAsStream("../../images/robots/gros" + this.j.getId() + ".png")
		));

		this.imgR2A1.setImage(getImageTuile(this.j.getOrdres(1)[0]));
		this.imgR2A2.setImage(getImageTuile(this.j.getOrdres(1)[1]));
		this.imgR2A3.setImage(getImageTuile(this.j.getOrdres(1)[2]));
	}

	/**
	 * Obtenir une Image représentant une tuile d'action
	 * @param action caractère représentant l'action
	 * @return l'Image avec l'image de tuile
	 */
	private Image getImageTuile(char action)
	{
		String uri = MainJoueur.getImageFromTuile(action);

		return new Image(getClass().getResourceAsStream(uri));
	}

	/**
	 * Gestionnaire d'évènement pour un clic sur image
	 */
	@FXML
	void imgClick(MouseEvent event)
	{
		if (ctrl.getJoueurCourant() != this.j)
			return;

		ImageView iv = (ImageView) event.getSource();

		int robot = -1;
		int indice = -1;


		if (iv == this.imgR1A1) { robot = 0; indice = 0;}
		if (iv == this.imgR1A2) { robot = 0; indice = 1;}
		if (iv == this.imgR1A3) { robot = 0; indice = 2;}

		if (iv == this.imgR2A1) { robot = 1; indice = 0;}
		if (iv == this.imgR2A2) { robot = 1; indice = 1;}
		if (iv == this.imgR2A3) { robot = 1; indice = 2;}

		if (event.getButton().equals(MouseButton.PRIMARY))
		{
			if (ctrl.getSource() != null && (ctrl.getSource().getRobot() == -1 || ctrl.getSource().getRobot() == robot))
			{
				ctrl.ajouterOrdre(robot, indice);
			}
			else
			{
				ctrl.setSource(new Action(indice, robot,
						this.ctrl.getJoueurCourant().getOrdres(robot)[indice]));
			}
		}
		if (event.getButton().equals(MouseButton.SECONDARY))
		{
			if (demander("Vous allez supprimer une tuile"))
			{
				ctrl.supprimerOrdre(robot, indice);
			}
		}
	}

	/**
	 * Gestionnaire d'évènemment du bouton du reset du robot 1
	 */
	@FXML
	void robot1Reset(ActionEvent event)
	{
		if (j == this.ctrl.getJoueurCourant())
			this.ctrl.resetRobot(0);
	}

	/**
	 * Gestionnaire d'évènemment du bouton du reset du robot 2
	 */
	@FXML
	void robot2Reset(ActionEvent event)
	{
		if (j == this.ctrl.getJoueurCourant())
			this.ctrl.resetRobot(1);
	}
}


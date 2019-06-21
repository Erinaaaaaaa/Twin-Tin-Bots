package ttb.gui.fx.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;
import ttb.metier.Joueur;

import java.util.List;
import java.util.Optional;

/**
 * Classe Dialog
 *
 * Boite à outils pour afficher diverses popups
 *
 * @author Jérémy Auzou
 * @author Matys Achart
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */

public class Dialog
{
	/**
	 * Demande le nombre de joueurs à l'utilisateur
	 * @return nombre de joueurs. -1 si aucune réponse
	 */
	public static int lireNbJoueur()
	{
		TextInputDialog dialog = new TextInputDialog("2");
		dialog.setTitle("Choix de joueur");
		dialog.setHeaderText("Choix du nombre de joueurs");
		dialog.setContentText("Entrez le nombre de joueurs voulu:");
		dialog.initStyle(StageStyle.UTILITY);

		Optional<String> result = dialog.showAndWait();
		return result.map(Integer::parseInt).orElse(-1);
	}

	/**
	 * Demande le numéro du scénario à l'utilisateur
	 * @return le numéro du scénarion. -1 si aucune réponse
	 */
	public static int lireNumScenario()
	{
		TextInputDialog dialog = new TextInputDialog("2");
		dialog.setTitle("Choix du scénario");
		dialog.setHeaderText("Choix du numéro de scénario");
		dialog.setContentText("Entrez le numéro du scénario voulu:");
		dialog.initStyle(StageStyle.UTILITY);

		Optional<String> result = dialog.showAndWait();
		return result.map(Integer::parseInt).orElse(-1);
	}

	/**
	 * Demande une question oui/non à l'utilisateur
	 * @param message question
	 * @return réponse
	 */
	public static boolean demander(String message)
	{
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Attention");
		alert.setHeaderText(message);
		alert.setContentText("Êtes-vous sûr?");
		alert.initStyle(StageStyle.UTILITY);

		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

		Optional<ButtonType> result = alert.showAndWait();
		return result.isPresent() && result.get() == ButtonType.YES;
	}

	/**
	 * Affiche les noms des joueurs gagnants
	 * @param gagnants joueurs gagnants
	 */
	public static void afficherNoms(List<Joueur> gagnants)
	{
		StringBuilder s = new StringBuilder();

		for (Joueur j : gagnants)
		{
			s.append("Joueur ").append(j.getId() + 1).append("\n");
		}

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Partie terminée");
		alert.setHeaderText("Les gagnants:");
		alert.setContentText(s.toString());

		alert.showAndWait();
	}
}

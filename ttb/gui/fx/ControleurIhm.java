package ttb.gui.fx;

import ttb.gui.fx.util.Action;
import ttb.metier.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Classe ControleurIhm
 * Controleur de la version GUI JavaFX
 *
 * @author Jérémy Auzou
 * @author Matys Achart
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */

public class ControleurIhm
{
	private Action source;

	private Plateau metier;
	private Ihm     ihm;
	private int     coupsDuTour;
	private boolean modeScenario;
	private boolean robot1;
	private boolean robot2;
	private boolean victoireAck;

	public ControleurIhm(int nbJoueurs, Ihm ihm)
	{
		this.coupsDuTour = 0;
		this.metier = SetGrille.initGrille(nbJoueurs);
		this.ihm = ihm;
		this.robot1 = this.robot2 = this.victoireAck = false;
	}

	public ControleurIhm(int nbJoueurs, Ihm ihm, int numScenario)
	{
		this(nbJoueurs, ihm);
		this.numScenario = numScenario;
		this.modeScenario = true;
		scenarioInit();
	}

	public void acknowlegeVictoire()
	{
		this.victoireAck = true;
	}

	// Méthodes pour jouer
	/**
	 * Obtient le nombre de joueurs
	 */
	public int getNbJoueurs() {	return this.metier.getNbJoueurs(); }

	/**
	 * Obtient un joueur en fonction de l'indice
	 * @param i indice du joueur
	 * @return Joueur à cet indice
	 */
	public Joueur getJoueur(int i) { return this.metier.getJoueur(i); }

	/**
	 * Obtient le joueur courant
	 * @return le joueur courant
	 */
	public Joueur getJoueurCourant() { return this.metier.getJoueurCourant(); }

	/**
	 * Passe au joueur suivant
	 */
	public void changerJoueur()
	{
		if (this.modeScenario) return;

		if (premierTour())
		{
			if (robot1 && robot2)
			{
				robot1 = robot2 = false;
			}
			else return;
		}

		executerOrdres(
				this.metier.getJoueurCourant().getOrdres(0),
				this.metier.getJoueurCourant().getRobot(0)
		);
		executerOrdres(
				this.metier.getJoueurCourant().getOrdres(1),
				this.metier.getJoueurCourant().getRobot(1)
		);
		this.setSource(null);
		this.metier.changerJoueur();
		this.coupsDuTour = 0;
	}

	/**
	 * Obtient si la partie est terminée
	 * @return vrai si la partie est terminée
	 */
	public boolean partieTerminee()
	{
		return metier.estPartieFinie();
	}

	/**
	 * Retourne la liste de gagnants.
	 * @return la liste de gagnants.
	 */
	public List<Joueur> getGagnants()
	{
		return metier.getGagnant();
	}

	/**
	 * @return vrai si appelée lors du premier tour
	 */
	private boolean premierTour()
	{
		return metier.getNbTours() < metier.getNbJoueurs();
	}

	/**
	 * Obtient le robot au coordonnées indiquées
	 * @param coords coordonnées du robot
	 * @return Robot si existant, null sinon
	 */
	public Robot getRobotAPosition(int[] coords) { return this.metier.getRobotAPosition(coords); }

	/**
	 * Obtient le joueur auquel appartient la base indiquée par les coordonnées fournies
	 * @param coords coordonnées de la base
	 * @return Joueur si les coordonnées pointent sur une base, null sinon
	 */
	public Joueur getJoueurParBase(int[] coords) { return this.metier.getJoueurParBase(coords); }

	/**
	 * Obtient le tableau des tuiles
	 * @return le tableau des tuiles
	 */
	public Tuile[][] getTuiles() { return this.metier.getTuiles(); }

	/**
	 * Obtient la file d'attente de cristaux
	 * @return un String représentant la file d'attente de cristaux
	 */
	public String getFileAttente() { return this.metier.getFileAttente(); }

	/**
	 * Définit la source de l'action pendant un tour
	 * @param a source de l'action
	 */
	public void setSource(Action a)
	{
		if (this.modeScenario) return;
		this.source = a;
		this.ihm.afficherPlateau();
	}

	/**
	 * Obtient la source de l'action
	 * @return la source de l'action
	 */
	public Action getSource()
	{
		return this.source;
	}

	/**
	 * Supprime l'ordre d'un robot
	 * @param robot indice du robot
	 * @param indice indice de l'ordre
	 */
	public void supprimerOrdre(int robot, int indice)
	{
		if (this.modeScenario) return;
		if (premierTour())     return;
		if (peutJouer())
		{
			this.metier.getJoueurCourant().enleverOrdre(robot, indice);
			this.ihm.afficherPlateau();
			this.coupsDuTour++;
		}
	}

	/**
	 * Ajoute un ordre à un robot
	 * @param robot indice du robot
	 * @param indice indice de l'ordre
	 */
	public void ajouterOrdre(int robot, int indice)
	{
		if (this.modeScenario) return;
		if (peutJouer())
		{
			if (premierTour())
			{
				if (robot == 0)
				{
					if (!robot1)
					{
						this.robot1 = true;
						this.metier.getJoueurCourant().ajouterOrdre(robot, indice, source.getAction());
						this.setSource(null);
						this.ihm.afficherPlateau();
						this.coupsDuTour++;
					}
				}
				else if (robot == 1)
				{
					if (!robot2)
					{
						this.robot2 = true;
						this.metier.getJoueurCourant().ajouterOrdre(robot, indice, source.getAction());
						this.setSource(null);
						this.ihm.afficherPlateau();
						this.coupsDuTour++;
					}
				}
			}
			else
			{
				if (this.source.getRobot() > -1 && this.source.getRobot() == robot)
				{
					this.metier.getJoueurCourant().permuterOrdre(robot, this.source.getIndice(), indice);
				} else
				{
					this.metier.getJoueurCourant().ajouterOrdre(robot, indice, source.getAction());
				}
				this.setSource(null);
				this.ihm.afficherPlateau();
				this.coupsDuTour++;
			}
		}
	}

	/**
	 * Réinitalise les ordres d'un robot
	 * @param i indice du robot
	 */
	public void resetRobot(int i)
	{
		if (this.modeScenario) return;
		if (peutJouer())
		{
			this.metier.getJoueurCourant().resetOrdres(i);
			this.ihm.afficherPlateau();
			this.coupsDuTour++;
		}
	}

	/**
	 * Retourne vrai si le joueur peut jouer
	 * @return vrai si le joueur peut jouer
	 */
	private boolean peutJouer()
	{
		if (premierTour())
			return this.coupsDuTour < 2;
		else
			return this.coupsDuTour < 1;
	}

	/**
	 * Retourne vrai si tous les cristaux de la file d'attente ont été déposés
	 * @return
	 */
	public boolean derniersTours()
	{
		return this.metier.getFileAttente().isEmpty();
	}

	/**
	 * Obtient le nombre de tours restants
	 * @return le nombre de tours restants
	 */
	public int toursRestants()
	{
		return this.metier.getDecompte();
	}

	/**
	 * Exécute les ordres d'un robot
	 * @param ordres Tableau d'ordres à exécuter
	 * @param r Robot sur lequel exécuter les ordres
	 */
	public void executerOrdres(char[] ordres, Robot r)
	{
		for (char ordre : ordres)
		{
			switch (ordre)
			{
				case 'A':
					metier.avancer(r, true);
					break;
				case 'D':
					r.turnAround(false);
					break;
				case 'G':
					r.turnAround(true);
					break;
				case 'C':
					metier.chargerCristal(r);
					break;
				case 'E':
					metier.deposerCristal(r);
					break;
				case 'S':
					metier.avancer(r, true);
					metier.avancer(r, true);
					break;
			}
		}
	}

	// Méthodes Scénario

	private Scanner sc;
	private int     numScenario;
	private int     ligne = 0;
	String dernierCommentaire = "";

	/**
	 * @return vrai si ce Controleur gère un scénario
	 */
	public boolean isModeScenario()
	{
		return this.modeScenario;
	}

	/**
	 * Initialise un scénario.
	 */
	public void scenarioInit()
	{
		this.victoireAck = false;
		this.coupsDuTour = 0;
		this.metier = SetGrille.initGrille(metier.getNbJoueurs());
		try
		{
			this.sc = new Scanner(new File(
					"./ttb/scenarios/scenario" + metier.getNbJoueurs() + "-" + numScenario + ".data"), "utf8");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Passe a l'étape suivante d'un scénario
	 * @return le commentaire le plus récent du fichier de scénario
	 */
	public String scenarioSuivant()
	{
		if (sc.hasNext())
		{
			ligne++;
			String line = sc.nextLine();
			char[] splittedLine = line.toCharArray();
			if (splittedLine[0] == 'R')
			{

				int playerID = Character.getNumericValue(splittedLine[1]);
				int robotID  = Character.getNumericValue(splittedLine[2]);

				// On exécute les ordres directs du robot.
				executerOrdres(Arrays.copyOfRange(splittedLine, 3, splittedLine.length),
						metier.getJoueur(playerID).getRobot(robotID));
				return "";
			}
			else if (splittedLine[0] == 'J') // exécute les ordres selon la main du joueur.
			{
				Joueur j = metier.getJoueur(Character.getNumericValue(splittedLine[1]));
				metier.setJoueurCourant(Character.getNumericValue(splittedLine[1]));
				int idRobot;
				switch (splittedLine[2])
				{
					case 'A':
						idRobot = Character.getNumericValue(splittedLine[3]);
						char lettre = splittedLine[4];
						int  index  = Character.getNumericValue(splittedLine[5]);
						j.ajouterOrdre(idRobot, index, lettre);
						break;
					case 'E':
						idRobot = Character.getNumericValue(splittedLine[3]);
						int indice = Character.getNumericValue(splittedLine[4]);
						j.enleverOrdre(idRobot, indice);
						break;
					case 'P':
						idRobot = Character.getNumericValue(splittedLine[3]);
						int indice1 = Character.getNumericValue(splittedLine[4]);
						int indice2 = Character.getNumericValue(splittedLine[5]);
						j.permuterOrdre(idRobot, indice1, indice2);
						break;
					case 'R':
						idRobot = Character.getNumericValue(splittedLine[3]);
						j.resetOrdres(idRobot);
						break;
					case 'F' :
						for(int i = 0; i < j.getRobots().length; i++)
							executerOrdres(j.getOrdres(i), j.getRobot(i));
				}
				return "";
			}
			else
			{
				ligne--;
				dernierCommentaire = line + '\n' + scenarioSuivant();
				return dernierCommentaire;
			}
		}
		return "// Fin de fichier";
	}

	/**
	 * Passe a l'étape précédente d'un scénario
	 * @return le commentaire le plus récent d'un scénario
	 */
	public String scenarioPrecedent()
	{
		scenarioInit();
		int lignes = ligne;
		String retour = "";
		ligne = 0;
		for (int i = 0; i < lignes - 1; i++)
			retour = scenarioSuivant();
		return retour;
	}

	/**
	 * @return le dernier commentaire du scénario courant
	 */
	public String getDernierCommentaire() { return this.dernierCommentaire; }

	/**
	 * @return vrai si la victoire a été prise en compte
	 */
	public boolean getVictoireAck()
	{
		return victoireAck;
	}
}

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
 * Controleur de la version GUI
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
	public int getNbJoueurs() {	return this.metier.getNbJoueurs(); }
	public Joueur getJoueur(int i) { return this.metier.getJoueur(i); }
	public Joueur getJoueurCourant() { return this.metier.getJoueurCourant(); }
	public void changerJoueur()
	{
		if (this.modeScenario) return;

		if (premierTour())
		{
			// TODO PREMIER TOUR
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

	public boolean partieTerminee()
	{
		return metier.estPartieFinie();
	}

	public List<Joueur> getGagnants()
	{
		return metier.getGagnant();
	}

	private boolean premierTour()
	{
		return metier.getNbTours() < metier.getNbJoueurs();
	}

	public Robot getRobotAPosition(int[] coords) { return this.metier.getRobotAPosition(coords); }
	public Joueur getJoueurParBase(int[] coords) { return this.metier.getJoueurParBase(coords); }
	public Tuile[][] getTuiles() { return this.metier.getTuiles(); }
	public String getFileAttente() { return this.metier.getFileAttente(); }

	public void setSource(Action a)
	{
		if (this.modeScenario) return;
		this.source = a;
		this.ihm.afficherPlateau();
	}

	public Action getSource()
	{
		return this.source;
	}

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

	public void ajouterOrdre(int robot, int indice)
	{
		if (this.modeScenario) return;
		if (peutJouer())
		{
			if (premierTour())
			{
				System.out.println("premier tour");
				if (robot == 0)
				{
					System.out.println("robot 1");
					if (robot1)
					{
						System.out.println("deja ajouté");
						return;
					}
					else
					{
						this.robot1 = true;
						System.out.println("ajout");
						this.metier.getJoueurCourant().ajouterOrdre(robot, indice, source.getAction());
						this.setSource(null);
						this.ihm.afficherPlateau();
						this.coupsDuTour++;
					}
				}
				else if (robot == 1)
				{
					System.out.println("robot 2");
					if (robot2)
					{
						System.out.println("deja ajouté");
						return;
					}
					else
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

	private boolean peutJouer()
	{
		if (premierTour())
			return this.coupsDuTour < 2;
		else
			return this.coupsDuTour < 1;
	}

	public boolean derniersTours()
	{
		return this.metier.getFileAttente().isEmpty();
	}

	public int toursRestants()
	{
		return this.metier.getDecompte();
	}

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

	public boolean isModeScenario()
	{
		return this.modeScenario;
	}

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
				// C'est un commentaire
				// System.out.println(line);
				ligne--;
				dernierCommentaire = line + '\n' + scenarioSuivant();
				return dernierCommentaire;
			}
		}
		return "// Fin de fichier";
	}

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

	public String getDernierCommentaire() { return this.dernierCommentaire; }

	public boolean getVictoireAck()
	{
		return victoireAck;
	}
}

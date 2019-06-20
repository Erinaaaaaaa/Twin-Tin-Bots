package ttb.gui.fx;

import ttb.gui.fx.util.Action;
import ttb.metier.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ControleurIhm
{
	private Action source;

	private Plateau metier;
	private Ihm     ihm;
	private int     coupsDuTour;

	public ControleurIhm(int nbJoueurs, Ihm ihm)
	{
		this.coupsDuTour = 0;
		this.metier = SetGrille.initGrille(nbJoueurs);
		this.ihm = ihm;
	}

	public ControleurIhm(int nbJoueurs, Ihm ihm, int numScenario)
	{
		this(nbJoueurs, ihm);
		this.numScenario = numScenario;
		scenarioInit();
	}

	// Méthodes pour jouer
	public int getNbJoueurs() {	return this.metier.getNbJoueurs(); }
	public Joueur getJoueur(int i) { return this.metier.getJoueur(i); }
	public Joueur getJoueurCourant() { return this.metier.getJoueurCourant(); }
	public void avancer(Robot robot, boolean b) { this.metier.avancer(robot, b); }
	public void chargerCristal(Robot robot) { this.metier.chargerCristal(robot); }
	public void deposerCristal(Robot robot) { this.metier.deposerCristal(robot); }
	public void changerJoueur()
	{

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
	public Robot getRobotAPosition(int[] coords) { return this.metier.getRobotAPosition(coords); }
	public Joueur getJoueurParBase(int[] coords) { return this.metier.getJoueurParBase(coords); }
	public Tuile[][] getTuiles() { return this.metier.getTuiles(); }
	public String getFileAttente() { return this.metier.getFileAttente(); }

	public void setSource(Action a)
	{
		this.source = a;
		this.ihm.afficherPlateau();
	}

	public Action getSource()
	{
		return this.source;
	}

	public void supprimerOrdre(int robot, int indice)
	{
		if (!actionEffectuee())
		{
			this.metier.getJoueurCourant().enleverOrdre(robot, indice);
			this.ihm.afficherPlateau();
			this.coupsDuTour++;
		}
	}

	public void ajouterOrdre(int robot, int indice)
	{
		if (!actionEffectuee())
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

	public void resetRobot(int i)
	{
		if (!actionEffectuee())
		{
			this.metier.getJoueurCourant().resetOrdres(i);
			this.ihm.afficherPlateau();
			this.coupsDuTour++;
		}
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private boolean actionEffectuee()
	{
		if (metier.getNbTours() < metier.getNbJoueurs())
			return this.coupsDuTour >= 2;
		else
			return this.coupsDuTour >= 1;
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

	public void scenarioInit()
	{
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

	public void scenarioSuivant()
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
			}
			else if (splittedLine[0] == 'J') // exécute les ordres selon la main du joueur.
			{
				Joueur j = metier.getJoueur(Character.getNumericValue(splittedLine[1]));
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
			}
			else
			{
				// C'est un commentaire
				System.out.println(line);
				ligne--;
				scenarioSuivant();
			}
		}
	}

	public void scenarioPrecedent()
	{
		scenarioInit();
		int lignes = ligne;
		ligne = 0;
		for (int i = 0; i < lignes - 1; i++)
			scenarioSuivant();
	}
}

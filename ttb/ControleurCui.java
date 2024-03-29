package ttb;

import ttb.metier.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

import iut.algo.CouleurConsole;
import iut.algo.Console;

/**
 * Classe ControleurCui
 * @author Jérémy AUZOU
 * @author Matys ACHART
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */

public class ControleurCui
{
	private Plateau metier;
	private IhmCui  ihm;

	public ControleurCui(int nbJoueurs)
	{
		metier = SetGrille.initGrille(nbJoueurs);
		ihm    = new IhmCui(this);
	}

	/**
	 * Permet d'exécuter l'action du joueur actuel (Ajouter ordre, permuter, enlever,...)
	 * @param j joueur actuel
	 * @param c Action choisie
	 * @param idRobot numero du robot subissant l'action
	 */
	private void actionJoueur(Joueur j, char c, int idRobot)
	{
		if(c == 'N')
			return;
		switch(c)
		{
			case 'P' :
				int[] ind = new int[] {ihm.getInd(j.getOrdres(idRobot)), ihm.getInd(j.getOrdres(idRobot))};
				j.permuterOrdre(idRobot, ind[0], ind[1]);
				break;
			case 'A' :
				j.ajouterOrdre(idRobot, ihm.getInd(j.getOrdres(idRobot)), ihm.getCarte());
				break;
			case 'E' :
				j.enleverOrdre(idRobot, ihm.getInd(j.getOrdres(idRobot)));
				break;
			case 'R' :
				j.resetOrdres(idRobot);
				break;
		}
	}

	/**
	 * Déroulement de la partie.
	 */
	public void jouer()
	{
		do
		{
			Joueur joueur = metier.getJoueurCourant();
			String action;
			this.afficherJoueur();
			ihm.afficherPlateau();
			if(metier.getNbTours() < metier.getNbJoueurs())
			{
				ihm.afficherString("Il s'agit du premier tour, choisissez une carte à ajouter à vos robots");
				for(int i = 0; i < 2; i++)
				{
					ihm.afficherString(((i == 1)?"Grand":"Petit") + " robot");
					actionJoueur(joueur, 'A', i);
				}
			}
			else
			{
				int idRobot = ihm.getRobot(joueur);
				do
					action = ihm.getAction();
				while (!action.matches("[APERN]")); // actions possibles
				actionJoueur(joueur, action.charAt(0), idRobot);
			}
			int i = 0;
			do
			{
				Robot r = joueur.getRobot(i);
				char[] ordres = joueur.getOrdres(i);
				executerOrdres(ordres, r);
				i++;
			}while(i < 2);

			ihm.afficherPlateau();
			metier.changerJoueur();
			if (metier.getFileAttente().equals(""))
				ihm.afficherString("Pion décompte : " + metier.getDecompte());
		}while(!metier.estPartieFinie());

		ihm.finPartie(metier.getGagnant());
	}

	/**
	 * Méthode pour utiliser le mode debug. Les instructions à exécuter seront lues
	 * dans le fichier "scenario.data". <br />
	 * La premiere lettre doit être soit J pour joueur ou R pour robot.<br />
	 * <br />
	 * <ul>
	 * <b>Si c'est un robot:</b>
	 * <li>La suivante indique l'indice du joueur.</li>
	 * <li>La suivante celui du robot.</li>
	 * <li>Ensuite, toutes les actions à effectuer.</li>
	 * </ul>
	 * <br>
	 *
	 * Si c'est un joueur, la lettre suivante est son indice.<br>
	 * Ensuite l'action à effectuer.<br>
	 * <ul>
	 * <b>En fonction de l'action.</b>
	 * <li>Si action A : on a une lettre et un indice.</li>
	 * <li>Si action E : 1 indice.</li>
	 * <li>Si action P : 2 indices suivent.</li>
	 * <li>Si action R : rien.</li>
	 * <li>Si action F : executer les ordres de ses robots</li>
	 * </ul>
	 * <br>
	 * @param nbJoueurs nombres de joueurs
	 * @param ligne     ligne a laquelle commencer/reprendre le test
	 * @param numScenar numero du scenario
	 */
	public void debug(int nbJoueurs, int ligne, int numScenar) {
		Scanner sc   = null;
		Scanner rep  = null;
		String choix = "s";
		int cpt      = 0;

		try
		{
			sc = new Scanner(new File(
				"./ttb/scenarios/scenario" + nbJoueurs + "-" + numScenar + ".data"), "utf8");
			rep = new Scanner(System.in);
			if(ligne == 0) // Affichage du plateau au début.
				ihm.afficherPlateau();
			while (sc.hasNext() && choix.equals("s"))
			{
				choix = "";
				char[] splittedLine = sc.nextLine().toCharArray();
				if (splittedLine[0] == 'R')
				{
					if(cpt >= ligne)
						ihm.afficherPlateau();
					int playerID = Character.getNumericValue(splittedLine[1]);
					int robotID  = Character.getNumericValue(splittedLine[2]);

					// On exécute les ordres directs du robot.
					executerOrdres(Arrays.copyOfRange(splittedLine, 3, splittedLine.length),
					               metier.getJoueur(playerID).getRobot(robotID));
					if(cpt >= ligne)
						ihm.afficherPlateau();
				}
				else if (splittedLine[0] == 'J') // exécute les ordres selon la main du joueur.
				{
					if(cpt >= ligne)
						ihm.afficherPlateau();
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
					if(cpt >= ligne)
					{
						this.afficherJoueur();
						ihm.afficherPlateau();
					}
				}
				else if(cpt >= ligne) // Affiche les commentaires du fichier.
					ihm.afficherString(new String(splittedLine));

				while(!choix.matches("[psq]") && cpt >= ligne &&
				     (splittedLine[0] == 'R' || splittedLine[0] == 'J'))
				{
					if (metier.estPartieFinie())
					{
						ihm.finPartie(metier.getGagnant());
						return;
					}
					ihm.controlesScenario();
					choix = rep.next().toLowerCase();
				}
				if(cpt < ligne || (splittedLine[0] != 'R' && splittedLine[0] != 'J'))
					choix = "s";

				if(splittedLine[0] == 'R' || splittedLine[0] == 'J')
					cpt++;
			}

			// Remise à zéro du plateau puis exécution de toutes les actions sauf celle-ci.
			if(choix.equals("p"))
			{
				this.metier = SetGrille.initGrille(nbJoueurs);
				debug(nbJoueurs, cpt-2, numScenar);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Le fichier scenario" + nbJoueurs +
			                   "-" + numScenar + ".data n'existe pas.");
		}
	}
	
	/**
	 * Transmet les informations permettant d'afficher le statut des joueurs.
	 */
	public void afficherJoueur()
	{
		Joueur joueurCourant = metier.getJoueurCourant();

		for(int i = 0; i < metier.getNbJoueurs(); i++)
		{
			if(metier.getJoueur(i) == joueurCourant)
				ihm.afficher(metier.getJoueur(i).toString(), this.converCouleur(joueurCourant.getCouleur()));
			else
				ihm.afficher(metier.getJoueur(i).toString(),null);
		}
	}

	/**
	 * Exécute le programme du robot.
	 * A correspond à Avancer
	 * D correspond à Tourner à droite
	 * G correspond à Tourner à gauche
	 * C correspond à Charger un cristal
	 * E correspond à Décharger un cristal
	 * S correspond à avancer 2 fois
	 * @param ordres les ordres à exécuter
	 * @param r le robot pour lequel exécuter les ordres placés.
	 */
	public void executerOrdres(char[] ordres, Robot r)
	{
		for(int i = 0; i < ordres.length; i++)
		{
			switch(ordres[i])
			{
				case 'A' :
					metier.avancer(r, true);
					break;
				case 'D' :
					r.turnAround(false);
					break;
				case 'G' :
					r.turnAround(true);
					break;
				case 'C' :
					metier.chargerCristal(r);
					break;
				case 'E' :
					metier.deposerCristal(r);
					break;
				case 'S' :
					metier.avancer(r, true);
					metier.avancer(r, true);
					break;
			}
		}
	}

	public Tuile[][] getPlateau()          { return metier.getTuiles(); }
	public String    getAffichagePlateau() { return metier.toString();  }

	public int getNbLigne()  {return metier.getNbLigne();}
	public int getNbColonne(){return metier.getNbColonne();}
	
	public String getSymbole(int lig, int col){return metier.getSymbole(lig,col);}

	public CouleurConsole getCouleur(int lig, int col)
	{
		String couleur = metier.getCouleur(lig,col);
		return this.converCouleur(couleur);
	}
	public CouleurConsole converCouleur(String couleur)
	{
		switch(couleur)
		{
			case "Vert"  : return CouleurConsole.VERT;
			case "Rouge" : return CouleurConsole.ROUGE;
			case "Jaune" : return CouleurConsole.JAUNE;
			case "Bleu"  : return CouleurConsole.BLEU;
			case "Violet": return CouleurConsole.MAUVE;
			case "Rose"  : return CouleurConsole.CYAN;
			case "Cyan"  : return CouleurConsole.CYAN;
		}
		return CouleurConsole.NOIR;

	}

	public static void main(String[] args)
	{
		System.out.println("Combien de joueurs ? [2-6]");
		Scanner sc = new Scanner(System.in);
		String nbJoueurs;
		int nbJ;
		do
			nbJoueurs = sc.next();
		while (!nbJoueurs.matches("[2-6]"));
		nbJ = Integer.parseInt(nbJoueurs);
		if(args.length > 0 && args[0].equals("SCENARIO"))
		{
			System.out.println("Numéro du scénario : ");
			String scen;
			do
				scen = sc.next();
			while (!scen.matches("[0-9]+"));
			new ControleurCui(nbJ).debug(nbJ, 0, Integer.parseInt(scen));
		}
		else
			new ControleurCui(nbJ).jouer();
		sc.close();
	}
}

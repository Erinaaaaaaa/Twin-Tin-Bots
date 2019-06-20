package ttb;

import ttb.metier.*;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

import iut.algo.CouleurConsole;
import iut.algo.Console;

public class ControleurCui
{
	private Plateau metier;
	private IhmCui  ihm;

	public ControleurCui(int nbJoueurs)
	{
		metier = SetGrille.initGrille(nbJoueurs);
		ihm    = new IhmCui(this);
	}

	private void actionJoueur(Joueur j, char c)
	{
		if(c == 'N')
			return;
		int idRobot = ihm.getRobot(j);
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

	public void jouer()
	{
		do
		{
			Joueur joueur = metier.getJoueurCourant();
			String action;
			ihm.afficher(joueur);
			do
				action = ihm.getAction();
			while (!action.matches("[APERN]")); // actions possibles
			actionJoueur(joueur, action.charAt(0));
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
		}while(true);
	}

	/**
	 * Méthode pour utiliser le mode debug. Les instructions à exécuter seront lues dans le fichier "scenario.data". <br />
	 * La premiere lettre doit être soit J pour joueur ou R pour robot.<br />
	 * <br />
	 * <ul><b>Si c'est un robot:</b>
	 * 	 <li>La suivante indique l'indice du joueur.</li>
	 * 	 <li>La suivante celui du robot</li>
	 * 	 <li>Ensuite, toutes les actions à effectuer.</li>
	 * </ul><br>
	 *
	 * Si c'est un joueur, la lettre suivante est son indice.<br>
	 * Ensuite l'action à effectuer.<br>
	 * <ul><b>En fontion de l'action.</b>
	 *   <li>Si action A : on a une lettre et un indice.</li>
	 *   <li>Si action E : 1 indice.</li>
	 *   <li>Si action P : 2 indices suivent.</li>
	 *   <li>Si action R : rien.</li>
	 * </ul>
	 * <br>
	 * Si action robot, on exécute que sur le robot.<br>
	 * Si action joueur, on exécute sur ses 2 robots.<br>
	 */
	public void debug(int nbJoueurs) {
		Scanner sc = null;

		try {
			sc = new Scanner(new File("./ttb/scenario.data"), "utf8");
			for(int i = 0; i < nbJoueurs; i++)
				sc.nextLine();

			Scanner line = new Scanner(new String(sc.nextLine()));
			line.useDelimiter("\\|");
			System.out.println("2");
			while (line.hasNext()) {
				char[] splittedLine = line.next().toCharArray();
				if (splittedLine[0] == 'R') {
					int playerID = Character.getNumericValue(splittedLine[1]);
					int robotID  = Character.getNumericValue(splittedLine[2]);

					executerOrdres(Arrays.copyOfRange(splittedLine, 3, splittedLine.length),
					               metier.getJoueur(playerID).getRobot(robotID));
					ihm.afficherPlateau();
				}
				else if (splittedLine[0] == 'J') {
					Joueur j = metier.getJoueur(Character.getNumericValue(splittedLine[1]));
					int idRobot;
					switch (splittedLine[2]) {
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
							int ind = Character.getNumericValue(splittedLine[4]);
							j.resetOrdres(idRobot);
							break;
						case 'F' :
							for(int i = 0; i < j.getRobots().length; i++)
								executerOrdres(j.getOrdres(i), j.getRobot(i));
					}
					ihm.afficher(j);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
			new ControleurCui(nbJ).debug(nbJ);
		else
			new ControleurCui(nbJ).jouer();
		sc.close();
	}

	public Tuile[][] getPlateau()          { return metier.getTuiles(); }
	public String    getAffichagePlateau() { return metier.toString();  }

	public int getNbLigne(){return metier.getNbLigne();}
	public int getNbColonne(){return metier.getNbColonne();}
	public String getSymbole(int lig, int col){return metier.getSymbole(lig,col);}

	public CouleurConsole getCouleur(int lig, int col)
	{
		String couleur = metier.getCouleur(lig,col);
		switch(couleur)
		{
			case "Vert"  :return CouleurConsole.VERT;
			case "Rouge" :return CouleurConsole.ROUGE;
			case "Jaune" :return CouleurConsole.JAUNE;
			case "Bleu"  :return CouleurConsole.BLEU;
			case "Violet":return CouleurConsole.MAUVE;
			case "Rose"  :return CouleurConsole.CYAN;
			case "Cyan"  :return CouleurConsole.CYAN;
		}
		return CouleurConsole.NOIR;
	}
}

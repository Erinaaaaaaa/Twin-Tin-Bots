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
		switch(c)
		{
			case 'P' :
				int[] ind = new int[] {ihm.getInd(j.getOrdres()), ihm.getInd(j.getOrdres())};
				break;
			case 'A' :
				j.ajouterOrdre(ihm.getInd(j.getOrdres()), ihm.getCarte());
				break;
			case 'E' :
				j.enleverOrdre(ihm.getInd(j.getOrdres()));
				break;
			case 'R' :
				j.resetOrdres(ihm.getInd(j.getOrdres()));
				break;
		}
	}

	public void jouer()
	{
		do
		{
			Joueur joueur = metier.getJoueurCourant();
			ihm.afficher();
			actionJoueur(joueur, ihm.getAction().charAt(0));
			int i = 0;
			do
			{
				Robot r = joueur.getRobot(i);
				char[] ordresTmp = joueur.getOrdres();
				int ind = 0;
				char[] ordres = new char[3];
				for(int j = 3*i; j < 3*(i+1); j++)
				{
					ordres[ind] = ordresTmp[j];
					ind++;
				}
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
	public void debug() {
		Scanner sc = null;

		try {
			sc = new Scanner(new File("./ttb/scenario.data"), "utf8");
			for(int i = 0; i < nbJoueurs; i++)
				sc.nextLine();

			Scanner line = new Scanner(sc.nextLine());
			line.setDelimiter("|")
			while (line.hasNext()) {
				char[] splittedLine = line.next().toCharArray();
				if (splittedLine[0] == 'R') {
					int playerID = Integer.parseInt(String.valueOf(splittedLine[1]));
					int robotID  = Integer.parseInt(String.valueOf(splittedLine[2]));

					executerOrdres(Arrays.copyOfRange(splittedLine, 3, splittedLine.length-1), metier.getJoueur(playerID).getRobot(robotID));
				}
				else if (splittedLine[0] == 'J') {
					Joueur j = metier.getJoueur( Integer.parseInt(String.valueOf(splittedLine[1])) );
					switch (splittedLine[2]) {
						case 'A':
							char lettre = splittedLine[3];
							int  index  = Integer.parseInt(String.valueOf(splittedLine[4]));
							j.ajouterOrdre(index, lettre);
							break;
						case 'E':
							int indice = Integer.parseInt(String.valueOf(splittedLine[3]));
							j.enleverOrdre(indice);
							break;
						case 'P':
							int indice1 = Integer.parseInt(String.valueOf(splittedLine[3]));
							int indice2 = Integer.parseInt(String.valueOf(splittedLine[4]));
							j.permuterOrdre(indice1, indice2)
							break;
						case 'R':
							break;
					}
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
		do
			nbJoueurs = sc.next();
		while (!nbJoueurs.matches("[2-6]"));
		if(args.length > 0 && args[0].equals("SCENARIO"))
			new ControleurCui(Integer.parseInt(nbJoueurs)).debug();
		else
			new ControleurCui(Integer.parseInt(nbJoueurs)).jouer();
		sc.close();
	}

	public Tuile[][] getPlateau()          { return metier.getTuiles(); }
	public String    getAffichagePlateau() { return metier.toString();  }
	public String    getInfosJoueur()
	{
		Joueur j = metier.getJoueurCourant();
		String retour = "Joueur " + (j.getId() + 1) + " : \n";
		retour += "\tOrdres : ";
		char[] ordres = j.getOrdres();
		for(int i = 0; i < ordres.length; i++)
		{
			if(i == 3)
				retour += ": ";
			retour += "[";
			if(ordres[i] == '\0')
				retour += " ";
			else
				retour += ordres[i];

			retour += "] ";
		}

		retour += "\n\tMain : ";
		for(Character c : j.getMain())
			retour += c + ",";

		return retour.substring(0, retour.length() - 1) + "\n";
	}


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

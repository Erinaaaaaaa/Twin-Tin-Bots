package ttb;

import ttb.metier.*;

import java.io.File;
import java.util.Scanner;

public class ControleurCui
{
	private Plateau metier;
	private IhmCui  ihm;

	public ControleurCui(int nbJoueurs)
	{
		metier = SetGrille.initGrille(nbJoueurs);
		ihm    = new IhmCui(this);
	}

	private void actionJoueur(Joueur j)
	{
		String action = ihm.getAction();
		switch(action.charAt(0))
		{
			case 'P' :
				int[] ind = new int[] {ihm.getInd(j.getOrdres()), ihm.getInd(j.getOrdres())};
				j.permuterOrdre(ind[0], ind[1]);
				break;
			case 'A' :
				j.ajouterOrdre(ihm.getInd(j.getOrdres()), ihm.getCarte());
				break;
			case 'E' :
				j.enleverOrdre(ihm.getInd(j.getOrdres()));
				break;
			case 'R' :
				j.resetOrdres();
				break;
		}
	}

	public void jouer()
	{
		do
		{
			Joueur joueur = metier.getJoueurCourant();
			ihm.afficher();
			actionJoueur(joueur);
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
			System.out.println(metier.toString());
			metier.changerJoueur();
		}while(metier.getJoueurCourant().getId() != 0);
	}

	/**
	 * Methode pour utiliser le mode debug. La ligne a executer Les instructions à executer seront lues dans le fichier "scenario.data".
	 */
	public void debug() {
		Scanner sc    = null;
		String[] line = null;
		char[] ordres = null;
		int robotID = 0;
		int nbJoueurs = metier.getNbJoueurs();
		try {
			sc = new Scanner(new File("./ttb/scenario.data"), "utf8");
			line = sc.nextLine().split(";");
			ordres = new char[line.length];
			for (int i = 0; i < line.length; i++) {
				ordres[i] = line[i].charAt(0);
			}

			executerOrdres(ordres, metier.getJoueurCourant().getRobot(robotID));
			robotID++;
			if(robotID > 1) {
				robotID=0;
				metier.changerJoueur();
			}


		} catch (Exception e) { e.printStackTrace(); }
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
		System.out.println("Combien de joueurs ? ");
		Scanner sc = new Scanner(System.in);
		if(args.length > 0 && args[0].equals("SCENARIO"))
			new ControleurCui(sc.nextInt()).debug();
		else
			new ControleurCui(sc.nextInt()).jouer();
		sc.close();
	}

	public Tuile[][] getPlateau() { return metier.getTuiles(); }
	public String getAffichagePlateau() { return metier.toString(); }
	public String getInfosJoueur()
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
}

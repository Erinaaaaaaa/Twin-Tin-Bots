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
		jouer();
	}

	private void actionJoueur(Joueur j)
	{
		String action = ihm.getAction();
		switch(action.charAt(0))
		{
			case 'P' :
				int[] ind = new int[] {ihm.getInd(j.getOrdres(), ihm.getInd(j.getOrdres()};
				j.permuterOrdre(ind[0], ind[1]);
				break;
			case 'A' :
				j.ajouterOrdre(ihm.getInd(j.getOrdres(), ihm.getAction().charAt(0));
				break;
			case 'E' :
				j.enleverOrdre(ihm.getInd(j.getOrdres());
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
			actionJoueur(joueur);
			int i = 0;
			do
			{
				Robot r = joueur.getRobot(i);
				char[] ordresTmp = joueur.getOrdres();
				int ind = 0;
				for(int j = 3*i; j < 3*(i+1); j++)
				{
					ordres[ind] = ordresTmp[j];
					ind++;
				}
				executerOrdres(ordres, r);
			}while(i < 2);
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
			sc = new Scanner(new File("./ttb/niveau.data"), "utf8");
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

	public void executerOrdres(char[] ordres, Robot rActuel)
	{
		for(int i = 0; i < ordres.length; i++)
		{
			switch(ordres[i])
			{
				case 'A' :
					metier.avancer(rActuel, true);
					break;
				case 'D' :
					rActuel.turnAround(false);
					break;
				case 'G' :
					rActuel.turnAround(true);
					break;
				case 'C' :
					metier.chargerCristal(rActuel);
					break;
				case 'E' :
					metier.deposerCristal(rActuel);
					break;
				case 'S' :
					metier.avancer(rActuel, true);
					metier.avancer(rActuel, true);
					break;
			}
		}
	}

	public static void main(String[] args)
	{
		System.out.println("Combien de joueurs ? ");
		Scanner sc = new Scanner(System.in);
		new ControleurCui(sc.nextInt());
		sc.close();
	}

	public Tuile[][] getPlateau() { return metier.getTuiles(); }
	public String getAffichagePlateau() { return metier.toString(); }
}

package ttb;

import ttb.metier.*;

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

	public void jouer()
	{
		do
		{
			Joueur joueur = metier.getJoueurCourant();
			int i = 0;
			do
			{
				Robot r = joueur.getRobot(i);

			}
			metier.changerJoueur();
		}while(metier.getJoueurCourant().getId() != 0);
	}

	public void executerOrdres(char[] ordres, Robot r)
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
			}
		}
	}

	public static void main(String[] args)
	{
		System.out.println("Combien de joueurs ? ");
		Scanner sc = new Scanner(System.in);
		new ControleurCui(sc.nextInt());
	}

	public Tuile[][] getPlateau() { return metier.getTuiles(); }
	public String getAffichagePlateau() { return metier.toString(); }
}

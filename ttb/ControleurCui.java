package ttb;

import ttb.metier.*;

import java.util.Scanner;

public class ControleurCui
{
	private Plateau metier;
	// private IHM     ihm;

	public ControleurCui(int nbJoueurs)
	{
		metier = SetGrille.initGrille(nbJoueurs);
		// ihm    = new IHM();
	}

	public void jouer()
	{
		do
		{
			Joueur joueur = metier.getJoueurCourant();
			Scanner sc = new Scanner(System.in);
			int i = 0;
			do
			{
				Robot rActuel = joueur.getRobot(i);
				System.out.println(metier.toString());
				System.out.println("Indiquez vos action");
				String action = sc.next().toUpperCase();
				for(int j = 0; j < action.length(); j++)
				{
					System.out.println(metier.toString());
					switch(action.charAt(j))
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
				i++;
			} while (i < 2);
			metier.changerJoueur();
		}while(true);
	}

	public static void main(String[] args) {
		System.out.println("Combien de joueurs ? ");
		Scanner sc = new Scanner(System.in);
		new ControleurCui(sc.nextInt()).jouer();
	}

	public Tuile[][] getPlateau() { return metier.getTuiles(); }
}

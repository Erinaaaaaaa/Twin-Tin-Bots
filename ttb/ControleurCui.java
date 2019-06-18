package ttb;

import java.util.Scanner;

public class ControleurCui
{
	private Plateau metier;
	// private IHM     ihm;

	public ControleurCui(int nbJoueurs)
	{
		metier = new Plateau(nbJoueurs);
		// ihm    = new IHM();
	}

	private void jouer()
	{
		joueur = metier.getJoueurCourant();
		Scanner sc = new Scanner(System.in);
		int i = 0;
		do
		{
			Robot rActuel = joueur.getRobot(i);
			System.out.println(toString());
			System.out.println("Entrez une lettre (A, D, G)");
			String choix = sc.next().toUpperCase();
			switch(choix)
			{
				case "A" :
					metier.avancer(r, true);
					break;
				case "D" :
					r.turnAround(false);
					break;
				case "G" :
					r.turnAround(true);
					break;
			}
			i++;
		} while (i < 2);
		metier.changerJoueur();
	}

	public static void main(String[] args) {
		System.out.println("Combien de joueurs ? ");
		Scanner sc = new Scanner(System.in);
		new ControleurCui(sc.nextInt());
	}

	public char[] getPlateau() { return null; }// return metier.getPlateau();}
}

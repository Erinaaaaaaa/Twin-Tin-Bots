package ttb;

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
				boolean bOk = true;
				do
				{
					System.out.println(metier.toString());
					System.out.println("Entrez une lettre (A, D, G)");
					String choix = sc.next().toUpperCase();
					switch(choix)
					{
						case "A" :
							metier.avancer(rActuel, true);
							break;
						case "D" :
							rActuel.turnAround(false);
							break;
						case "G" :
							rActuel.turnAround(true);
							break;
						case "C" :
							metier.chargerCristal(rActuel);
							break;
						case "E" :
							metier.deposerCristal(rActuel);
							break;
						default : bOk = false;
					}
				}while(bOk);
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

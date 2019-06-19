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
			Scanner sc = new Scanner(System.in);
			int i = 0;
			do
			{
				Robot rActuel = joueur.getRobot(i);
				ihm.afficherPlateau();
				//for(int j = 0; j < action.length(); j++)
				boolean bOk = true;
				do
				{
					String action = ihm.getAction();
					switch(action.charAt(0))
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
						default :
							bOk = false;
					}
					ihm.afficherPlateau();
				}while(bOk);
				i++;
			} while (i < 2);
			metier.changerJoueur();
		}while(true);
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

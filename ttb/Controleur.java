package ttb;

import java.util.Scanner;

public class Controleur
{
	private Plateau metier;
	// private IHM     ihm;

	public Controleur(int nbJoueurs)
	{
		metier = new Plateau(nbJoueurs);
		// ihm    = new IHM();
	}

	public static void main(String[] args) {
		System.out.println("Combien de joueurs ? ");
		Scanner sc = new Scanner(System.in);
		new Controleur(sc.nextInt());
	}

	public char[] getPlateau() { return null; }// return metier.getPlateau();}
}

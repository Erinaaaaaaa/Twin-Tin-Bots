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

	public static void main(String[] args) {
		System.out.println("Combien de joueurs ? ");
		Scanner sc = new Scanner(System.in);
		new ControleurCui(sc.nextInt());
	}

	public char[] getPlateau() { return null; }// return metier.getPlateau();}
}

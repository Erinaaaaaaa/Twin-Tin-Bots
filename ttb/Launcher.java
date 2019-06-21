package ttb;

import ttb.gui.fx.LauncherIhm;

import java.util.Scanner;

/**
 * Classe launcher
 * Permet de lancer soit l'IHM CUI ou l'IHM GUI JavaFX
 * @author Jérémy Auzou
 * @author Matys Achart
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */
public class Launcher
{
	private static Scanner sc = new Scanner(System.in);
	public static void main(String[] args)
	{
		System.out.println("Choix de l'IHM: ");
		System.out.println(" 1 - IHM CUI");
		System.out.println(" 2 - IHM JavaFX");
		int choix;

		while ((choix = choisir("Mode")) < 1 || choix > 2) ;

		switch (choix)
		{
			case 1:

				String debug = new Scanner(System.in).nextLine();
				int nbJoueurs = choisir("Nombre de joueurs");
				if (debug.equals("yes"))
				{
					int numScenar = choisir("Numéro du scénario");
					new ControleurCui(nbJoueurs).debug(nbJoueurs,0,numScenar);
				}
				else
					new ControleurCui(nbJoueurs).jouer();
				break;
			case 2:
				LauncherIhm.main(null);
				break;
			default:
				System.exit(0);
		}
	}

	private static int choisir(String text)
	{
		System.out.print(text + ": ");
		return sc.nextInt();
	}
}

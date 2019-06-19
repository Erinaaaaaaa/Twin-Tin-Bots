package ttb;

import ttb.gui.fx.*;
import java.util.Scanner;

/**
 * Classe launcher, permettant de lancer soit l'IHM CUI ou l'IHM GUI JavaFX
 */
public class Launcher
{
    public static void main(String[] args)
    {
        System.out.println("Choix de l'IHM: ");
        System.out.println(" 1 - IHM CUI");
        System.out.println(" 2 - IHM JavaFX");
        int choix;

        while( (choix = choisir("Mode")) < 1 || choix > 2);

        switch (choix)
        {
            case 1:
                new ControleurCui(choisir("Nombre de joueurs"));
                break;
            case 2:
                IhmGui.main(null);
                break;
            default:
                System.exit(0);
        }
    }

    private static int choisir(String text)
    {
        System.out.print(text + ": ");
		Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }
}

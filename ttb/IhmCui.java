package ttb;

import java.util.Scanner;

public class IhmCui
{
	private ControleurCui ctrl;

	public IhmCui(ControleurCui ctrl)
	{
		this.ctrl = ctrl;
	}

	public void afficher()
	{
		System.out.println(ctrl.getInfosJoueur());
		System.out.println(ctrl.getAffichagePlateau());
		System.out.println("\n Quelle action effectuer ?");
		System.out.println("\t[A]jouter/Remplacer une carte");
		System.out.println("\t[P]ermuter une carte");
		System.out.println("\t[E]nlever une carte");
		System.out.println("\t[R]einitialiser les ordres");
		System.out.println("\t[N]e rien faire");
	}

	public void erreur()
	{
		System.out.println("Option invalide");
	}

	public String getAction()
	{
		System.out.print("Action : ");
		Scanner sc = new Scanner(System.in);
		return sc.next().toUpperCase();
	}

	public char getCarte()
	{
		System.out.println("Choisissez une carte de la réserve : ");
		Scanner sc = new Scanner(System.in);
		return sc.next().charAt(0);
	}

	public int getInd(char[] ordres)
	{
		Scanner sc = new Scanner(System.in);
		String ind;
		do
		{
			System.out.print("Choisir l'indice : ");
			ind = sc.next();
		} while (!ind.matches("[1-6]"));

		return Integer.parseInt(ind);
	}
}

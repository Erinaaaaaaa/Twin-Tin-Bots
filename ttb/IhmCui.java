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
		System.out.println("\n Quel action effectuer ?");
		System.out.println("\t[A]jouter/Remplacer une carte");
		System.out.println("\t[P]ermuter une carte");
		System.out.println("\t[E]nlever une carte");
		System.out.println("\t[R]einitialiser les ordres");
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
		System.out.print("Choisir l'indice : ");
		Scanner sc = new Scanner(System.in);
		int ind;
		do
		{
			try
			{
				ind = sc.nextInt();
			}
			catch(Exception e) { ind = -1; }
		} while (ind < 0);
		return ind-1;
	}
}

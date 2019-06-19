package ttb;

import java.util.Scanner;
import iut.algo.CouleurConsole; 
import iut.algo.Console; 


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
		this.afficherPlateau();
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

	public void afficherPlateau()
	{
		String cara;
		for(int i = 0; i < ctrl.getNbLigne(); i++)
		{
			if (i%2 == 0)
				System.out.print("  ");
			for(int j = 0; j < ctrl.getNbColonne(); j++)
			{
				cara = ctrl.getSymbole(i,j);
				
				switch(cara)
				{
					case "R" : Console.couleurFont ( ctrl.getCouleur(i,j) );
					break;
					case "2" : Console.couleurFont ( ctrl.getCouleur(i,j) ); cara = "C";
					break;
					case "3" : Console.couleurFont ( ctrl.getCouleur(i,j) ); cara = "C";
					break;
					case "4" : Console.couleurFont ( ctrl.getCouleur(i,j) ); cara = "C";
					break;
					case "B" : Console.couleurFont ( ctrl.getCouleur(i,j) );
				}
				System.out.print("  "+cara+" ");
				Console.normal(); 
			}
			System.out.print("\n");

		}
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

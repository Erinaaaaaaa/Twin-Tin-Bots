package ttb;

import java.util.List;
import java.util.Scanner;

import iut.algo.CouleurConsole;
import iut.algo.Console;
import ttb.metier.*;

/**
 * Classe IhmCui
 * @author Jérémy Auzou
 * @author Matys Achart
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */

public class IhmCui
{
	private ControleurCui ctrl;
	private Scanner       sc;

	public IhmCui(ControleurCui ctrl)
	{
		this.ctrl = ctrl;
		sc = new Scanner(System.in);
	}

	public void afficher(String info, CouleurConsole coul)
	{
		if(coul != null) Console.couleurFont (coul);

		System.out.println(info);
		Console.normal();
		
	}

	public void erreur()
	{
		System.out.println("Option invalide");
	}

	public void afficherString(char[] cara)
	{
		System.out.println(new String(cara));
	}

	public void controlesScenario()
	{
		System.out.println("[S]uivant | [P]récédent | [Q]uitter");
	}

	public String getAction()
	{
		System.out.println("\nQuel action effectuer ?");
		System.out.println("\t[A]jouter/Remplacer une carte");
		System.out.println("\t[P]ermuter une carte");
		System.out.println("\t[E]nlever une carte");
		System.out.println("\t[R]einitialiser les ordres");
		System.out.println("\t[N]e rien faire");
		System.out.print("\tAction : ");
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
					break;
					case "T" : Console.couleurFont (CouleurConsole.BLANC );
				}
				System.out.print("  "+cara+" ");
				Console.normal();
			}
			System.out.print("\n");

		}
		System.out.println();
	}

	public void finPartie(List<Joueur> gagnants)
	{
		System.out.println("\n-------------- Fin de la partie --------------");
		if (gagnants != null)
			for (Joueur j : gagnants)
				System.out.println("Le joueur " + j.getCouleur() +
				                   " a gagné avec " + j.getPoints() + " points.");
	}

	public char getCarte()
	{
		System.out.println("Choisissez une carte de la réserve : ");
		return sc.next().toUpperCase().charAt(0);
	}

	public int getInd(char[] ordres)
	{
		String ind;
		do
		{
			System.out.print("Choisir l'indice : ");
			ind = sc.next();
		} while (!ind.matches("[1-6]"));

		return Integer.parseInt(ind) - 1;
	}

	public int getRobot(Joueur j)
	{
		String s = "";
		for(int i = 0; i < j.getRobots().length; i++)
		{
			s += "\nRobot " + (i+1) + " :\n\t";
			for(char c : j.getOrdres(i))
			{
				s += ("[");
				if(c == '\0')
					s += (" ");
				else
					s += (c);

				s += ("] ");
			}
		}

		System.out.println(s);

		String ind;
		do
		{
			System.out.print("Choisissez quel robot modifier : ");
			ind = sc.next();
		} while (!ind.matches("[1-2]"));

		return Integer.parseInt(ind) - 1;
	}
}

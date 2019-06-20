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

	/**
	 * Constructeur par défaut de l'affichage en CUI.
	 * @param ctrl
	 */
	public IhmCui(ControleurCui ctrl)
	{
		this.ctrl = ctrl;
		sc = new Scanner(System.in);
	}

	/**
	 * Affiche une information avec la couleur demandée.
	 * @param info l'information à afficher.
	 * @param coul la couleur à utiliser.
	 */
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

	public void afficherString(String s)
	{
		System.out.println(s);
	}

	/**
	 * Affiche les possibilités de contrôle du mode scénario.
	 */
	public void controlesScenario()
	{
		System.out.println("[S]uivant | [P]récédent | [Q]uitter");
	}

	/**
	 * Affiche les actions possibles pendant le tour d'un joueur.
	 * @return l'action effectuée par le joueur.
	 */
	public String getAction()
	{
		System.out.println("\nQuelle action effectuer ?");
		System.out.println("\t[A]jouter/Remplacer une carte");
		System.out.println("\t[P]ermuter une carte");
		System.out.println("\t[E]nlever une carte");
		System.out.println("\t[R]einitialiser les ordres");
		System.out.println("\t[N]e rien faire");
		System.out.print("\tAction : ");
		return sc.next().toUpperCase();
	}

	/**
	 * Affiche le plateau de jeu, avec couleurs.
	 */
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

	/**
	 * Affiche les gagnants de la partie, une fois finie.
	 * @param gagnants la liste du ou des gagnants.
	 */
	public void finPartie(List<Joueur> gagnants)
	{
		System.out.println("\n-------------- Fin de la partie --------------");
		if (gagnants != null && !gagnants.isEmpty())
		{
			for (Joueur j : gagnants)
				System.out.println("Le joueur " + j.getCouleur() +
								   " a gagné avec " + j.getPoints() + " points.");
		}
		else
			System.out.println("Tous les joueurs ont gagné.");

	}

	/**
	 * Demande à un joueur de choisir une des cartes dans sa réserve.
	 * @return le caractère correspondant à la carte.
	 */
	public char getCarte()
	{
		System.out.println("Choisissez une carte de la réserve : ");
		return sc.next().toUpperCase().charAt(0);
	}

	/**
	 * Demande un indice du tableau d'ordres.
	 * @return l'indice choisi.
	 */
	public int getInd(char[] ordres)
	{
		String s = "";
		for(int i = 0; i < ordres.length; i++)
		{
			s += "[";
			if(ordres[i] != '\0')
				s += ordres[i];
			else
				s += " ";

			s += "] ";
		}
		System.out.println(s);
		do
		{
			System.out.print("Choisir l'indice : ");
			s = sc.next();
		} while (!s.matches("[1-3]"));

		return Integer.parseInt(s) - 1;
	}

	/**
	 * Affiche l'état des robots d'un joueur, et demande la modification.
	 * @param j le joueur pour qui modifier un robot.
	 * @return l'indice du robot à modifier.
	 */
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

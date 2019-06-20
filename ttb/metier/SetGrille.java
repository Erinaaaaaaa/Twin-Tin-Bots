package ttb.metier;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public abstract class SetGrille
{
	private static int nbJoueur;

	/**
	 * Initialise le plateau à partir du fichier niveau.data
	 * et place les robots, les bases et les cristaux.
	 * @param nbJoueur le nombre de joueurs
	 * @return un Plateau initialisé.
	 */
	public static Plateau initGrille(int nbJoueur)
	{
		SetGrille.nbJoueur = 0;

		Tuile[][] grille;
		Joueur[]  tabJoueur = new Joueur[nbJoueur];
		Scanner   sc;
		String[]  temp;
		String    nextLigne;
		String    fileAttent = "";

		SetGrille.nbJoueur = nbJoueur;

		tabJoueur = new Joueur[nbJoueur];
		for(int i = 0; i < nbJoueur; i++)
			tabJoueur[i] = new Joueur();

		if(nbJoueur <= 4) grille = SetGrille.creePlateau(9);
		else              grille = SetGrille.creePlateau(11);

		try
		{
			sc  = new Scanner(new FileReader("./ttb/niveau.data"));

			// Les données de chaque plateau sont stockées sur une ligne.
			// Le plateau change à chaque fois que l'on ajoute un joueur.
			for (int i = 1; i < nbJoueur; i++)
				sc.nextLine();

			nextLigne = sc.nextLine();
			temp      = nextLigne.split("\\|");

			int[] coor;
			for (int i = 0; i < temp.length; i++)
			{
				switch (temp[i].charAt(0))
				{
					case 'R' :
						coor = SetGrille.getCooordonees(temp[i]);
						grille[coor[0]][coor[1]] = Tuile.ROBOT;
						tabJoueur[Integer.parseInt("" +temp[i].charAt(1))].
						ajouterRobot(new Robot(coor[0],coor[1],
						                       Integer.parseInt(temp[i].charAt(2) + "")));
						break;

					case 'B' :
						coor = SetGrille.getCooordonees(temp[i]);
						grille[coor[0]][coor[1]] = Tuile.BASE;
						tabJoueur[Integer.parseInt(temp[i].charAt(1) + "")].setBase(coor);
						break;

					case 'C' :
						coor = SetGrille.getCooordonees(temp[i]);
						grille[coor[0]][coor[1]] = SetGrille.getTypeCristal(temp[i]);
						break;

					case 'A' : fileAttent = SetGrille.getFileAttente(temp[i]);
						break;
				}
			}

			sc.close();
		}
		catch (FileNotFoundException e) {e.printStackTrace();}

		return new Plateau(grille,tabJoueur,fileAttent);
	}

	/**
	 * Créé le Plateau de base, en le remplissant de tuiles vides
	 * selon le nombre de cases.
	 * @param nbCase Le nombre de cases du plateau.
	 * @return le plateau de tuiles initialisé.
	 */
	private static Tuile[][] creePlateau(int nbCase)
	{
		Tuile[][] plateau = new Tuile[nbCase][nbCase];

		for (Tuile[] ligne : plateau) // On remplit le plateau de tuiles inaccessibles
			Arrays.fill(ligne, Tuile.OUT_OF_BOUNDS);

		int nbCasesVoulu = plateau.length/2;  // Nombre de tuiles vides sur une ligne

		int aRemplir;

		for (int i=0; i<plateau.length; i++)
		{
			aRemplir = plateau.length-nbCasesVoulu;
			if (i < plateau.length/2 + 1)
				nbCasesVoulu++;
			else
			{
				aRemplir += 2;
				nbCasesVoulu--;
			}
			if(nbCase ==11) aRemplir--;
			for (int j=aRemplir/2; j<aRemplir/2+nbCasesVoulu; j++)
			{
				plateau[i][j] = Tuile.VIDE;
			}
		}
		return plateau;
	}

	/**
	 * Permet d'obtenir les coordonnées selon les infos données en paramètre.
	 * @param info
	 * @return un tableau contenant les coordonnées.
	 */
	private static int[] getCooordonees(String info)
	{
		String   coorBrut;
		if(info.charAt(0) == 'R') coorBrut = info.substring(4);
		else                      coorBrut = info.substring(3);

		String[] coorString = coorBrut.split(":");
		int[] coor = new int[2];
		coor[0] = Integer.parseInt(coorString[0]);
		coor[1] = Integer.parseInt(coorString[1]);

		if(coor[0] % 2 != 0 && SetGrille.nbJoueur < 5)
			coor[1]++;

		return coor;
	}

	/**
	 * Permet d'obtenir le type de cristal.
	 * @param info
	 * @return la Tuile correspondant à ce cristal.
	 */
	private static Tuile getTypeCristal(String info)
	{
		switch(info.charAt(1))
		{
			case 'V' : return Tuile.CRISTAL_VERT;
			case 'B' : return Tuile.CRISTAL_BLEU;
			case 'R' : return Tuile.CRISTAL_VIOLET;
		}
		return Tuile.VIDE;
	}

	/**
	 * Permet d'obtenir la file d'attente du plateau, qui correspond
	 * aux cristaux en attente.
	 * @param info
	 * @return la file d'attente de cristaux.
	 */
	private static String getFileAttente(String info)
	{
		String   infoBrut    = info.substring(3);
		String   fileAttente = "";

		String[] tabInfo     = infoBrut.split(":");

		for(String cristal : tabInfo)
			fileAttente += cristal.charAt(1);

		return fileAttente;

	}
}

package ttb;

import java.util.Scanner;
import java.io.FileReader;
import java.util.Arrays;

public abstract class SetGrille
{
	public static Plateau initGrille(int nbJoueur)
	{
		Tuile[][] grille;
		Joueur[]  tabJoueur = new Joueur[nbJoueur];
		Scanner   sc;
		String[]  temp;
		String    nextLigne;
		String    fileAttent = "";

		tabJoueur = new Joueur[nbJoueur];
		for(int i = 0; i < nbJoueur; i++)
			tabJoueur[i] = new Joueur();

		if(nbJoueur <= 4) grille = SetGrille.creePlateau(9);
		else              grille = SetGrille.creePlateau(11);


		try
		{
			sc  = new Scanner(new FileReader("./ttb/niveau.data"));

			for (int i = 1; i < nbJoueur; i++)
				sc.nextLine();

			nextLigne = sc.nextLine();
			temp      = nextLigne.split("\\|");

			int[] coor;
			for (int i = 0; i < temp.length; i++)
			{
				switch ( temp[i].charAt(0))
				{
					case 'R' : 	coor = SetGrille.getCooordoner(temp[i]);
								grille[coor[0]][coor[1]] = Tuile.ROBOT;
								System.out.println(Integer.parseInt("" +temp[i].charAt(1)));
								tabJoueur[Integer.parseInt("" +temp[i].charAt(1))].ajouterRobot(new Robot(coor[0],coor[1],Integer.parseInt(temp[i].charAt(2) + "")));
					break;

					case 'B' :	coor = SetGrille.getCooordoner(temp[i]);
								grille[coor[0]][coor[1]] = Tuile.BASE;
								tabJoueur[Integer.parseInt(temp[i].charAt(1) + "")].setBase(coor);
								break;

					case 'C' :	coor = SetGrille.getCooordoner(temp[i]);
								grille[coor[0]][coor[1]] = SetGrille.getTypeCristal(temp[i]);
								break;

					case 'A' : fileAttent = SetGrille.getFileAttente(temp[i]);
							   break;
				}
			}

			sc.close();
		}
		catch (Exception e){e.printStackTrace();}

		return new Plateau(grille,tabJoueur,fileAttent);
	}

	private static Tuile[][] creePlateau(int nbCase)
	{
		Tuile[][] plateau = new Tuile[nbCase][nbCase];

		for (Tuile[] ligne : plateau) // On remplit le plateau de tuiles inaccessibles
			Arrays.fill(ligne, Tuile.OUT_OF_BOUNDS);

		int nbCasesVoulu = plateau.length/2;

		int aRemplir;

		for (int i=0; i<plateau.length; i++)
		{
			aRemplir = plateau.length-nbCasesVoulu;
			if (i < plateau.length/2+1)
				nbCasesVoulu++;
			else
			{
				aRemplir+=2;
				nbCasesVoulu--;
			}
			for (int j=aRemplir/2; j<aRemplir/2+nbCasesVoulu; j++)
			{
				plateau[i][j] = Tuile.VIDE; // Tuile vide
			}
		}
		return plateau;
	}

	private static int[] getCooordoner(String info)
	{
		String   coorBrut;
		if(info.charAt(0) == 'R') coorBrut = info.substring(4);
		else                      coorBrut = info.substring(3);

		String[] coorString = coorBrut.split(":");
		int[] coor = new int[2];
		coor[0] = new Integer(coorString[0]);
		coor[1] = new Integer(coorString[1]);

		return coor;
	}

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

	private static String getFileAttente(String info)
	{
		String   infoBrut = info.substring(3);
		String   fileAttente = "";
		String[] tabInfo = info.split(":");

		for(String cristal : tabInfo)
			fileAttente += cristal.charAt(1);

		return fileAttente;

	}
}

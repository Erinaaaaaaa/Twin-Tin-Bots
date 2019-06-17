package ttb;

import java.util.Scanner;
import java.io.FileReader;
import java.util.Arrays;

public abstract class SetGrille
{

    public static Tuile[][] initGrille(int nbJoueur)
    {
        Tuile[][] grille;
        Joueur[]  tabJoueur;
		Scanner   sc;
        String[]  temp;
        String    nextLigne;
        
        if( nbJoueur > 4) grille = SetGrille.creePlateau(9);
        else              grille = SetGrille.creePlateau(11);

        

		try
		{
			sc  = new Scanner(new FileReader("niveau.data"));
            
            for (int i = 0; i < nbJoueur; i++)
                sc.nextLine();
                
            nextLigne = sc.nextLine();
            temp      = nextLigne.split("\\|");

			for (int i = 0; i < temp.length; i++)
			{
                switch ( temp[i].substring(0))
                {
                    case "R" :
                    break;
                    
                    case "B" :
                    break;

                    case "C" :
                    break;

                    case "A" :
                    break;

                }
			}

		sc.close();
		}
        catch (Exception e){e.printStackTrace();}
        
        return grille;
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
			else {
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
    
    
}

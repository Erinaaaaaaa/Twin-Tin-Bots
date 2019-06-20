package ttb.metier;

/**
 * Classe Tuile.
 * Contient tous les types de Tuile utiles à l'affichage du plateau.
 * @author Jérémy Auzou
 * @author Matys Achart
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */
public enum Tuile
{
	OUT_OF_BOUNDS(null),
	VIDE("T"),
	CRISTAL_BLEU("2"),
	CRISTAL_VERT("3"),
	CRISTAL_VIOLET("4"),
	ROBOT("R"),
	BASE("B");

	/**
	 * Le nom de la Tuile, utilisé dans l'affichage.
	 */
	private String nom;

	private Tuile(String nom)
	{
		this.nom = nom;
	}

	/**
	 * Détermine si la Tuile passée en paramètre est un cristal.
	 * @param tuile la Tuile à tester.
	 * @return true si c'est une tuile, false sinon.
	 */
	public static boolean isCristal(Tuile tuile)
	{
		return tuile.name().contains("CRISTAL");
	}

	/**
	 * Retourne le cristal en fonction des caractères utilisés pour déterminer
	 * sa couleur.
	 * @param type le caractère correspondant au cristal.
	 * @return la Tuile correspondant à un cristal.
	 */
	public static Tuile getCristal(char type)
	{
		switch (type)
		{
			case 'B' : return Tuile.CRISTAL_BLEU;
			case 'V' : return Tuile.CRISTAL_VERT;
			case 'R' : return Tuile.CRISTAL_VIOLET;
		}
		return Tuile.VIDE;
	}

	public String toString()
	{
		if (nom != null)
			return nom;
		else
			return " ";
	}
}

package ttb.metier;

/**
 * Classe Tuile.
 * Contient tous les types de Tuile utiles à l'affichage du plateau.
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

	public String toString()
	{
		if (nom != null)
			return nom;
		else
			return " ";
	}
}

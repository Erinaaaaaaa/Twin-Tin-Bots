package ttb.metier;

public enum Tuile
{
	OUT_OF_BOUNDS(null),
	VIDE("T"),
	CRISTAL_BLEU("2"),
	CRISTAL_VERT("3"),
	CRISTAL_VIOLET("4"),
	ROBOT("R"),
	BASE("B");

	private String nom;

	private Tuile(String nom)
	{
		this.nom = nom;
	}

	public static boolean isCristal(Tuile tuile)
	{
		String nomTuile = tuile.name();
		if (nomTuile.lastIndexOf("_") == -1)
			return false;

		return nomTuile.substring(0, nomTuile.lastIndexOf("_")).equals("CRISTAL");
	}

	public String toString()
	{
		if (nom != null)
			return nom;
		else
			return " ";
	}
}

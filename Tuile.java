public enum Tuile
{
	OUT_OF_BOUNDS(null),
	VIDE("T"),
	CRISTAL_BLEU("B"),
	CRISTAL_VERT("V"),
	CRISTAL_VIOLET("R");

	private String nom;

	private Tuile(String nom)
	{
		this.nom = nom;
	}

	public String toString()
	{
		return nom;
	}
}
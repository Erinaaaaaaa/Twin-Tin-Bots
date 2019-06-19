package ttb;

import java.util.Scanner;

public class IhmCui
{
	private ControleurCui ctrl;

	public IhmCui(ControleurCui ctrl)
	{
		this.ctrl = ctrl;
	}

	public void afficherPlateau()
	{
		System.out.println(ctrl.getAffichagePlateau());
	}

	public String getAction()
	{
		System.out.print("Action : ");
		Scanner sc = new Scanner(System.in);
		return sc.next().toUpperCase();
	}

	public int getInd(char[] ordres)
	{
		for (char c: ordres)
			System.out.print(" " + c);

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
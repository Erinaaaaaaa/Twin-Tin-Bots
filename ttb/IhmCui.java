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
}
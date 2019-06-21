package ttb.gui.fx.util;

/**
 * Classe Action
 * @author Jérémy AUZOU
 * @author Matys ACHART
 * @author Kemo DESCHAMPS
 * @author Guillaume COUFOURIER
 * @author Sébastien PRUNIER
 * @version 2019-06-21
 */

public class Action
{
	private  char action;
	private int indice;
	private int robot;

	public Action(int indice, int robot, char action)
	{
		this.indice = indice;
		this.robot  = robot;
		this.action = action;
	}

	public int getIndice()
	{
		return indice;
	}

	public int getRobot()
	{
		return robot;
	}

	public char getAction()
	{
		return action;
	}
}

package ttb.gui.fx.util;

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

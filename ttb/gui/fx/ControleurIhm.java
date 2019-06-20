package ttb.gui.fx;

import ttb.gui.fx.util.Action;
import ttb.metier.*;

public class ControleurIhm
{
	private Action source;

	private Plateau metier;
	private Ihm     ihm;
	private int     coupsDuTour;

	public ControleurIhm(int nbJoueur, Ihm ihm)
	{
		this.coupsDuTour = 0;
		this.metier = SetGrille.initGrille(nbJoueur);
		this.ihm = ihm;
	}

	public int getNbJoueurs() {	return this.metier.getNbJoueurs(); }
	public Joueur getJoueur(int i) { return this.metier.getJoueur(i); }
	public Joueur getJoueurCourant() { return this.metier.getJoueurCourant(); }
	public void avancer(Robot robot, boolean b) { this.metier.avancer(robot, b); }
	public void chargerCristal(Robot robot) { this.metier.chargerCristal(robot); }
	public void deposerCristal(Robot robot) { this.metier.deposerCristal(robot); }
	public void changerJoueur()
	{

		executerOrdres(
				this.metier.getJoueurCourant().getOrdres(0),
				this.metier.getJoueurCourant().getRobot(0)
		);
		executerOrdres(
				this.metier.getJoueurCourant().getOrdres(1),
				this.metier.getJoueurCourant().getRobot(1)
		);
		this.setSource(null);
		this.metier.changerJoueur();
		this.coupsDuTour = 0;
	}
	public Robot getRobotAPosition(int[] coords) { return this.metier.getRobotAPosition(coords); }
	public Joueur getJoueurParBase(int[] coords) { return this.metier.getJoueurParBase(coords); }
	public Tuile[][] getTuiles() { return this.metier.getTuiles(); }
	public String getFileAttente() { return this.metier.getFileAttente(); }

	public void setSource(Action a)
	{
		this.source = a;
		this.ihm.afficherPlateau();
	}

	public Action getSource()
	{
		return this.source;
	}

	public void supprimerOrdre(int robot, int indice)
	{
		if (!actionEffectuee())
		{
			this.metier.getJoueurCourant().enleverOrdre(robot, indice);
			this.ihm.afficherPlateau();
			this.coupsDuTour++;
		}
	}

	public void ajouterOrdre(int robot, int indice)
	{
		if (!actionEffectuee())
		{
			if (this.source.getRobot() > -1 && this.source.getRobot() == robot)
			{
				this.metier.getJoueurCourant().permuterOrdre(robot, this.source.getIndice(), indice);
			} else
			{
				this.metier.getJoueurCourant().ajouterOrdre(robot, indice, source.getAction());
			}
			this.setSource(null);
			this.ihm.afficherPlateau();
			this.coupsDuTour++;
		}
	}

	public void resetRobot(int i)
	{
		if (!actionEffectuee())
		{
			this.metier.getJoueurCourant().resetOrdres(i);
			this.ihm.afficherPlateau();
			this.coupsDuTour++;
		}
	}

	private boolean actionEffectuee()
	{
		if (metier.getNbTours() < metier.getNbJoueurs())
			return this.coupsDuTour >= 2;
		else
			return this.coupsDuTour >= 1;
	}

	public void executerOrdres(char[] ordres, Robot r)
	{
		for(int i = 0; i < ordres.length; i++)
		{
			switch(ordres[i])
			{
				case 'A' :
					metier.avancer(r, true);
					break;
				case 'D' :
					r.turnAround(false);
					break;
				case 'G' :
					r.turnAround(true);
					break;
				case 'C' :
					metier.chargerCristal(r);
					break;
				case 'E' :
					metier.deposerCristal(r);
					break;
				case 'S' :
					metier.avancer(r, true);
					metier.avancer(r, true);
					break;
			}
		}
	}
}

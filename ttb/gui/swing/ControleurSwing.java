package ttb.gui.swing;

import ttb.metier.Plateau;

public class ControleurSwing
{
    private FramePrincipale ihm;
    private Plateau         metier;

    public ControleurSwing()
    {
        this.ihm = new FramePrincipale(this);
    }

    public static void main(String[] args)
    {
        new ControleurSwing();
    }
}

package ttb.gui.swing;

import ttb.ControleurCui;

import javax.swing.*;

public class FramePrincipale extends JFrame
{
    private ControleurSwing ctrl;

    public FramePrincipale(ControleurSwing ctrl)
    {
        this.ctrl = ctrl;

        this.setTitle("Twin Tin Robots");
        this.setLocation(50,50);
        this.setSize(900, 600);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setVisible(true);
    }
}

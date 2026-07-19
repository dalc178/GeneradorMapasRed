package generador.vista;

import generador.modelo.Topologia;

import javax.swing.*;
import java.awt.*;

public class VentanaTopologia extends JDialog {
    private Topologia topologia;

    public VentanaTopologia(Topologia topologia) {
        setTitle("Topología de Red");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.topologia = topologia;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (topologia != null) {
            topologia.dibujar(g, getWidth(), getHeight());
        }
    }
}
package generador.vista;

import generador.modelo.Dispositivo;
import generador.controlador.ControladorPrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VistaPrincipal extends JFrame {
    private JTextField txtIpInicial;
    private JTextField txtIpFinal;
    private JButton btnEscanear;
    private JButton btnTopologia;
    private JButton btnExportar;
    private JButton btnGuardar;
    private JButton btnCargar;
    private JTable tablaDispositivos;
    private DefaultTableModel modeloTabla;
    private JProgressBar barraProgreso;

    public VistaPrincipal() {
        setTitle("Generador de Mapas de Red - Sysadmins");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con BorderLayout
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Panel Superior (IPs y botón Escanear) ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelSuperior.add(new JLabel("IP Inicial:"));
        txtIpInicial = new JTextField("192.168.1.1", 12);
        panelSuperior.add(txtIpInicial);

        panelSuperior.add(new JLabel("IP Final:"));
        txtIpFinal = new JTextField("192.168.1.20", 12);
        panelSuperior.add(txtIpFinal);

        btnEscanear = new JButton("Escanear");
        btnEscanear.setBackground(new Color(50, 200, 50));
        btnEscanear.setForeground(Color.WHITE);
        btnEscanear.setFocusPainted(false);
        panelSuperior.add(btnEscanear);

        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setStringPainted(true);
        barraProgreso.setPreferredSize(new Dimension(200, 25));
        panelSuperior.add(barraProgreso);

        panel.add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {"IP", "Nombre", "Tipo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaDispositivos = new JTable(modeloTabla);
        tablaDispositivos.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tablaDispositivos);
        panel.add(scroll, BorderLayout.CENTER);

     
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnTopologia = new JButton("Ver Topología");
        btnExportar = new JButton("Exportar PNG");
        btnGuardar = new JButton("Guardar CSV");
        btnCargar = new JButton("Cargar CSV");

        btnTopologia.setBackground(new Color(70, 130, 200));
        btnTopologia.setForeground(Color.WHITE);
        btnTopologia.setFocusPainted(false);
        btnExportar.setBackground(new Color(200, 160, 70));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);
        btnGuardar.setBackground(new Color(70, 180, 130));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnCargar.setBackground(new Color(180, 130, 70));
        btnCargar.setForeground(Color.WHITE);
        btnCargar.setFocusPainted(false);

        panelBotones.add(btnTopologia);
        panelBotones.add(btnExportar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCargar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        ControladorPrincipal controlador = new ControladorPrincipal(this);

        btnEscanear.addActionListener(controlador);
        btnTopologia.addActionListener(controlador);
        btnExportar.addActionListener(controlador);
        btnGuardar.addActionListener(controlador);
        btnCargar.addActionListener(controlador);

        add(panel);

        setVisible(true);
    }

    public void actualizarTabla(ArrayList<Dispositivo> dispositivos) {
        modeloTabla.setRowCount(0);
        for (Dispositivo d : dispositivos) {
            modeloTabla.addRow(new Object[]{
                d.getIp(),
                d.getNombre(),
                d.getTipo(),
                d.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    public void setProgreso(int valor) {
        barraProgreso.setValue(valor);
    }

    public JTextField getTxtIpInicial() { return txtIpInicial; }
    public JTextField getTxtIpFinal() { return txtIpFinal; }
    public JButton getBtnEscanear() { return btnEscanear; }
    public JButton getBtnTopologia() { return btnTopologia; }
    public JButton getBtnExportar() { return btnExportar; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnCargar() { return btnCargar; }
    public JTable getTablaDispositivos() { return tablaDispositivos; }
}
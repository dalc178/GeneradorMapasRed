package generador.controlador;

import generador.modelo.Dispositivo;
import generador.modelo.Red;
import generador.modelo.Escanner;
import generador.modelo.Topologia;
import generador.vista.VistaPrincipal;
import generador.vista.VentanaTopologia;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ControladorPrincipal implements ActionListener {
    private VistaPrincipal vista;
    private Red red;
    private Escanner escanner;
    private Topologia topologia;

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vista = vista;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente = e.getSource();

        if (fuente == vista.getBtnEscanear()) {
            escanearRed();
        } else if (fuente == vista.getBtnTopologia()) {
            mostrarTopologia();
        } else if (fuente == vista.getBtnExportar()) {
            exportarDiagrama();
        } else if (fuente == vista.getBtnGuardar()) {
            guardarCSV();
        } else if (fuente == vista.getBtnCargar()) {
            cargarCSV();
        }
    }

    private void escanearRed() {
        String ipInicial = vista.getTxtIpInicial().getText().trim();
        String ipFinal = vista.getTxtIpFinal().getText().trim();

        Escanner escannerTemp = new Escanner(null);
        if (!escannerTemp.ipValida(ipInicial) || !escannerTemp.ipValida(ipFinal)) {
            JOptionPane.showMessageDialog(vista, "Formato de IP inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!escannerTemp.rangoValido(ipInicial, ipFinal)) {
            JOptionPane.showMessageDialog(vista, "Rango inválido. La IP inicial debe ser menor o igual a la final", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        red = new Red(ipInicial, ipFinal);
        escanner = new Escanner(red);

        Thread hilo = new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i += 10) {
                    Thread.sleep(50);
                    int finalI = i;
                    SwingUtilities.invokeLater(() -> vista.setProgreso(finalI));
                }
                escanner.escanear();
                SwingUtilities.invokeLater(() -> {
                    vista.actualizarTabla(red.getDispositivos());
                    JOptionPane.showMessageDialog(vista, "Escaneo completado. " + red.getCantidadDispositivos() + " dispositivos encontrados.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        hilo.start();
    }

    private void mostrarTopologia() {
    if (red == null || red.getDispositivos().isEmpty()) {
        JOptionPane.showMessageDialog(vista, "Primero debes escanear una red.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    topologia = new Topologia(red);
    VentanaTopologia ventana = new VentanaTopologia(topologia);
    ventana.setVisible(true);
}

    private void exportarDiagrama() {
        if (topologia == null) {
            JOptionPane.showMessageDialog(vista, "Primero debes generar la topología.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("topologia.png"));
        chooser.setFileFilter(new FileNameExtensionFilter("Imagen PNG", "png"));
        if (chooser.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().getAbsolutePath();
            if (!ruta.endsWith(".png")) ruta += ".png";
            topologia.exportarPNG(ruta);
            JOptionPane.showMessageDialog(vista, "Diagrama exportado a: " + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void guardarCSV() {
        if (red == null || red.getDispositivos().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay datos para guardar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("red.csv"));
        if (chooser.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().getAbsolutePath();
            if (!ruta.endsWith(".csv")) ruta += ".csv";
            try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
                writer.println("IP,Nombre,Tipo,Activo");
                for (Dispositivo d : red.getDispositivos()) {
                    writer.println(d.getIp() + "," + d.getNombre() + "," + d.getTipo() + "," + d.isActivo());
                }
                JOptionPane.showMessageDialog(vista, "Datos guardados en: " + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(vista, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarCSV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));
        if (chooser.showOpenDialog(vista) == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().getAbsolutePath();
            try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
                String linea = reader.readLine();
                if (linea == null || !linea.startsWith("IP,Nombre,Tipo,Activo")) {
                    JOptionPane.showMessageDialog(vista, "Formato CSV inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                red = new Red("", "");
                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length == 4) {
                        Dispositivo d = new Dispositivo(partes[0], partes[1], partes[2]);
                        d.setActivo(Boolean.parseBoolean(partes[3]));
                        red.agregarDispositivo(d);
                    }
                }
                vista.actualizarTabla(red.getDispositivos());
                JOptionPane.showMessageDialog(vista, "Datos cargados: " + red.getCantidadDispositivos() + " dispositivos.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(vista, "Error al cargar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
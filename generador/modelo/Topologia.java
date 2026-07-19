package generador.modelo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Topologia {
    private Red red;

    public Topologia(Red red) {
        this.red = red;
    }

    public void dibujar(Graphics g, int ancho, int alto) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, ancho, alto);

        ArrayList<Dispositivo> dispositivos = red.getDispositivos();
        if (dispositivos.isEmpty()) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("No hay dispositivos para mostrar", 50, ancho / 2);
            return;
        }

        int cantidad = dispositivos.size();
        int centroX = ancho / 2;
        int centroY = alto / 2;
        int radio = Math.min(ancho, alto) / 2 - 100;

        Font fontTexto = new Font("Arial", Font.BOLD, 11);
        g2d.setFont(fontTexto);
        FontMetrics fm = g2d.getFontMetrics();

        int[] posX = new int[cantidad];
        int[] posY = new int[cantidad];
        int[] diametros = new int[cantidad];

        for (int i = 0; i < cantidad; i++) {
            double angulo = (2 * Math.PI / cantidad) * i - Math.PI / 2;
            posX[i] = (int) (centroX + radio * Math.cos(angulo));
            posY[i] = (int) (centroY + radio * Math.sin(angulo));
        }

        for (int i = 0; i < cantidad; i++) {
            Dispositivo d = dispositivos.get(i);
            String nombre = d.getNombre();
            String ip = d.getIp();
            
            if (nombre.length() > 10) {
                nombre = nombre.substring(0, 9) + ".";
            }
            if (ip.length() > 13) {
                ip = ip.substring(0, 12) + ".";
            }
            
            int anchoNombre = fm.stringWidth(nombre);
            int anchoIp = fm.stringWidth(ip);
            int anchoTexto = Math.max(anchoNombre, anchoIp);
            int diametro = Math.max(anchoTexto + 20, 40);
            diametros[i] = diametro;
        }

        for (int i = 0; i < cantidad; i++) {
            int x = posX[i];
            int y = posY[i];
            int diametro = diametros[i];
            int xCirculo = x - diametro / 2;
            int yCirculo = y - diametro / 2;

            Dispositivo d = dispositivos.get(i);
            Color color = d.isActivo() ? new Color(50, 200, 50) : Color.GRAY;
            g2d.setColor(color);
            g2d.fillOval(xCirculo, yCirculo, diametro, diametro);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(xCirculo, yCirculo, diametro, diametro);

            g2d.setColor(Color.WHITE);
            g2d.setFont(fontTexto);
            
            String nombre = d.getNombre();
            String ip = d.getIp();
            if (nombre.length() > 10) {
                nombre = nombre.substring(0, 9) + ".";
            }
            if (ip.length() > 13) {
                ip = ip.substring(0, 12) + ".";
            }
            
            int nombreX = x - fm.stringWidth(nombre) / 2;
            int nombreY = y - 3;
            g2d.drawString(nombre, nombreX, nombreY);
            
            int ipX = x - fm.stringWidth(ip) / 2;
            int ipY = y + fm.getHeight() - 2;
            g2d.drawString(ip, ipX, ipY);
        }

        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1.5f));
        for (int i = 0; i < cantidad; i++) {
            int x = posX[i];
            int y = posY[i];
            int dx = x - centroX;
            int dy = y - centroY;
            double dist = Math.sqrt(dx*dx + dy*dy);
            if (dist > 10) {
                double factor = (dist - diametros[i]/2 - 3) / dist;
                int xFinal = centroX + (int)(dx * factor);
                int yFinal = centroY + (int)(dy * factor);
                g2d.drawLine(centroX, centroY, xFinal, yFinal);
            }
        }

        Dispositivo router = dispositivos.get(0);
        String nombreRouter = router.getNombre();
        String ipRouter = router.getIp();
        
        if (nombreRouter.length() > 10) {
            nombreRouter = nombreRouter.substring(0, 9) + ".";
        }
        if (ipRouter.length() > 13) {
            ipRouter = ipRouter.substring(0, 12) + ".";
        }
        
        int anchoNombreRouter = fm.stringWidth(nombreRouter);
        int anchoIpRouter = fm.stringWidth(ipRouter);
        int anchoTextoRouter = Math.max(anchoNombreRouter, anchoIpRouter);
        int diametroRouter = Math.max(anchoTextoRouter + 30, 60);
        
        int xRouter = centroX - diametroRouter / 2;
        int yRouter = centroY - diametroRouter / 2;

        g2d.setColor(Color.RED);
        g2d.fillOval(xRouter, yRouter, diametroRouter, diametroRouter);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(xRouter, yRouter, diametroRouter, diametroRouter);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fmRouter = g2d.getFontMetrics();
        
        int nombreRouterX = centroX - fmRouter.stringWidth(nombreRouter) / 2;
        int nombreRouterY = centroY - 4;
        g2d.drawString(nombreRouter, nombreRouterX, nombreRouterY);
        
        int ipRouterX = centroX - fmRouter.stringWidth(ipRouter) / 2;
        int ipRouterY = centroY + fmRouter.getHeight() - 1;
        g2d.drawString(ipRouter, ipRouterX, ipRouterY);
    }

    public void exportarPNG(String rutaArchivo) {
        int ancho = 800;
        int alto = 600;
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics g = imagen.getGraphics();
        dibujar(g, ancho, alto);
        g.dispose();

        try {
            File archivo = new File(rutaArchivo);
            ImageIO.write(imagen, "png", archivo);
            System.out.println("Diagrama exportado a: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("Error al exportar: " + e.getMessage());
        }
    }
}
package generador.modelo;

import java.util.Random;

public class Escanner {
    private Red red;
    private Random random;
    private String[] nombres = {"Router", "Switch", "PC-Oficina", "PC-Taller", "Impresora", "Servidor", "Laptop", "Tablet", "Camara", "AccessPoint"};
    private String[] tipos = {"Router", "Switch", "Computadora", "Impresora", "Servidor", "Dispositivo IoT"};

    public Escanner(Red red) {
        this.red = red;
        this.random = new Random();
    }

    public void escanear() {
        int cantidad = 5 + random.nextInt(11);
        String[] ipParts = red.getIpInicial().split("\\.");
        int inicio = Integer.parseInt(ipParts[3]);
        int fin = Integer.parseInt(red.getIpFinal().split("\\.")[3]);

        for (int i = 0; i < cantidad; i++) {
            int numIp = inicio + random.nextInt(fin - inicio + 1);
            String ip = ipParts[0] + "." + ipParts[1] + "." + ipParts[2] + "." + numIp;
            String nombre = generarNombreAleatorio();
            String tipo = generarTipoAleatorio();
            Dispositivo d = new Dispositivo(ip, nombre, tipo);
            d.setActivo(random.nextDouble() < 0.8);
            red.agregarDispositivo(d);
        }

        if (!red.getDispositivos().isEmpty()) {
            red.getDispositivos().get(0).setActivo(true);
        }
    }

    private String generarNombreAleatorio() {
        return nombres[random.nextInt(nombres.length)] + "-" + (100 + random.nextInt(900));
    }

    private String generarTipoAleatorio() {
        return tipos[random.nextInt(tipos.length)];
    }

    public boolean ipValida(String ip) {
        String[] partes = ip.split("\\.");
        if (partes.length != 4) return false;
        for (String p : partes) {
            try {
                int num = Integer.parseInt(p);
                if (num < 0 || num > 255) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public boolean rangoValido(String ip1, String ip2) {
        if (!ipValida(ip1) || !ipValida(ip2)) return false;
        String[] p1 = ip1.split("\\.");
        String[] p2 = ip2.split("\\.");
        for (int i = 0; i < 3; i++) {
            if (!p1[i].equals(p2[i])) return false;
        }
        return Integer.parseInt(p1[3]) <= Integer.parseInt(p2[3]);
    }
}
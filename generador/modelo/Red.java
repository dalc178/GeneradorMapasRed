package generador.modelo;

import java.util.ArrayList;

public class Red {
    private ArrayList<Dispositivo> dispositivos;
    private String ipInicial;
    private String ipFinal;

    public Red(String ipInicial, String ipFinal) {
        this.ipInicial = ipInicial;
        this.ipFinal = ipFinal;
        this.dispositivos = new ArrayList<>();
    }

    public void agregarDispositivo(Dispositivo d) {
        dispositivos.add(d);
    }

    public ArrayList<Dispositivo> getDispositivos() { return dispositivos; }
    public String getIpInicial() { return ipInicial; }
    public String getIpFinal() { return ipFinal; }
    public int getCantidadDispositivos() { return dispositivos.size(); }
}
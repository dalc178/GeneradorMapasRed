package generador.modelo;

public class Dispositivo {
    private String ip;
    private String nombre;
    private String tipo;
    private boolean activo;

    public Dispositivo(String ip, String nombre, String tipo) {
        this.ip = ip;
        this.nombre = nombre;
        this.tipo = tipo;
        this.activo = true;
    }

    public String getIp() { return ip; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return ip + " - " + nombre + " (" + tipo + ")" + (activo ? " [ACTIVO]" : " [INACTIVO]");
    }
}
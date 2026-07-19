**Generador de Mapas de Red para Sysadmins**

**Autor:** Daniel Lara  
**C.I:** 31.564.519  
**Asignatura:** Técnicas de Programación III (TP3)  
**Profesora:** Dubraska Roca  
**Fecha:** Julio 2026
Universidad Nacional Experimental de Guayana (UNEG)

Descripción...

Aplicación de escritorio en Java que simula el escaneo de un rango de direcciones IP y 
genera automáticamente un diagrama topológico en forma de estrella con un router central 
y los dispositivos como nodos.

Diseñada para entornos educativos y de simulación, permite a los administradores de sistemas 
visualizar la estructura de una red sin necesidad de hardware real.

Tecnologías utilizadas

- Java SE 11
- Swing (interfaz gráfica)
- Graphics2D (dibujo del diagrama)
- File I/O (persistencia en CSV)

**Estructura del proyecto**

generador/

├── modelo/

│   ├── Dispositivo.java

│   ├── Red.java

│   ├── Escanner.java

│   └── Topologia.java

├── vista/

│   ├── VistaPrincipal.java

│   └── VentanaTopologia.java

├── controlador/

│   └── ControladorPrincipal.java

└── Main.java


**Instalación y ejecución**

1. Clonar el repositorio
   
git clone https://github.com/dalc178/GeneradorMapasRed.git

cd GeneradorMapasRed

2. Compilar

Abrir PowerShell en la carpeta del proyecto y ejecutar:
   
javac -d . (Get-ChildItem -Recurse -Filter *.java).FullName

3. Ejecutar
   
java generador.Main

Nota: Los comandos están diseñados para PowerShell de Windows. En otros terminales (CMD o Git Bash), usar el comando alternativo:

javac -d . generador/modelo/*.java generador/vista/*.java generador/controlador/*.java generador/Main.java

**Funcionalidades**

**Función**	       //      **Descripción**

-Escanear red	    //        Simula el escaneo de un rango de IPs y genera dispositivos aleatorios

-Ver topología	    //      Dibuja un diagrama en estrella con router central

-Exportar PNG	    //        Guarda el diagrama como imagen PNG

-Guardar CSV	    //        Guarda la lista de dispositivos en un archivo CSV

-Cargar CSV	       //       Carga dispositivos desde un archivo CSV


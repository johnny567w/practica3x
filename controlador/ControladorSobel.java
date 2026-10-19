package controlador;

import modelo.ModeloSobel;
import vista.VistaConsola;

// Controlador: decide cuantos hilos usar y coordina modelo y vista
public class ControladorSobel {
    private final ModeloSobel modelo;
    private final VistaConsola vista;

    public ControladorSobel(ModeloSobel modelo, VistaConsola vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    public void ejecutar(String ruta) throws Exception {
        modelo.cargar(ruta);
        int hilos = Runtime.getRuntime().availableProcessors(); // hilos logicos del CPU
        vista.mostrarInfo(modelo.getAncho(), modelo.getAlto(), hilos);

        modelo.procesar(hilos); // calentamiento del JIT (no se mide)
        long tMulti = modelo.procesar(hilos);
        modelo.guardar("bordes_multihilo.png");
        long tUno = modelo.procesar(1);
        modelo.guardar("bordes_1hilo.png");

        vista.mostrarResultados(hilos, tMulti, tUno);
    }
}

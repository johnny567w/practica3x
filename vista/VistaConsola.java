package vista;

// Vista: solo muestra informacion por consola
public class VistaConsola {
    public void mostrarInfo(int w, int h, int hilos) {
        System.out.println("Imagen " + w + "x" + h + " | hilos disponibles: " + hilos
                + " | filas por hilo: " + (h + hilos - 1) / hilos);
    }

    public void mostrarResultados(int hilos, long tMulti, long tUno) {
        System.out.printf("%d hilos: %d ms%n1 hilo:   %d ms%nSpeedup:  %.2fx%n",
                hilos, tMulti, tUno, (double) tUno / tMulti);
    }
}

package modelo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

// Modelo: datos de la imagen y el filtro Sobel paralelo
public class ModeloSobel {
    private int w, h;
    private int[] gris, salida;

    public void cargar(String ruta) throws Exception {
        BufferedImage img = ImageIO.read(new File(ruta));
        w = img.getWidth(); h = img.getHeight();
        int[] rgb = img.getRGB(0, 0, w, h, null, 0, w);
        gris = new int[w * h];
        for (int i = 0; i < rgb.length; i++) { // pasar a escala de grises
            int p = rgb[i];
            gris[i] = (((p >> 16) & 255) * 299 + ((p >> 8) & 255) * 587 + (p & 255) * 114) / 1000;
        }
    }

    // Divide la imagen en n franjas horizontales, una por hilo, y espera a que todos terminen
    public long procesar(int n) throws InterruptedException {
        salida = new int[w * h];
        Thread[] t = new Thread[n];
        int bloque = (h + n - 1) / n;
        long ini = System.nanoTime();
        for (int k = 0; k < n; k++) {
            int y0 = k * bloque, y1 = Math.min(h, y0 + bloque);
            t[k] = new Thread(() -> sobel(y0, y1));
            t[k].start();
        }
        for (Thread x : t) x.join();
        return (System.nanoTime() - ini) / 1_000_000;
    }

    // Filtro Sobel sobre las filas [y0, y1); cada hilo escribe solo en sus filas del arreglo comun
    private void sobel(int y0, int y1) {
        for (int y = Math.max(1, y0); y < Math.min(h - 1, y1); y++)
            for (int x = 1; x < w - 1; x++) {
                int i = y * w + x;
                int gx = -gris[i - w - 1] + gris[i - w + 1] - 2 * gris[i - 1] + 2 * gris[i + 1] - gris[i + w - 1] + gris[i + w + 1];
                int gy = -gris[i - w - 1] - 2 * gris[i - w] - gris[i - w + 1] + gris[i + w - 1] + 2 * gris[i + w] + gris[i + w + 1];
                int m = Math.min(255, (int) Math.sqrt(gx * gx + gy * gy));
                salida[i] = 0xFF000000 | m << 16 | m << 8 | m;
            }
    }

    public void guardar(String nombre) throws Exception {
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        out.setRGB(0, 0, w, h, salida, 0, w);
        ImageIO.write(out, "png", new File(nombre));
    }

    public int getAncho() { return w; }
    public int getAlto() { return h; }
}

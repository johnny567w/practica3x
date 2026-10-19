import controlador.ControladorSobel;
import modelo.ModeloSobel;
import vista.VistaConsola;

public class Main {
    public static void main(String[] args) throws Exception {
        new ControladorSobel(new ModeloSobel(), new VistaConsola()).ejecutar("imagen.jpg");
    }
}

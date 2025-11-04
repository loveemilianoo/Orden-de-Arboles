
import javax.swing.*;
import java.awt.*;

public class Screen extends JFrame {
    /**
     * Crea una ventana simple
     * 
     * @param title  Título de la ventana
     * @param width  Ancho de la ventana
     * @param height Alto de la ventana
     */

    public Screen(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    /**
     * Muestra la ventana al usuario
     */
    public void showScreen() {
        setVisible(true);
    }

    public static void main(String[] args) {
        Screen screen = new Screen("Arboles - Equipo 4", 500, 400);
        screen.showScreen();
    }
}
import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        Screen screen;
        TreePanel treePanel;
        ArbolExpresion arbol;
        JButton preordenBtn;
        JButton ordenBtn;
        JButton posordenBtn;
        String expresion;

        try {

            // ? Se inicia la ventana con un border layout
            screen = new Screen("Árbol binario", 800, 500);
            screen.setLayout(new BorderLayout());

            // ? Se ingresa la expresión
            expresion = Dialogs.getExpression("Expresión", "Por favor, ingresa la expresión");

            System.out.println("##### Expresión: " + expresion + " #####");

            // ? Se crea el arbol y su componente gráfico
            arbol = new ArbolExpresion(expresion);
            treePanel = new TreePanel(arbol);

            // ? Se crean los botones y se añade su función
            // ? Botón pre-orden
            preordenBtn = new JButton("Mostrar preorden");
            preordenBtn.addActionListener(
                    e -> Dialogs.showDialog("Recorrido en preorden", arbol.preOrden()));
            // ? Botón orden
            ordenBtn = new JButton("Mostrar orden");
            ordenBtn.addActionListener(
                    e -> Dialogs.showDialog("Recorrido en orden", arbol.orden()));
            // ? Botón pos-orden
            posordenBtn = new JButton("Mostrar posorden");
            posordenBtn.addActionListener(
                    e -> Dialogs.showDialog("Recorrido en posorden", arbol.posOrden()));

            // ? Se añaden los componentes a la pantalla
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.add(preordenBtn);
            buttonPanel.add(ordenBtn);
            buttonPanel.add(posordenBtn);
            screen.add(treePanel, BorderLayout.CENTER);
            screen.add(buttonPanel, BorderLayout.SOUTH);

            screen.showScreen();

        } catch (Exception e) {
            Dialogs.showError("Error : " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }
}

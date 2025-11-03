
import javax.swing.*;

import java.awt.*;

public class TreePanel extends JPanel {
    private NodoExpresion root;
    private int nodeRadius = 30;
    private int verticalGap = 80;

    public TreePanel(NodoExpresion root) {
        this.root = root;
        setBackground(Color.WHITE);
    }

    public TreePanel(ArbolExpresion bTree) {
        this.root = bTree.raiz;
    }

    public void setRoot(NodoExpresion root) {
        this.root = root;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (root != null) {
            drawNode(g, root, getWidth() / 2, 30, getWidth() / 4);
        }
    }

    private Color getNodeColor(NodoExpresion node) {

        if ("+-*/".indexOf(node.valor) >= 0 ||
                "div".equalsIgnoreCase(node.valor) ||
                "mod".equalsIgnoreCase(node.valor)) {
            return Color.decode("#6040b0");
        }

        if ("ABCDE".indexOf(node.valor) >= 0) {
            return Color.decode("#103015");
        }

        return Color.BLACK;

    }

    private void drawNode(Graphics g, NodoExpresion node, int x, int y, int horizontalGap) {
        // ? Dibuja nodo

        g.setColor(getNodeColor(node));

        g.fillOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);
        g.setColor(Color.WHITE);
        String text = node.valor;
        g.drawString(text, x - g.getFontMetrics().stringWidth(text) / 2, y + 5);

        g.setColor(Color.BLACK);

        // ? Dibuja hijo izquierdo
        if (node.izq != null) {
            int childX = x - horizontalGap;
            int childY = y + verticalGap;
            g.drawLine(x, y + nodeRadius, childX, childY - nodeRadius);
            drawNode(g, node.izq, childX, childY, horizontalGap / 2);
        }

        // ? Dibuja hijo derecho
        if (node.der != null) {
            int childX = x + horizontalGap;
            int childY = y + verticalGap;
            g.drawLine(x, y + nodeRadius, childX, childY - nodeRadius);
            drawNode(g, node.der, childX, childY, horizontalGap / 2);
        }
    }
}

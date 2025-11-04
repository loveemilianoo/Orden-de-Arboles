/*
 * Programa realizado por el Equipo 8 de la materia de Matemáticas Discretas
 * con la profesora Lilian Karina Espinosa de los Monteros Heredia
 * Integrantes:
 * Sánchez Callejas Eduardo Josafat
 * Trinidad García Dulce Maria
 * Villanueva Reyes Yael
 */


package mx.uaemex.fi.matesDisc.Ordenar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Stack;
import java.util.HashMap;
import java.util.Map;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArbolExpresionGUI extends JFrame {

    private final ArbolLogica arbol = new ArbolLogica();
    private JTextField entradaExpresion;
    private JTextArea salidaRecorridos;
    private PanelDibujo panelArbol;

    private static final int SEP_X = 75; 
    private static final int RADIO = 20;
    private static final int SEP_Y = 120;

    private static class Nodo {
        public String valor;
        public Nodo izquierda;
        public Nodo derecha;

        public Nodo(String valor) {
            this.valor = valor;
            this.izquierda = null;
            this.derecha = null;
        }
    }

    private static class ArbolLogica {
        private Nodo raiz;
        private final Map<String, Integer> prec;

        public ArbolLogica() {
            prec = new HashMap<>() {{
                put("+", 1); put("-", 1);
                put("*", 2); put("/", 2); put("%", 2); put("//", 2); 
                put("^", 3);
            }};
        }

        public Nodo getRaiz() { return raiz; }
        public void limpiarArbol() { this.raiz = null; }

        private boolean esOp(String s) { return prec.containsKey(s); }
        private boolean esOpr(char c) { return Character.isLetterOrDigit(c); }
        
        public int contarHojas(Nodo n) {
            if (n == null) return 0;
            if (n.izquierda == null && n.derecha == null) return 1;
            return contarHojas(n.izquierda) + contarHojas(n.derecha);
        }

        private boolean debeSacar(String opPila, String opActual) {
            if (opPila.equals("(")) return false;
            int precPila = prec.get(opPila);
            int precActual = prec.get(opActual);
            
            if (precPila == precActual && opActual.equals("^")) {
                return false; 
            }
            return precPila >= precActual;
        }

        // CORRECCIÓN FINAL DE TOKENIZACIÓN: Usa Matcher para separar todos los tokens robustamente.
        private String insertarEspacios(String exp) {
            // Regex: Captura grupos de letras/dígitos (operandos) O operadores de dos chars (//) O operadores de un char.
            Pattern pattern = Pattern.compile("([a-zA-Z0-9]+)|(//)|([+\\-*/%^()])");
            Matcher matcher = pattern.matcher(exp);
            StringBuilder sb = new StringBuilder();
            
            while (matcher.find()) {
                sb.append(matcher.group()).append(" ");
            }
            return sb.toString().trim();
        }

        public String convertirInfijaAPostfija(String expInf) throws IllegalArgumentException {
            StringBuilder postfija = new StringBuilder(); 
            Stack<String> pila = new Stack<>();
            String[] elementos = insertarEspacios(expInf).split(" ");
            
            for (String token : elementos) {
                if (token.isEmpty()) continue;
                
                if (!esOp(token) && !token.equals("(") && !token.equals(")")) {
                    postfija.append(token).append(" ");
                } else if (token.equals("(")) {
                    pila.push(token);
                } else if (token.equals(")")) {
                    while (!pila.isEmpty() && !pila.peek().equals("(")) {
                        postfija.append(pila.pop()).append(" ");
                    }
                    if (!pila.isEmpty() && pila.peek().equals("(")) {
                        pila.pop(); 
                    } else {
                        throw new IllegalArgumentException("Paréntesis mal balanceados.");
                    }
                } else if (esOp(token)) {
                    while (!pila.isEmpty() && debeSacar(pila.peek(), token)) {
                        postfija.append(pila.pop()).append(" ");
                    }
                    pila.push(token);
                } else {
                    throw new IllegalArgumentException("Token inválido: " + token);
                }
            }
            
            while (!pila.isEmpty()) {
                if (pila.peek().equals("(") || pila.peek().equals(")")) {
                     throw new IllegalArgumentException("Paréntesis mal balanceados.");
                }
                postfija.append(pila.pop()).append(" ");
            }
            return postfija.toString().trim();
        }

        public void construirArbolDesdePostfija(String postfija) throws IllegalArgumentException {
            if (postfija.isEmpty()) {
                this.raiz = null;
                return;
            }
            Stack<Nodo> pila = new Stack<>();
            String[] elementos = postfija.split(" ");
            
            for (String elemento : elementos) {
                if (elemento.isEmpty()) continue;
                
                if (!esOp(elemento)) {
                    pila.push(new Nodo(elemento));
                } else {
                    if (pila.size() < 2) {
                        // Si la tokenización es correcta, este error solo ocurre si la expresión es mal formada (e.g., "a + * b")
                        throw new IllegalArgumentException("Expresión Postfija inválida (pocos operandos).");
                    }
                    Nodo nodo = new Nodo(elemento);
                    nodo.derecha = pila.pop(); 
                    nodo.izquierda = pila.pop(); 
                    pila.push(nodo);
                }
            }
            
            if (pila.size() != 1) {
                 throw new IllegalArgumentException("Expresión Postfija inválida (exceso de operandos).");
            }
            this.raiz = pila.pop();
        }
        
        public String obtenerInorden() { 
            StringBuilder sb = new StringBuilder();
            inorden(raiz, sb);
            return sb.toString().trim().replaceAll("\\s+", " ").replaceAll("^\\(|\\)$", "").trim(); 
        }
        private void inorden(Nodo n, StringBuilder sb) {
            if (n == null) return;
            boolean necesitaParentesis = esOp(n.valor);
            
            if (necesitaParentesis) sb.append("(");
            inorden(n.izquierda, sb);
            
            if (sb.length() > 0 && (sb.charAt(sb.length() - 1) != '(')) sb.append(" ");
            sb.append(n.valor);
            
            inorden(n.derecha, sb);
            if (necesitaParentesis) sb.append(")");
        }

        public String obtenerPreorden() { 
            StringBuilder sb = new StringBuilder();
            preorden(raiz, sb);
            return sb.toString().trim(); 
        }
        private void preorden(Nodo n, StringBuilder sb) {
            if (n == null) return;
            sb.append(n.valor).append(" ");
            preorden(n.izquierda, sb);
            preorden(n.derecha, sb);
        }

        public String obtenerPostorden() { 
            StringBuilder sb = new StringBuilder();
            postorden(raiz, sb);
            return sb.toString().trim();
        }
        private void postorden(Nodo n, StringBuilder sb) {
            if (n == null) return;
            postorden(n.izquierda, sb);
            postorden(n.derecha, sb);
            sb.append(n.valor).append(" ");
        }
    }

    private class PanelDibujo extends JPanel {
        
        private final Color COLOR_NODO = new Color(153, 102, 204);

        public PanelDibujo() {
            setBackground(Color.WHITE);
        }
        
        @Override
        public Dimension getPreferredSize() {
            if (arbol.getRaiz() == null) return new Dimension(600, 300);
            int numHojas = arbol.contarHojas(arbol.getRaiz());
            int profundidad = calcularProfundidad(arbol.getRaiz());
            int anchoNecesario = numHojas * SEP_X * 8; 
            int alturaNecesaria = profundidad * SEP_Y + 100;

            return new Dimension(Math.max(600, anchoNecesario), Math.max(300, alturaNecesaria));
        }

        private int calcularProfundidad(Nodo n) {
            if (n == null) return 0;
            return 1 + Math.max(calcularProfundidad(n.izquierda), calcularProfundidad(n.derecha));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (arbol.getRaiz() != null) {
                int initialWidth = getPreferredSize().width;
                int factorSepInicial = initialWidth / 4; 
                dibujar(g2d, arbol.getRaiz(), initialWidth / 2, 30, factorSepInicial); 
            } else {
                g.setColor(Color.GRAY);
                g.setFont(new Font("Arial", Font.ITALIC, 16));
                g.drawString("Ingrese una expresión para dibujar el árbol...", 
                             getWidth() / 2 - 150, getHeight() / 2);
            }
        }

        private void dibujar(Graphics2D g2d, Nodo nodo, int x, int y, int sepX) {
            if (nodo == null) return;
            
            g2d.setColor(new Color(150, 150, 150)); 
            g2d.setStroke(new BasicStroke(1.5f)); 
            
            int nextSepXDelta = sepX / 3;
            int x_izq = x - nextSepXDelta;
            int x_der = x + nextSepXDelta;
            int y_hijo = y + SEP_Y;
            
            int sepX_recursivo = sepX / 2; 

            if (nodo.izquierda != null) {
            	g2d.setColor(Color.BLUE);
                g2d.drawLine(x, y + RADIO, x_izq, y_hijo - RADIO);
                dibujar(g2d, nodo.izquierda, x_izq, y_hijo, sepX_recursivo);
            }
            if (nodo.derecha != null) {
            	g2d.setColor(Color.BLUE);
                g2d.drawLine(x, y + RADIO, x_der, y_hijo - RADIO);
                dibujar(g2d, nodo.derecha, x_der, y_hijo, sepX_recursivo);
            }

            g2d.setColor(COLOR_NODO);
            g2d.fillOval(x - RADIO, y - RADIO, 2 * RADIO, 2 * RADIO);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x - RADIO, y - RADIO, 2 * RADIO, 2 * RADIO);
            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            
            FontRenderContext frc = g2d.getFontRenderContext();
            Rectangle2D bounds = g2d.getFont().getStringBounds(nodo.valor, frc);
            double anchoTexto = bounds.getWidth();
            
            g2d.drawString(nodo.valor, 
                           (float) (x - anchoTexto / 2), 
                           (float) (y + bounds.getHeight() / 2 - bounds.getY() - RADIO / 2));
        }
    }

    public ArbolExpresionGUI() {
        inicializarComponentes();
        configurarVentana();
    }

    private void inicializarComponentes() {
        setTitle("Árbol Binario de Expresión (Optimizado)");
        setLayout(new BorderLayout(10, 10));

        JPanel panelEntrada = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelEntrada.add(new JLabel("Expresión Infija (ej: a + b * c^2 % x):"));
        entradaExpresion = new JTextField(30);
        panelEntrada.add(entradaExpresion);
        
        JButton botonProcesar = new JButton("Convertir, Construir y Recorrer");
        JButton botonLimpiar = new JButton("Limpiar");
        
        botonProcesar.addActionListener(this::procesarExpresion);
        botonLimpiar.addActionListener(this::limpiarInterfaz);
        
        panelEntrada.add(botonProcesar);
        panelEntrada.add(botonLimpiar);
        add(panelEntrada, BorderLayout.NORTH);

        JPanel panelOeste = new JPanel();
        panelOeste.setLayout(new BorderLayout());
        salidaRecorridos = new JTextArea(10, 25); 
        salidaRecorridos.setEditable(false);
        salidaRecorridos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollRecorridos = new JScrollPane(salidaRecorridos);
        scrollRecorridos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollRecorridos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panelOeste.add(new JLabel("Recorridos y Postfija:"), BorderLayout.NORTH);
        panelOeste.add(scrollRecorridos, BorderLayout.CENTER);
        add(panelOeste, BorderLayout.WEST);

        panelArbol = new PanelDibujo();
        panelArbol.setBorder(BorderFactory.createTitledBorder("Visualización del Árbol de Expresión"));
        
        JScrollPane scrollArbol = new JScrollPane(panelArbol);
        scrollArbol.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollArbol, BorderLayout.CENTER);
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 500); 
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void limpiarInterfaz(ActionEvent e) {
        entradaExpresion.setText("");
        salidaRecorridos.setText("");
        arbol.limpiarArbol();
        panelArbol.revalidate(); 
        panelArbol.repaint();
    }

    private void procesarExpresion(ActionEvent e) {
        String expInfija = entradaExpresion.getText().trim().replaceAll("\\s", ""); 
        salidaRecorridos.setText("");
        
        if (expInfija.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                                          "Debe ingresar una expresión.", 
                                          "Error de Entrada", 
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String postfija = arbol.convertirInfijaAPostfija(expInfija);
            arbol.construirArbolDesdePostfija(postfija);

            String inorden = arbol.obtenerInorden();
            String preorden = arbol.obtenerPreorden();
            String postorden = arbol.obtenerPostorden();

            salidaRecorridos.append("POSTFIJA:\n" + postfija + "\n\n");
            salidaRecorridos.append("INORDEN:\n" + inorden + "\n\n");
            salidaRecorridos.append("PREORDEN:\n" + preorden + "\n\n");
            salidaRecorridos.append("POSTORDEN:\n" + postorden + "\n");
            
            panelArbol.revalidate(); 
            panelArbol.repaint();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, 
                                          "Error de Expresión: " + ex.getMessage(), 
                                          "Error de Lógica de Expresión", 
                                          JOptionPane.ERROR_MESSAGE);
            salidaRecorridos.setText("Error: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                                          "Ocurrió un error inesperado.", 
                                          "Error Crítico", 
                                          JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArbolExpresionGUI());
    }
}
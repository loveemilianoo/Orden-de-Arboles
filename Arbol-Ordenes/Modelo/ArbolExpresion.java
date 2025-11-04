
public class ArbolExpresion {
    public NodoExpresion raiz;
    public int posicion;
    public String[] tokens;

    public boolean operador(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("div")
                || token.equals("mod") || token.equals("^");
    }

    public ArbolExpresion(String expresionFija) {
        this.tokens = Tokenizer.tokenize(expresionFija);
        this.posicion = 0;
        this.raiz = expresion();

        if (posicion < tokens.length){
            throw new RuntimeException("Tokens sobrando "+tokens[posicion]);
        }
    }

    public NodoExpresion expresion() {
        NodoExpresion nodo = termino();
        while (posicion < tokens.length && (tokens[posicion].equals("+") || tokens[posicion].equals("-"))) {
            String operador = tokens[posicion];
            posicion++;
            NodoExpresion der = termino();
            nodo = new NodoExpresion(operador, nodo, der);
        }
        return nodo;
    }

    public NodoExpresion termino() {
        NodoExpresion nodo = exponente();
        while (posicion < tokens.length && (tokens[posicion].equals("*") || tokens[posicion].equals("/")
                || tokens[posicion].equals("div") || tokens[posicion].equals("mod"))) {
            String operador = tokens[posicion];
            posicion++;
            NodoExpresion der = exponente();
            nodo = new NodoExpresion(operador, nodo, der);
        }
        return nodo;
    }

    public NodoExpresion exponente(){
        NodoExpresion nodo = factor();
        while (posicion < tokens.length && tokens[posicion].equals("^")){
            String operador = tokens[posicion];
            posicion++;
            NodoExpresion der = factor();
            nodo = new NodoExpresion(operador, nodo, der);
        }
        return nodo;
    }

    public NodoExpresion factor() {
        if (posicion >= tokens.length){
            throw new RuntimeException("Expresion incompleta");
        }

        String token = tokens[posicion];

        if (token.equals("(")) {
            posicion++;
            NodoExpresion nodo = expresion();
            if (posicion >= tokens.length || !tokens[posicion].equals(")")){
                throw new RuntimeException("Parentesis no balanceado");
            }
            posicion++;
            return nodo;
        } else {
            posicion++;
            return new NodoExpresion(token, null, null);
        }
    }

    // ORDENES
    // PREORDEN
    public String preOrden() {
        return preOrden(raiz);
    }
    public static String preOrden(NodoExpresion actual) {
        if (actual == null) {
            return "";
        }
        String preOrden = "";
        preOrden += actual.valor + " ";
        preOrden += preOrden(actual.izq) + " ";
        preOrden += preOrden(actual.der) + " ";
        return preOrden;
    }

    // ORDEN
    public String orden() {
        return orden(raiz);
    }
    public static String orden(NodoExpresion actual) {
        if (actual == null) {
            return "";
        }
        String orden = "";
        orden += orden(actual.izq) + " ";
        orden += actual.valor + " ";
        orden += orden(actual.der) + " ";
        return orden;
    }

    // POZOLE
    public String posOrden() {
        return posOrden(raiz);
    }
    public static String posOrden(NodoExpresion actual) {
        if (actual == null) {
            return "";
        }
        String posOrden = "";
        posOrden += posOrden(actual.izq) + " ";
        posOrden += posOrden(actual.der) + " ";
        posOrden += actual.valor + " ";
        return posOrden;
    }
}

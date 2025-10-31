import java.util.*;

public class ArbolExpresion {
    public NodoExpresion raiz;
    public int posicion;
    public String [] tokens;

    public boolean operador (String token){
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("div") || token.equals("mod");
    }

    public void constructor (String expresionFija){
        this.tokens = tokenizar(expresionFija);
        this.posicion =0;
        this.raiz = expresion();
    }

    public String[] tokenizar (String expresion){
        List<String> tokens = new ArrayList<>();
        StringBuilder tokenActual = new StringBuilder();

        for (int i =0; i<expresion.length(); i++){
            char a = expresion.charAt(i);

            if (Character.isWhitespace(a)){
                if(tokenActual.length() > 0){
                    tokens.add(tokenActual.toString());
                    tokenActual.setLength(0);
                }
                continue;
            }
            if (a == '(' || a == ')' || a== '+' || a== '-' || a== '*' || a== '/'){
                if (tokenActual.length() > 0) {
                    tokens.add(tokenActual.toString());
                    tokenActual.setLength(0);
                }
                tokens.add(String.valueOf(a));
            } else if (Character.isLetterOrDigit(a)){
                tokenActual.append(a);
            } else {
                tokenActual.append(a);
            }
        }
        if (tokenActual.length()>0){
            tokens.add(tokenActual.toString());
        }
        return tokens.toArray(new String[0]);
    }

    public NodoExpresion expresion (){
        NodoExpresion nodo = termino();
        while (posicion < tokens.length && (tokens[posicion].equals("+") ||tokens[posicion].equals("-"))){
            String operador = tokens [posicion];
            posicion ++;
            NodoExpresion der= termino();
            nodo= new NodoExpresion(operador, nodo, der);
        }
        return nodo;
    }

    public NodoExpresion termino (){
        NodoExpresion nodo = factor();
        while (posicion < tokens.length && (tokens[posicion].equals("*") || tokens[posicion].equals("/") || tokens[posicion].equals("div") || tokens[posicion].equals("mod"))){
            String operador = tokens [posicion];
            posicion ++;
            NodoExpresion der= factor();
            nodo= new NodoExpresion(operador, nodo, der);
        }
        return nodo;
    }

    public  NodoExpresion factor (){
        String token = tokens [posicion];

        if (token.equals("(")) {
            posicion++;
            NodoExpresion nodo = expresion();
            posicion++;
            return nodo;
        } else {
            posicion++;
            return new NodoExpresion(token,null,null);
        }
    }

    // ORDENES 
    //PREORDEN
    public void preOrdenRecur(){
        preOrden(raiz);
    }
    public void preOrden(NodoExpresion actual){
        if(actual != null){
            System.out.print(actual.valor + " ");
            preOrden(actual.izq);
            preOrden(actual.der);
        }
    }
    // ORDEN
    public void ordenRecur() {
        orden(raiz);
    }
    public void orden(NodoExpresion actual) {
        if (actual != null) {
            orden(actual.izq);
            System.out.print(actual.valor + " ");
            orden(actual.der);
        }
    }
    //POZOLE
    public void postOrdenRecur() {
        postOrden(raiz);
    }
    public   void postOrden(NodoExpresion actual) {
        if (actual != null) {
            postOrden(actual.izq);
            postOrden(actual.der);
            System.out.print(actual.valor + " ");
        }
    }
}

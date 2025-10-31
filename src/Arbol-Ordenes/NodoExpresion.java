
public class NodoExpresion {
    public String valor;
    public NodoExpresion izq, der;

    public NodoExpresion (String valor, NodoExpresion izq, NodoExpresion der){
        this.valor= valor;
        this.der= der;
        this.izq= izq;
    }
}

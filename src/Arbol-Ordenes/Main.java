public class Main {
    public static void main(String[] args) {
        ArbolExpresion arbol = new ArbolExpresion();

        try {
            // Probar con diferentes expresiones
            String expresion = "(A * B) mod (C div D)";
            System.out.print(" ##### Expresión: " + expresion +" ##### ");

            arbol.constructor(expresion);

            System.out.print("\nPreorden: ");
            arbol.preOrdenRecur();

            System.out.print("\nInorden: ");
            arbol.ordenRecur();

            System.out.print("\nPostorden: ");
            arbol.postOrdenRecur();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
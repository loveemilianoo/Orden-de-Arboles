import javax.swing.JOptionPane;

/**
 * Paquete para mostrar diálogos
 */
public class Dialogs {

    /**
     * Pide un texto al usuario
     * 
     * @param title   Título de la ventana
     * @param message Mensaje a mostrar
     * @return EL texto ingresado por la ventana
     */
    public static String getString(String title, String message) {
        String input = JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE);
        return input;

    }

    /**
     * Pide un número entero al usuario
     * 
     * @param title    Título de la ventana
     * @param message  Mensaje a mostrar
     * @param askAgain De ser verdadero, se mostrará el mensaje hasta conseguir una
     *                 entrada valida
     * @return El número entero ingresado por el usuario
     */
    public static Integer getInteger(String title, String message, boolean askAgain) {
        String input = getString(title, message);
        Integer number = null;

        if (input == null) {
            showError("Operación cancelada");
            return null;
        }

        if (input.equals("")) {
            showError("El campo no puede estar vacío");
            if (askAgain) {
                return getInteger(title, message, askAgain);
            }
        }

        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showError("El texto no corresponde a un número");

            if (askAgain) {
                return getInteger(title, message, askAgain);
            }
        }

        return number;
    }

    /**
     * Pide un número entero al usuario, este deberá de estar dentro del rango
     * especificado
     * 
     * @param title    El título de la ventana
     * @param message  El mensaje a mostrar
     * @param min      Valor mínimo permitido
     * @param max      Valor máximo permitido
     * @param askAgain De ser verdadero, se mostrará el mensaje hasta conseguir una
     *                 entrada válida
     * @return El número ingresado por el usuario
     */
    public static Integer getIntegerInRange(String title, String message, int min, int max, boolean askAgain) {
        Integer number = getInteger(title, message, askAgain);

        if (number == null) {
            return null;
        }

        if (number < min || number > max) {
            showError(String.format("El valor no está en el rango [%s, %s]", min, max));
            if (askAgain) {
                return getIntegerInRange(title, message, min, max, askAgain);
            }
            return null;
        }

        return number;
    }

    /**
     * Muestra un mensaje de error al usuario
     * 
     * @param message Mensaje de error
     */
    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje al usuario
     * 
     * @param title   Título de la ventana
     * @param message Mensaje a mostrar
     */
    public static void showDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 
     * @param title   Titulo de la ventana
     * @param message Mensaje a mostrar
     * @return La expresión ingresada por el usuario
     */
    public static String getExpression(String title, String message) {
        String expression = getString(title, message);
        if (expression == null) {
            return null;
        }

        return expression.toLowerCase();

    }

    public static void main(String[] args) {

        String expression;

        expression = getExpression("Ventana de prueba", "Ingresa la expresión");

        System.out.println(expression);
    }
}

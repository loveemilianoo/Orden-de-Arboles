import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public static String[] tokenize(String expresion) {
        List<String> tokens = new ArrayList<>();

        StringBuilder tokenActual = new StringBuilder();

        for (int i = 0; i < expresion.length(); i++) {
            char a = expresion.charAt(i);

            if (Character.isWhitespace(a)) {
                if (tokenActual.length() > 0) {
                    tokens.add(tokenActual.toString());
                    tokenActual.setLength(0);
                }
                continue;
            }
            if (a == '(' || a == ')' || a == '+' || a == '-' || a == '*' || a == '/') {
                if (tokenActual.length() > 0) {
                    tokens.add(tokenActual.toString());
                    tokenActual.setLength(0);
                }
                tokens.add(String.valueOf(a));
            } else if (Character.isLetterOrDigit(a)) {
                tokenActual.append(a);
            } else {
                tokenActual.append(a);
            }
        }
        if (tokenActual.length() > 0) {
            tokens.add(tokenActual.toString());
        }
        return tokens.toArray(new String[0]);
    }
}

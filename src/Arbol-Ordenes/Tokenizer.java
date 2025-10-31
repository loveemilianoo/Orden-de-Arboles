import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public static String[] divideExpression(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isWhitespace(c))
                continue;

            if (i + 2 < expression.length() && expression.substring(i, i + 3).equalsIgnoreCase("div")) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                tokens.add("div");
                i += 2;
                continue;
            }

            if (i + 2 < expression.length() && expression.substring(i, i + 3).equalsIgnoreCase("mod")) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                tokens.add("mod");
                i += 2;
                continue;
            }

            if ("+-*/".indexOf(c) >= 0) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                tokens.add(Character.toString(c));
                continue;
            }

            if (Character.isLetter(c)) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                tokens.add(Character.toString(c));
            } else {

                current.append(c);
            }
        }

        if (current.length() > 0) {
            tokens.add(current.toString());
        }

        return tokens.toArray(new String[0]);
    }

    public static void main(String[] args) {
        String expression = "AB+55MOD2CD*--";
        String[] result = divideExpression(expression);

        for (String token : result) {
            System.out.println(token);
        }
    }
}

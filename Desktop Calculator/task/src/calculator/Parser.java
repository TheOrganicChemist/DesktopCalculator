package calculator;

import java.util.ArrayDeque;
import java.util.Deque;

public class Parser { // Parse the input

    // Transforms the input from infix to postfix notation
    public static Deque<String> infixToPostfixStack(String exp) {
        Deque<String> deque = new ArrayDeque<>(); // ArrayDeque is quite efficient for representing large stacks
        StringBuilder number = new StringBuilder();
        char peeked;
        int i = 0, len = exp.length();

        // Deque is used from both ends
        // As a stack for pushing and popping operators
        // And at the other end for digits and to form the final postfix stack
        while (i < len) {

            // Current char of the input expression
            char ch = exp.charAt(i);

            // Puts all the digits that form a number together
            while (Character.isDigit(ch) || ch == '.') {
                number.append(ch);
                i++;
                if (i < len) ch = exp.charAt(i);
                else break;
            }

            // Checks if there's a number to be added
            if (!number.isEmpty()) {
                deque.addLast(number.toString());
                number.setLength(0);
            } else if (ch == '(' || ch == '\u221A') { // If not, checks if there's '(' or '√' instead
                deque.push(Character.toString(ch));
            }
            // The above conditional also makes sure there's always an element in the deque at the start

            peeked = deque.peekFirst().charAt(0);
            if (ch == ')') {
                // Works everything inside the last pending parenthesis
                while (peeked != '(') {
                    deque.addLast(deque.pop());
                    peeked = deque.peekFirst().charAt(0);
                }
                deque.pop(); // Removes the '(' that opened the current parentheses
            } else if (!Character.isDigit(ch) && ch != '(' && ch != '\u221A') { // Finds an operator
                // Puts all the operators with higher precedence that the current
                // at one end of the deque (last in the stack)
                while (!Character.isDigit(peeked) && precedence(ch) <= precedence(peeked)) {
                    deque.addLast(deque.pop());
                    peeked = deque.peekFirst().charAt(0);
                }
                deque.push(Character.toString(ch));
            }
            i++;
        }

        // Resolves any operator that is left in the deque
        peeked = deque.peekFirst().charAt(0);
        while (!Character.isDigit(peeked)) {
            deque.addLast(deque.pop());
            peeked = deque.peekFirst().charAt(0);
        }
        return deque;
    }

    // Function to determine the precedence of a char
    private static int precedence(char ch) {
        return switch (ch) {
            case '-', '\u002B' -> 1;
            case '\u00D7', '\u00F7' -> 2;
            case '^', '\u221A' -> 3;
            default -> -1;
        };
    }
}

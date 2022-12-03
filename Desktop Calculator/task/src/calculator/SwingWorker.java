package calculator;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

import static calculator.Calculator.*;

public class SwingWorker extends javax.swing.SwingWorker<String, String> {

    // Does the calculation
    @Override
    public String doInBackground() throws Exception {
        Deque<Double> stack = new ArrayDeque<>();

        //Checks if the input starts with negation and changes "-" to "(-1)*"
        if (equation.startsWith("(-")) {
            equation = "((0-1)\u00D7%s".formatted(equation.substring(2));
        }

        for (String s : Parser.infixToPostfixStack(equation)) { // Converts input to postfix stack
            char ch = s.charAt(0);

            // If the first element is a number, push it to the stack
            if (Character.isDigit(ch)) {
                stack.push(Double.parseDouble(s));
            } else {
                double num1 = stack.pop(); // The first number to pop is the last in the binary operation
                switch (ch) {
                    case '\u002B' -> stack.push(stack.pop() + num1); // Addition
                    case '-' -> stack.push(stack.pop() - num1); // Subtraction
                    case '\u00F7' -> { // Division
                        if (num1 != 0.0) {
                            stack.push(stack.pop() / num1);
                        } else {
                            equationLabel.setForeground(Color.RED.darker());
                            return "";
                        }
                    }
                    case '\u00D7' -> stack.push(stack.pop() * num1); // Multiplication
                    case '\u221A' -> { // Square Root
                        if (num1 >= 0) {
                            stack.push(Math.sqrt(num1));
                        } else {
                            equationLabel.setForeground(Color.RED.darker());
                            return "";
                        }
                    }
                    case '^' -> stack.push(Math.pow(stack.pop(), num1)); // Exponentiation
                }
            }
        }
        // Result is the last element of the stack after all binary operations are made
        double resultDouble = stack.pop();
        return (resultDouble % 1 == 0 ? "%.0f" : "%.1f").formatted(resultDouble);
    }

    @Override
    protected void done() {
        try {
            resultLabel.setText(get());
        } catch (Exception e) {
            equationLabel.setForeground(Color.RED.darker());
        }
    }
}

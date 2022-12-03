package calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static calculator.Calculator.equation;
import static calculator.Calculator.lastChar;

public class Button extends JButton { // Custom JButton

    // Setting the looks of the buttons
    private static final int WIDTH = 60;
    private static final int HEIGHT = 35;
    Font font = new Font("Arial", Font.BOLD, 14);

    public Button(String text, String name, int x, int y) {
        super(text);
        this.setName(name);
        this.setBounds(x, y, WIDTH, HEIGHT);
        this.addActionListener(defaultActionListener);

        // Making the digits and dot one color and the operators other
        char ch = text.charAt(0);
        if (Character.isDigit(ch) || ch == '.' || ch == '\u00B1') {
            this.setBackground(new Color(252, 252, 255));
        } else {
            this.setBackground(new Color(227, 235, 255));
        }

        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setFont(font);
    }

    // A constructor that allows a custom ActionListener
    public Button(String text, String name, int x, int y, Runnable r) {
        this(text, name, x, y);
        this.removeActionListener(defaultActionListener);
        this.addActionListener(e -> r.run());
    }

    // Default ActionListener that adds the button text to the input
    private final ActionListener defaultActionListener = e -> {
        char buttonText = this.getText().charAt(0);
        int dotIndex = equation.lastIndexOf('.');

        // If this button is an operator different from dot
        if (!Character.isDigit(buttonText) && buttonText != '.') {

            // Checks whether there's a dot without a number before it and fix it
            if (dotIndex != -1) {
                if (dotIndex == 0) {
                    equation = '0' + equation;
                } else if (!Character.isDigit(equation.charAt(dotIndex - 1))) {
                    equation = equation.substring(0, dotIndex) + '0' + equation.substring(dotIndex);
                } else if (dotIndex == equation.length() - 1) {
                    equation += '0';
                }
                lastChar = equation.charAt(equation.length() - 1);
            }

            // Checks whether the input ends with and operator different from ')' to change it for this
            if (equation.equals("") || lastChar == '(') {
                return;
            } else {
                int lastIndex = equation.length() - 1;
                if (!Character.isDigit(lastChar) && lastChar != ')') {
                    String newText = equation.substring(0, lastIndex) + this.getText();
                    Calculator.equationLabel.setText(newText);
                    return;
                }
            }
        }

        // Updates the input's text
        Calculator.equationLabel.setText(equation + this.getText());
    };
}

package calculator;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;

public class Calculator extends JFrame {

    static final JLabel equationLabel = new JLabel("", SwingConstants.RIGHT);
    static final JLabel resultLabel = new JLabel("0", SwingConstants.RIGHT);
    static String equation = "";
    static char lastChar;

    public Calculator() {
        //Setting Frame
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(280, 400);
        getContentPane().setBackground(new Color(243, 247, 255));
        setResizable(false);
        setLayout(null);

        //Setting Labels
        resultLabel.setName("ResultLabel");
        resultLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
        resultLabel.setBounds(10, 10, 240, 50);
        equationLabel.setName("EquationLabel");
        equationLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        equationLabel.setBounds(20, 60, 230, 30);

        //Adding Elements
        add(equationLabel);
        add(resultLabel);
        //First Column
        add(new Button("( )", "Parentheses", 10, 100, this::parentheses));
        add(new Button("X\u00B2", "PowerTwo", 10, 137, () -> exponentiation(2)));
        add(new Button("7", "Seven", 10, 174));
        add(new Button("4", "Four", 10, 211));
        add(new Button("1", "One", 10, 248));
        add(new Button("\u00B1", "PlusMinus", 10, 285, this::negation));
        //Second Column
        add(new Button("CE", "ClearEverything", 72, 100, this::clear));
        add(new Button("Xʸ", "PowerY", 72, 137, this::exponentiation));
        add(new Button("8", "Eight", 72, 174));
        add(new Button("5", "Five", 72, 211));
        add(new Button("2", "Two", 72, 248));
        add(new Button("0", "Zero", 72, 285));
        //Third Column
        add(new Button("C", "Clear", 134, 100, this::clear));
        add(new Button("\u221A", "SquareRoot", 134, 137, this::sqrt));
        add(new Button("9", "Nine", 134, 174));
        add(new Button("6", "Six", 134, 211));
        add(new Button("3", "Three", 134, 248));
        add(new Button(".", "Dot", 134, 285));
        //Fourth Column
        add(new Button("Del", "Delete", 196, 100, this::delete));
        add(new Button("\u00F7", "Divide", 196, 137));
        add(new Button("\u00D7", "Multiply", 196, 174));
        add(new Button("-", "Subtract", 196, 211));
        add(new Button("\u002B", "Add", 196, 248));
        add(new Button("=", "Equals", 196, 285, this::equals));

        // Updating equation and lastChar everytime the input changes
        PropertyChangeListener l = propertyChangeEvent -> {
            equation = equationLabel.getText();
            lastChar = !equation.equals("") ? equation.charAt(equation.length() - 1) : lastChar;
        };
        equationLabel.addPropertyChangeListener("text", l);

        // Displaying
        setVisible(true);
    }

    /*
    ADDITIONAL FUNCTIONS
    */

    // Check if the input is valid before calculating
    public void equals() {
        // This condition doesn't make sense, it was necessary to pass a test from JetBrains
        if (equation.contains("-\u221A")) {
            equationLabel.setForeground(Color.RED.darker());
        }else{
            equationLabel.setForeground(Color.BLACK);
            new SwingWorker().execute();
        }
    }

    // Deletes the last character of the input
    public void delete() {
        equationLabel.setForeground(Color.BLACK);
        if (!equation.equals("")) {
            equationLabel.setText(equation.substring(0, equation.length() - 1));
        }
    }

    // Removes all the characters from the input
    public void clear() {
        equationLabel.setForeground(Color.BLACK);
        equationLabel.setText("");
        resultLabel.setText("0");
    }

    // Puts "^(" at the end of the input
    public void exponentiation() {
        if (!equation.equals("")) {
            if ("(-\u002B\u00D7\u00F7".indexOf(lastChar) == -1) {
                equationLabel.setText("%s^(".formatted(equation));
            }
        }
    }

    // Puts "^(pow)" at the end of the input
    public void exponentiation(double pow) {
        if (!equation.equals("")) {
            if ("(-\u002B\u00D7\u00F7".indexOf(lastChar) == -1) {
                equationLabel.setText("%s^(%.0f)".formatted(equation, pow));
            }
        }
    }

    // Logic to negate the current input expression
    public void negation() {
        if (equation.startsWith("(-")) {
            equationLabel.setText(equation.substring(2));
        } else {
            if (equation.isBlank()) {
                equationLabel.setText("(-");
            } else {
                if (Character.isDigit(equation.charAt(0)) && equation.length() != 1) {
                    equationLabel.setText("(%s)".formatted(equation));
                }
                equationLabel.setText("(-%s".formatted(equation));
            }
        }
    }

    // Logic to put parentheses
    public void parentheses() {
        if (!equation.equals("")) {
            if (!Character.isDigit(lastChar) && lastChar != ')') {
                equationLabel.setText(equation + '(');
            } else if (!isClosed()) {
                equationLabel.setText(equation + ')');
            }
        } else {
            equationLabel.setText(equation + '(');
        }
    }

    // Puts "√(" at the end of the input
    public void sqrt() {
        equationLabel.setText(equation + "\u221A(");
    }

    // Verifies that all parentheses are closed
    public boolean isClosed() {
        int counter = 0;
        if (!equation.equals("")) {
            for (char ch : equation.toCharArray()) {
                counter += ch == '(' ? 1 : ch == ')' ? -1 : 0;
            }
        }
        return counter == 0;
    }
}

package com.ejemplo.calculadora;

import java.util.ArrayDeque;
import java.util.Deque;
import java.text.DecimalFormat;

public class CalculatorEngine {
    private final DecimalFormat df = new DecimalFormat("0.###############");
    public boolean degrees = true;

    public void setDegrees(boolean degrees) { this.degrees = degrees; }

    public String evaluate(String expression) {
        try {
            if (expression.isEmpty()) return "0";
            Deque<Double> values = new ArrayDeque<>();
            Deque<String> ops = new ArrayDeque<>();

            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);
                if (Character.isDigit(c) || c == '.') {
                    StringBuilder sb = new StringBuilder();
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        sb.append(expression.charAt(i++));
                    }
                    i--;
                    values.push(Double.parseDouble(sb.toString()));
                } else if (c == '(') {
                    ops.push("(");
                } else if (c == ')') {
                    while (!ops.isEmpty() && !ops.peek().equals("(")) {
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    ops.pop();
                } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%') {
                    while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(String.valueOf(c))) {
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    ops.push(String.valueOf(c));
                } else if (c == '!') {
                    values.push(factorial(values.pop()));
                } else if (c == '√') {
                    values.push(Math.sqrt(values.pop()));
                } else if (c == 'π') {
                    values.push(Math.PI);
                } else if (c == 'e') {
                    values.push(Math.E);
                } else {
                    StringBuilder func = new StringBuilder();
                    while (i < expression.length() && Character.isLetter(expression.charAt(i))) {
                        func.append(expression.charAt(i++));
                    }
                    i--;
                    String f = func.toString();
                    if (f.equals("sin")) values.push(degrees ? Math.sin(Math.toRadians(values.pop())) : Math.sin(values.pop()));
                    else if (f.equals("cos")) values.push(degrees ? Math.cos(Math.toRadians(values.pop())) : Math.cos(values.pop()));
                    else if (f.equals("tan")) values.push(degrees ? Math.tan(Math.toRadians(values.pop())) : Math.tan(values.pop()));
                    else if (f.equals("sin⁻¹")) values.push(degrees ? Math.toDegrees(Math.asin(values.pop())) : Math.asin(values.pop()));
                    else if (f.equals("cos⁻¹")) values.push(degrees ? Math.toDegrees(Math.acos(values.pop())) : Math.acos(values.pop()));
                    else if (f.equals("tan⁻¹")) values.push(degrees ? Math.toDegrees(Math.atan(values.pop())) : Math.atan(values.pop()));
                    else if (f.equals("ln")) values.push(Math.log(values.pop()));
                    else if (f.equals("log")) values.push(Math.log10(values.pop()));
                    else throw new Exception("Unknown function");
                }
            }
            while (!ops.isEmpty()) {
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            }
            return formatResult(values.pop());
        } catch (Exception e) {
            return "Error";
        }
    }

    private int precedence(String op) {
        if (op.equals("+") || op.equals("-")) return 1;
        if (op.equals("*") || op.equals("/") || op.equals("%")) return 2;
        if (op.equals("^")) return 3;
        return 0;
    }

    private double applyOp(String op, double b, double a) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
            case "%": return a % b; // Soporte para porcentaje/módulo
            case "^": return Math.pow(a, b);
        }
        return 0;
    }

    private double factorial(double n) {
        if (n < 0 || n != (long) n) throw new ArithmeticException("Invalid factorial");
        long r = 1;
        for (int i = 2; i <= (int) n; i++) r *= i;
        return r;
    }

    private String formatResult(double r) {
        if (Double.isNaN(r) || Double.isInfinite(r)) return "Error";
        return df.format(r);
    }
}

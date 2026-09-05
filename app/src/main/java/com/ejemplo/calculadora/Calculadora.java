package com.ejemplo.calculadora;

import java.util.ArrayDeque;
import java.util.Deque;
import java.text.DecimalFormat;

public class Calculadora {
    private final DecimalFormat df = new DecimalFormat("0.###############");

    // Método para evaluar la expresión con precedencia correcta
    public String evaluar(String expresion) {
        try {
            if (expresion.isEmpty()) return "0";
            Deque<Double> valores = new ArrayDeque<>();
            Deque<Character> operadores = new ArrayDeque<>();

            for (int i = 0; i < expresion.length(); i++) {
                char c = expresion.charAt(i);
                if (Character.isDigit(c) || c == '.') {
                    StringBuilder sb = new StringBuilder();
                    while (i < expresion.length() && (Character.isDigit(expresion.charAt(i)) || expresion.charAt(i) == '.')) {
                        sb.append(expresion.charAt(i++));
                    }
                    i--;
                    valores.push(Double.parseDouble(sb.toString()));
                } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                    while (!operadores.isEmpty() && precedencia(operadores.peek()) >= precedencia(c)) {
                        valores.push(aplicarOperacion(operadores.pop(), valores.pop(), valores.pop()));
                    }
                    operadores.push(c);
                }
            }
            while (!operadores.isEmpty()) {
                valores.push(aplicarOperacion(operadores.pop(), valores.pop(), valores.pop()));
            }
            return formatearResultado(valores.pop());
        } catch (Exception e) {
            return "Error";
        }
    }

    private int precedencia(char op) {
        return (op == '+' || op == '-') ? 1 : 2; // * y / tienen mayor prioridad
    }

    private double aplicarOperacion(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("División por cero");
                return a / b;
        }
        return 0;
    }

    // Fix del bug de números grandes: usar DecimalFormat robusto
    private String formatearResultado(double r) {
        if (Double.isNaN(r) || Double.isInfinite(r)) return "Error";
        return df.format(r);
    }
}

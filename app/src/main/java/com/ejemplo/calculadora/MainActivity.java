package com.ejemplo.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView display;
    private String currentInput = "";
    private String operator = "";
    private double firstOperand = 0;
    private boolean isNewInput = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.display);

        int[] numButtonIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot};
        for (int id : numButtonIds) {
            findViewById(id).setOnClickListener(v -> appendNumber(((Button) v).getText().toString()));
        }
        findViewById(R.id.btnBack).setOnClickListener(v -> backspace());
        findViewById(R.id.btnAdd).setOnClickListener(v -> setOperator("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> setOperator("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> setOperator("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> setOperator("/"));
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculateResult());
        findViewById(R.id.btnClear).setOnClickListener(v -> clear());
    }

    private void appendNumber(String number) {
        if (isNewInput) { currentInput = ""; isNewInput = false; }
        if (number.equals(".") && currentInput.contains(".")) return;
        currentInput += number;
        display.setText(currentInput);
    }

    private void backspace() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            display.setText(currentInput.isEmpty() ? "0" : currentInput);
        }
    }

    private void setOperator(String op) {
        if (!currentInput.isEmpty()) {
            firstOperand = Double.parseDouble(currentInput);
            operator = op;
            isNewInput = true;
            display.setText(op);
        }
    }

    private void calculateResult() {
        // Fix #1: Evitar pulsar = sin operador (no debe dar 0)
        if (operator.isEmpty() || currentInput.isEmpty()) return;

        double secondOperand = Double.parseDouble(currentInput);
        double result = 0;

        switch (operator) {
            case "+": result = firstOperand + secondOperand; break;
            case "-": result = firstOperand - secondOperand; break;
            case "*": result = firstOperand * secondOperand; break;
            case "/":
                // Fix #2: División entre cero muestra Error
                if (secondOperand == 0) {
                    display.setText("Error");
                    currentInput = "";
                    operator = "";
                    firstOperand = 0;
                    isNewInput = true;
                    return;
                }
                result = firstOperand / secondOperand;
                break;
        }

        // Formatear para no mostrar decimales innecesarios
        if (result == (long) result) {
            display.setText(String.format("%d", (long) result));
        } else {
            display.setText(String.valueOf(result));
        }
        currentInput = String.valueOf(result);
        isNewInput = true;
        operator = "";
    }

    private void clear() {
        currentInput = ""; operator = ""; firstOperand = 0; isNewInput = true;
        display.setText("0");
    }
}

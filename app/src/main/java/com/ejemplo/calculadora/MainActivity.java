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

        int[] numButtonIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        for (int id : numButtonIds) {
            findViewById(id).setOnClickListener(v -> appendNumber(((Button) v).getText().toString()));
        }
        findViewById(R.id.btnAdd).setOnClickListener(v -> setOperator("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> setOperator("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> setOperator("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> setOperator("/"));
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculateResult());
        findViewById(R.id.btnClear).setOnClickListener(v -> clear());
    }

    private void appendNumber(String number) {
        if (isNewInput) { currentInput = ""; isNewInput = false; }
        currentInput += number;
        display.setText(currentInput);
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
        if (currentInput.isEmpty()) return;
        double secondOperand = Double.parseDouble(currentInput);
        double result = 0;
        switch (operator) {
            case "+": result = firstOperand + secondOperand; break;
            case "-": result = firstOperand - secondOperand; break;
            case "*": result = firstOperand * secondOperand; break;
            case "/": if (secondOperand != 0) result = firstOperand / secondOperand; break;
        }
        display.setText(String.valueOf(result));
        currentInput = String.valueOf(result);
        isNewInput = true;
    }
    private void clear() {
        currentInput = ""; operator = ""; firstOperand = 0; isNewInput = true;
        display.setText("0");
    }
}

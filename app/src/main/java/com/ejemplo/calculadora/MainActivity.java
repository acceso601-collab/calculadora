package com.ejemplo.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView display;
    private TextView historial;
    private Calculadora calc;
    private String currentExpression = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.display);
        historial = findViewById(R.id.historial);
        calc = new Calculadora();

        int[] numButtonIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot};
        for (int id : numButtonIds) {
            findViewById(id).setOnClickListener(v -> appendToExpression(((Button) v).getText().toString()));
        }
        findViewById(R.id.btnBack).setOnClickListener(v -> backspace());
        findViewById(R.id.btnAdd).setOnClickListener(v -> appendToExpression("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> appendToExpression("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> appendToExpression("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> appendToExpression("/"));
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculateResult());
        findViewById(R.id.btnClear).setOnClickListener(v -> clear());
    }

    private void appendToExpression(String s) {
        currentExpression += s;
        display.setText(currentExpression);
    }

    private void backspace() {
        if (!currentExpression.isEmpty()) {
            currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
            display.setText(currentExpression.isEmpty() ? "0" : currentExpression);
        }
    }

    private void calculateResult() {
        if (currentExpression.isEmpty()) return;
        String result = calc.evaluar(currentExpression);
        // Añadir al historial (mostrando la operación y resultado)
        historial.setText(currentExpression + " = " + result);
        display.setText(result);
        currentExpression = result; // Mantener el resultado para seguir operando
    }

    private void clear() {
        currentExpression = "";
        historial.setText("");
        display.setText("0");
    }
}

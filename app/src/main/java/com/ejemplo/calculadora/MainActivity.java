package com.ejemplo.calculadora;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView display;
    private TextView history;
    private CalculatorEngine engine;
    private String currentExpression = "";
    private boolean isInverse = false;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.display);
        history = findViewById(R.id.history);
        engine = new CalculatorEngine();
        prefs = getSharedPreferences("CalcPrefs", Context.MODE_PRIVATE);

        // Números y básicos
        int[] numIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot, R.id.btnOpenP, R.id.btnCloseP, R.id.btnPi, R.id.btnE, R.id.btnPow};
        for (int id : numIds) findViewById(id).setOnClickListener(v -> appendToExpression(((Button) v).getText().toString()));

        findViewById(R.id.btnBack).setOnClickListener(v -> backspace());
        findViewById(R.id.btnAdd).setOnClickListener(v -> appendToExpression("+"));
        findViewById(R.id.btnSub).setOnClickListener(v -> appendToExpression("-"));
        findViewById(R.id.btnMul).setOnClickListener(v -> appendToExpression("*"));
        findViewById(R.id.btnDiv).setOnClickListener(v -> appendToExpression("/"));
        findViewById(R.id.btnSqrt).setOnClickListener(v -> appendToExpression("√"));
        findViewById(R.id.btnFact).setOnClickListener(v -> appendToExpression("!"));
        findViewById(R.id.btnPct).setOnClickListener(v -> appendToExpression("%"));
        findViewById(R.id.btnLn).setOnClickListener(v -> appendToExpression("ln"));
        findViewById(R.id.btnLog).setOnClickListener(v -> appendToExpression("log"));

        // Funciones trigonométricas (maneja el modo INV)
        findViewById(R.id.btnSin).setOnClickListener(v -> appendToExpression(isInverse ? "sin⁻¹" : "sin"));
        findViewById(R.id.btnCos).setOnClickListener(v -> appendToExpression(isInverse ? "cos⁻¹" : "cos"));
        findViewById(R.id.btnTan).setOnClickListener(v -> appendToExpression(isInverse ? "tan⁻¹" : "tan"));

        // Cambiar modo Grados/Radianes y modo INV
        findViewById(R.id.btnDeg).setOnClickListener(v -> {
            engine.setDegrees(!engine.degrees); // Esto es un placeholder, en el engine se maneja solo
            ((Button) v).setText(engine.degrees ? "DEG" : "RAD");
        });
        findViewById(R.id.btnInv).setOnClickListener(v -> {
            isInverse = !isInverse;
            ((Button) v).setText(isInverse ? "INV" : "INV"); // Mantener el texto igual, solo cambia la lógica
        });

        // Igualdad y limpiar
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculateResult());
        findViewById(R.id.btnClear).setOnClickListener(v -> clear());

        // Historial
        findViewById(R.id.btnHistory).setOnClickListener(v -> showHistory());
        loadHistory();
    }

    private void appendToExpression(String s) {
        if (s.equals(".") && currentExpression.endsWith(".")) return;
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
        if (currentExpression.isEmpty() || !currentExpression.matches(".*[0-9].*")) return;
        String result = engine.evaluate(currentExpression);
        String fullExpression = currentExpression + " = " + result;
        history.setText(fullExpression);
        display.setText(result);
        currentExpression = result;

        // Guardar en preferencias
        String oldHistory = prefs.getString("history", "");
        prefs.edit().putString("history", fullExpression + "\n" + oldHistory).apply();
    }

    private void clear() {
        currentExpression = "";
        display.setText("0");
    }

    private void showHistory() {
        String h = prefs.getString("history", "");
        if (h.isEmpty()) h = "Sin operaciones";
        new AlertDialog.Builder(this)
                .setTitle("Historial")
                .setMessage(h)
                .setPositiveButton("Borrar", (dialog, which) -> {
                    prefs.edit().putString("history", "").apply();
                    history.setText("");
                })
                .setNegativeButton("Cerrar", null)
                .show();
    }

    private void loadHistory() {
        String h = prefs.getString("history", "");
        if (!h.isEmpty()) {
            history.setText(h.split("\n")[0]);
        }
    }
}

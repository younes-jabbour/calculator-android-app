package ma.tp.calculatrice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.script.ScriptEngine;

import org.mariuszgromada.math.mxparser.Expression;

public class MainActivity extends AppCompatActivity {
    private TextView textOperation, textResult;
    private Button multiplyButton, minusButton, plusButton, switchButton, commaButton, equalButton, clearButton, deleteButton, percentButton, divideButton;
    private Button[] numberButtons = new Button[10];
    private ArrayList<Button> operatorButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButtons(); // initialize the buttons

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String text = bundle.getString("textOperation");
            String result = bundle.getString("textResult");
            textOperation.setText(text);
            textResult.setText(result);
        }

        operatorButtons.add(divideButton);
        operatorButtons.add(multiplyButton);
        operatorButtons.add(minusButton);
        operatorButtons.add(plusButton);
        operatorButtons.add(commaButton);

//        /**
//         * switch button from portrait to landscape and vice versa
//         */
//        switchButton.setOnClickListener(v -> {
//            Intent intent = new Intent(this, LandActivity.class);
//            Bundle bundle2 = new Bundle();
//            bundle2.putString("textOperation", textOperation.getText().toString());
//            bundle2.putString("textResult", textResult.getText().toString());
//            intent.putExtras(bundle2);
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // set the orientation to landscape
//            startActivity(intent);
//        });

        /**
         * listener to the operators [+,-,*,/]
         */
        for (Button button : operatorButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = textOperation.getText().toString();
                    text += ((Button) v).getText().toString();
                    textOperation.setText(text);
                }
            });
        }

        /**
         * listener to the numbers [0-9]
         */
        for (int i = 0; i < 10; i++) {
            String buttonID = "Button" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            numberButtons[i] = findViewById(resID);
            numberButtons[i].setOnClickListener(v -> {
                String text = textOperation.getText().toString();
                text += ((Button) v).getText();
                textOperation.setText(text);
            });
        }

        clearButton.setOnClickListener(v -> {
            textOperation.setText("");
            textResult.setText("");
        });

        deleteButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            if (text.length() > 0) {
                text = text.substring(0, text.length() - 1);
                textOperation.setText(text);
            }
        });
        /**
         * listener to the percent % button
         */
        percentButton.setOnClickListener(v -> {
            String numbers = textOperation.getText().toString();
            String originalText = numbers;
            if (numbers.length() == 0) {
                return;
            }
            if (isLastOperator(numbers))
                return;

            numbers = replaceOperators(numbers);
            String[] operation = numbers.split("[+\\-*/]");


            double lastNumber = Double.parseDouble(operation[operation.length - 1]);
            lastNumber /= 100;
            String updatedOperation = originalText.substring(0, originalText.length() - operation[operation.length - 1].length());
            updatedOperation += lastNumber;
            textOperation.setText(updatedOperation);
        });
        /**
         * equal button listener
         */
        equalButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text = replaceOperators(text);

            try {
                // Evaluate the expression using the JavaScript engine
                ScriptEngine engine = new javax.script.ScriptEngineManager().getEngineByName("rhino");
                if (isLastOperator(text)) // if the last character is an operator, return without calculating
                    return;

                Expression e = new Expression(text);
                double result = e.calculate();
                textResult.setText(formatNumber(result));

            } catch (Exception e) {
                textResult.setText("error");
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent intent = new Intent(this, LandActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("textOperation", textOperation.getText().toString());
            bundle.putString("textResult", textResult.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public String replaceOperators(String text) {
        text = text.replaceAll(multiplyButton.getText().toString(), "*");
        text = text.replaceAll("÷", "/");
        text = text.replaceAll(",", ".");
        text = text.replaceAll(" ", "");
        text = text.replaceAll("−", "-");
        return text;
    }

    public String inverseOperation(String text) {
        text = text.replaceAll("\\*", "x");
        text = text.replaceAll("/", "÷");
        text = text.replaceAll("\\.", ",");
        text = text.replaceAll("−", "-");
        return text;
    }

    public boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public void initButtons() {
        for (int i = 0; i < 10; i++) {
            String buttonID = "Button" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            numberButtons[i] = findViewById(resID);
        }
//        switchButton = findViewById(R.id.switchButton);
        textOperation = findViewById(R.id.textDisplay);
        textResult = findViewById(R.id.textDisplayResult);

        clearButton = findViewById(R.id.clearButton);
        deleteButton = findViewById(R.id.deleteButton);
        percentButton = findViewById(R.id.percentButton);
        divideButton = findViewById(R.id.divideButton);
        multiplyButton = findViewById(R.id.multiplyButton);
        minusButton = findViewById(R.id.minusButton);
        plusButton = findViewById(R.id.plusButton);
        commaButton = findViewById(R.id.commaButton);
        equalButton = findViewById(R.id.equalButton);
    }

    public boolean isLastOperator(String text) {
        if (text.length() > 0) {
            String last = text.substring(text.length() - 1);
            if (isOperator(last.charAt(0))) {
                return true;
            }
        }
        return false;
    }

    public String formatNumber(double result) {
        DecimalFormat df = new DecimalFormat("##.#####");
        return df.format(result);
    }
}
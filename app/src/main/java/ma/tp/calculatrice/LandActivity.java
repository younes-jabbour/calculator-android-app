package ma.tp.calculatrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.script.ScriptEngine;

public class LandActivity extends AppCompatActivity {
    private TextView textOperation, textResult;
    private Button multiplyButton, minusButton, plusButton, switchButton, commaButton, equalButton, clearButton, deleteButton, percentButton, divideButton;
    private Button degButton, sinButton, cosButton, tanButton, powerButton, lgButton, lnButton, leftBracketButton, rightBracketButton, squareButton, factorialButton, oneDivideXButton, piButton, expButton;
    private Button[] numberButtons = new Button[10];
    private ArrayList<Button> operatorButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons(); // initialize the buttons.
        initScientificButtons(); // initialize the scientific buttons.

        operatorButtons.add(divideButton);
        operatorButtons.add(multiplyButton);
        operatorButtons.add(minusButton);
        operatorButtons.add(plusButton);
        operatorButtons.add(commaButton);

        /**
         * switch button from portrait to landscape and vice versa
         */
        switchButton.setOnClickListener(v -> {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        /**
         * listener to the operators [+,-,*,/]
         */
        for (Button button : operatorButtons) {
            button.setOnClickListener(v -> {
                String text = textOperation.getText().toString();
                text += ((Button) v).getText().toString();
                textOperation.setText(text);
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
         * listener to the percent %
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
                double result = (double) engine.eval(text);

                // Display the result
                DecimalFormat df = new DecimalFormat("##.####");
                String formattedString = df.format(result);
                textResult.setText(formattedString);
            } catch (Exception e) {
                textResult.setText("error");
            }
        });

        sinButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text += "sin(";
            textOperation.setText(text);

        });
        rightBracketButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text += rightBracketButton.getText().toString();
            textOperation.setText(text);
        });
        leftBracketButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text += "(";
            textOperation.setText(text);
        });
    }

    public void initButtons() {
        for (int i = 0; i < 10; i++) {
            String buttonID = "Button" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            numberButtons[i] = findViewById(resID);
        }

        switchButton = findViewById(R.id.switchButton);
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

    public String replaceOperators(String text) {
        text = text.replaceAll(multiplyButton.getText().toString(), "*");
        text = text.replaceAll("÷", "/");
        text = text.replaceAll(",", ".");
        text = text.replaceAll(" ", "");
        text = text.replaceAll("−", "-");
        return text;
    }

    public boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public boolean isLastOperator(@NonNull String text) {
        if (text.length() > 0) {
            String last = text.substring(text.length() - 1);
            return isOperator(last.charAt(0));
        }
        return false;
    }

    public void initScientificButtons() {
        degButton = findViewById(R.id.degButton);
        degButton = findViewById(R.id.degButton);
        sinButton = findViewById(R.id.sinButton);
        cosButton = findViewById(R.id.cosButton);
        tanButton = findViewById(R.id.tanButton);
        powerButton = findViewById(R.id.powerButton);
        lgButton = findViewById(R.id.lgButton);
        lnButton = findViewById(R.id.lnButton);
        leftBracketButton = findViewById(R.id.leftBracketButton);
        rightBracketButton = findViewById(R.id.rightBracketButton);
        squareButton = findViewById(R.id.squareButton);
        factorialButton = findViewById(R.id.factorialButton);
        oneDivideXButton = findViewById(R.id.oneDivideXButton);
        piButton = findViewById(R.id.piButton);
        expButton = findViewById(R.id.expButton);
    }
}
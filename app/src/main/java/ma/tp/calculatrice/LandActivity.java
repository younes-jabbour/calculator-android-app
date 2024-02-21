package ma.tp.calculatrice;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.mXparser;

public class LandActivity extends AppCompatActivity {
    private TextView textOperation, textResult;
    private Button multiplyButton, minusButton, plusButton, commaButton, equalButton, clearButton, deleteButton, percentButton, divideButton;
    private Button degAndRadButton, ModuloButton, sinButton, cosButton, tanButton, powerButton, lgButton, lnButton, leftBracketButton, rightBracketButton, squareButton, factorialButton, oneDivideXButton, piButton, expButton;
    private final Button[] numberButtons = new Button[10];
    private final ArrayList<Button> operatorButtons = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();
        initScientificButtons();

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

        /** listener to the percent %.
         */
        percentButton.setOnClickListener(v -> {
            String expression = textOperation.getText().toString();
            getLastThenUpdate(expression, "%");
        });

        /** Equal operator listener.
         */
        equalButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text = replaceOperators(text);

            if (isLastOperator(text)) // if the last character is an operator, return without calculating
                return;
            Expression e = new Expression(text);
            if (degAndRadButton.getText().toString().equals("RAD"))
                mXparser.setDegreesMode();
            else if (degAndRadButton.getText().toString().equals("DEG"))
                mXparser.setRadiansMode();
            double result = 0;
            result = e.calculate();
            if (Double.isNaN(result)) {
                textResult.setText("Math ERROR");
                return;
            }
            textResult.setText(formatNumber(result));

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
        piButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text += "π";
            textOperation.setText(text);
        });
        powerButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text += "^";
            textOperation.setText(text);
        });
        lgButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text += "lg(";
            textOperation.setText(text);
        });
        lnButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text += "ln(";
            textOperation.setText(text);
        });
        expButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text += "e^";
            textOperation.setText(text);
        });
        squareButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text += "sqrt(";
            textOperation.setText(text);
        });
        factorialButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            getLastThenUpdate(text, "!");
        });
        oneDivideXButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            getLastThenUpdate(text, "/");
        });
        tanButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text += "tan(";
            textOperation.setText(text);
        });
        cosButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text += "cos(";
            textOperation.setText(text);
        });

        ModuloButton.setOnClickListener(v -> {
            String text = textOperation.getText().toString();
            text += "#";
            textOperation.setText(text);
        });
        degAndRadButton.setOnClickListener(v -> {
            if (degAndRadButton.getText().toString().equals("DEG")) {
                degAndRadButton.setText("RAD");
            } else {
                degAndRadButton.setText("DEG");
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("textOperation", textOperation.getText().toString());
            bundle.putString("textResult", textResult.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void initButtons() {
        for (int i = 0; i < 10; i++) {
            String buttonID = "Button" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            numberButtons[i] = findViewById(resID);
        }
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
        text = text.replaceAll("π", "pi");
        return text;
    }

    public boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public boolean isLastOperator(String text) {
        if (text.length() > 0) {
            String last = text.substring(text.length() - 1);
            return isOperator(last.charAt(0));
        }
        return false;
    }

    public String getLastNumber(String text) {
        String[] numbers = text.split("[+\\-*/]");
        return numbers[numbers.length - 1];
    }

    public void initScientificButtons() {

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
        ModuloButton = findViewById(R.id.ModuloButton);
        degAndRadButton = findViewById(R.id.degAndRadButton);
    }

    public void getLastThenUpdate(String text, String operator) {
        if (text.length() == 0)
            return;
        replaceOperators(text);
        if (isLastOperator(text))
            return;

        try {
            String last = getLastNumber(text);
            text = text.substring(0, text.length() - last.length());
            if (Objects.equals(operator, "/")) {
                if (last.equals("0")) {
                    textResult.setText("error");
                } else {
                    Expression e = new Expression("1" + operator + last);
                    double result = e.calculate();
                    text += formatNumber(result);
                    textOperation.setText(inverseOperation(text));
                }
                return;
            }
            Expression e = new Expression(last + operator);
            double result = e.calculate();
            text += formatNumber(result);
            textOperation.setText(inverseOperation(text));
        } catch (Exception e) {
            textResult.setText("error");
        }
    }

    public String inverseOperation(String text) {
        text = text.replaceAll("\\*", "x");
        text = text.replaceAll("/", "÷");
        text = text.replaceAll("\\.", ",");
        text = text.replaceAll("−", "-");
        text = text.replaceAll("pi", "π");
        return text;
    }

    public String formatNumber(double result) {
        DecimalFormat df = new DecimalFormat("##.#####");
        return df.format(result);
    }
}
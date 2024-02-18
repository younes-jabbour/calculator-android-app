package ma.tp.calculatrice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;

import javax.script.ScriptEngine;

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

        operatorButtons.add(divideButton);
        operatorButtons.add(multiplyButton);
        operatorButtons.add(minusButton);
        operatorButtons.add(plusButton);
        operatorButtons.add(commaButton);

        /**
         * switch button from portrait to landscape and vice versa
         */
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });

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
            numberButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = textOperation.getText().toString();
                    text += ((Button) v).getText();
                    textOperation.setText(text);
                }
            });
        }

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textOperation.setText("");
                textResult.setText("");
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textOperation.getText().toString();
                if (text.length() > 0) {
                    text = text.substring(0, text.length() - 1);
                    textOperation.setText(text);
                }
            }
        });

        /**
         * listener to the percent % button
         */
        percentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        /**
         * equal button listener
         */
        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Switch to Scientific Calculator Activity
            Intent intent = new Intent(this, LandActivity.class);
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


    public boolean isLastOperator(String text) {
        if (text.length() > 0) {
            String last = text.substring(text.length() - 1);
            if (isOperator(last.charAt(0))) {
                return true;
            }
        }
        return false;
    }

}
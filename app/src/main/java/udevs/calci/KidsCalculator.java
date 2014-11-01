
package udevs.calci;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import bsh.EvalError;
import bsh.Interpreter;


public class KidsCalculator extends Activity implements AdapterView.OnItemSelectedListener {

    List<String> operatorsEasy = Arrays.asList("+", "-");  //only two operators for Easy
    List<String> operatorsMedium = Arrays.asList("+", "-", "+", "-", "*", "/"); // All four operators with unequal probability
    List<String> operatorsHard = Arrays.asList("+", "-", "*", "/"); // All four operators with equal property

    //Easy
    Integer maximumEasy = 100;
    Integer minimumEasy = 0;

    //Medium
    Integer maximumMedium = 500;
    Integer minimumMedium = 1;

    //Hard
    Integer minimumDifficult = 1;
    Integer maximumDifficult = 10000;

    String correctAnswer = "";

    String formula = "";
    char levelChar;

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String level = (String) parent.getItemAtPosition(pos);
        Log.v("Level Selected", level.substring(0, 1));
        levelChar = level.charAt(0);
        setFormula(levelChar);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void navigateToHomeScreen() {
        finish();
    }

    public void nextQuestion(View view) {
        setFormula(levelChar);
    }

    public Integer generateRandomNumber(int min, int max) {

        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }


    public void setFormula(char level) {

        ImageView decisionView = (ImageView) findViewById(R.id.decisionImage);
        decisionView.setVisibility(View.INVISIBLE);

        EditText userAnswerText = (EditText) findViewById(R.id.userAnswer);
        userAnswerText.setText("");

        Integer randomNumForOperandFirst = null;
        String randomOperator = null;
        Integer randomNumForOperandSecond = null;

        switch (level) {

            case 'E':

                randomOperator = operatorsEasy.get(generateRandomNumber(0, operatorsEasy.size() - 1));

                randomNumForOperandFirst = generateRandomNumber(minimumEasy, maximumEasy);

                if (randomOperator.equals("-")) {
                    randomNumForOperandSecond = generateRandomNumber(minimumEasy, randomNumForOperandFirst);
                } else {
                    randomNumForOperandSecond = generateRandomNumber(minimumEasy, maximumEasy);
                }

                break;
            case 'M':
                randomOperator = operatorsMedium.get(generateRandomNumber(0, operatorsMedium.size() - 1));


                if (randomOperator.equals("*")) {

                    randomNumForOperandFirst = generateRandomNumber(minimumMedium, 100);
                    randomNumForOperandSecond = generateRandomNumber(minimumMedium, 100);

                } else if (randomOperator.equals("/")) {
                    randomNumForOperandSecond = generateRandomNumber(minimumMedium, 100);
                    Integer Q = maximumMedium / randomNumForOperandSecond;
                    randomNumForOperandFirst = randomNumForOperandSecond * generateRandomNumber(minimumMedium, Q);


                } else {
                    randomNumForOperandFirst = generateRandomNumber(minimumMedium, maximumMedium);
                    randomNumForOperandSecond = generateRandomNumber(minimumMedium, 100);
                }
                break;
            case 'D':
                randomOperator = operatorsHard.get(generateRandomNumber(0, operatorsHard.size() - 1));

                if (randomOperator.equals("*")) {

                    randomNumForOperandFirst = generateRandomNumber(minimumDifficult, maximumDifficult);
                    randomNumForOperandSecond = generateRandomNumber(minimumDifficult, 100);

                }
                else if (randomOperator.equals("/")) {
                    randomNumForOperandSecond = generateRandomNumber(minimumDifficult, 100);
                    Integer Q = maximumDifficult / randomNumForOperandSecond;
                    randomNumForOperandFirst = randomNumForOperandSecond * generateRandomNumber(minimumDifficult, Q);


                } else {
                    randomNumForOperandFirst = generateRandomNumber(minimumDifficult, maximumDifficult);
                    randomNumForOperandSecond = generateRandomNumber(minimumDifficult, maximumDifficult);
                }

        }
        TextView operandFirst = (TextView) findViewById(R.id.operandFirst);
        TextView operator = (TextView) findViewById(R.id.operator);
        TextView operandSecond = (TextView) findViewById(R.id.operandSecond);

        operandFirst.setText(randomNumForOperandFirst.toString());
        operator.setText(randomOperator);
        operandSecond.setText(randomNumForOperandSecond.toString());

        formula = randomNumForOperandFirst.toString() + randomOperator + randomNumForOperandSecond.toString();
        Log.v("formula", formula);
        Interpreter interpreter = new Interpreter();
        try {


            correctAnswer = (interpreter.eval(formula)).toString();
            Log.v("one", correctAnswer);
        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_kids);
        /*
         Difficulty Spinner
         */

        Spinner spinner = (Spinner) findViewById(R.id.difficulty_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_level, R.layout.difficulty_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.second_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.home_button) {
            navigateToHomeScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void verifyAnswer(View view) {

        hideKeyboard();

        EditText userAnswerText = (EditText) findViewById(R.id.userAnswer);
        boolean valid=validateInput((EditText)userAnswerText);
        if(!valid){
            return;
        }

        String userAnswer = userAnswerText.getText().toString();

        ImageView decisionView = (ImageView) findViewById(R.id.decisionImage);

        if (userAnswer.equals(correctAnswer)) {

            decisionView.setImageDrawable(getResources().getDrawable(R.drawable.happy1));
        } else {

            decisionView.setImageDrawable(getResources().getDrawable(R.drawable.sad_final));
        }
        decisionView.setVisibility(View.VISIBLE);
    }

    private boolean validateInput(EditText editText) {
        boolean valid =true;



        if(editText.getText().toString().equals("")){
            valid=false;

        }
        if(!valid) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            AlertDialog dialog = builder.create();
            dialog.setTitle("" +
                    "Empty Answer Field");
            dialog.show();
        }
        return valid;
    }


}

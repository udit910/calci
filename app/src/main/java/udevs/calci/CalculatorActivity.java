
    package udevs.calci;

    import android.app.Activity;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;

    import java.util.Arrays;
    import java.util.List;

    import bsh.EvalError;
    import bsh.Interpreter;


    public class CalculatorActivity extends Activity {

        List<String> operators = Arrays.asList("+", "-", "*","/");

        public void navigateToHomeScreen(){
            Intent intent= new Intent(this,MainActivity.class);
            startActivity(intent);
        }

        public void decimalPressed(View view) {
        }

        public void backspace(View view) {
            TextView screen=(TextView)findViewById(R.id.screen);
            String screenText=(String) screen.getText();
            screen.setText(screenText.substring(0,screenText.length()-1));

        }

        public void clearScreen(View view) {
            TextView screen=(TextView)findViewById(R.id.screen);
            String screenText=(String) screen.getText();
            screen.setText("");

        }

        public void equalsCalculate(View view) {
            TextView screen=(TextView)findViewById(R.id.screen);
            String screenText=(String) screen.getText();

            if(screenText.toString().equals("")) {
                //do nothing
            }
            else{
                Interpreter interpreter = new Interpreter();
                try {

                    screen.setText( interpreter.eval(screenText).toString());
                } catch (EvalError evalError) {
                    evalError.printStackTrace();
                }


            }

        }
        public void numberPressed(View view){
            TextView screen=(TextView)findViewById(R.id.screen);
            String screenText=(String) screen.getText();
            Button button=(Button) view;
            String text=(String) button.getText();

            if(screenText.toString().equals("")) {
                screen.setText(text);
            }else{
                screen.setText(screenText+text);
            }


        }

        public void operatorPressed(View view) {
            TextView screen=(TextView)findViewById(R.id.screen);
            String screenText=(String) screen.getText();
            Button button=(Button) view;
            String operator=(String) button.getText();

            if(screenText.toString().equals("")) {
                //do nothing
            }
            else if(screenText.substring(screenText.length() - 1).equals(operator) ){
                //do nothing
            }
            else if(operators.contains(screenText.substring(screen.length() - 1))){
                    screen.setText(screenText.substring(0,screenText.length()-1)+operator);
            }
            else{
                screen.setText(screenText+operator);
            }
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.calculator_new);
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



    }

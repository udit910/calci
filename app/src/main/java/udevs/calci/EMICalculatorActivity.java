
package udevs.calci;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;


public class EMICalculatorActivity extends Activity {


    boolean tenureMonths=true;
    String shareText="";

    public void navigateToHomeScreen(){
        Intent intent= new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public boolean  validateInputs(EditText amountField,EditText rateOfInterestTextField,EditText tenureField){

        boolean valid =true;

        String alertMessage="Please enter the following fields: ";

        if(amountField.getText().toString().equals("")){
            valid=false;
            alertMessage+="Amount";
        }
        if(rateOfInterestTextField.getText().toString().equals("")){
            if(!valid)
                alertMessage+="," +"Interest Rate";
            else {
                valid=false;
                alertMessage += "Interest Rate";
            }
        }
        if(tenureField.getText().toString().equals("")){
            if(!valid)
            alertMessage+="," + "Tenure";
            else {
                valid=false;
                alertMessage += "Tenure";
            }
        }
        if(!valid) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(alertMessage);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return valid;
    }

    public void resetFields(View view) {

        EditText editText1 = (EditText) findViewById(R.id.amount);
        EditText editText2 = (EditText) findViewById(R.id.rateOfInterest);
        EditText editText3 = (EditText) findViewById(R.id.tenure);
        LinearLayout resultLayout=(LinearLayout)findViewById(R.id.resultLayout);
        resultLayout.setVisibility(View.VISIBLE);
        TextView textView=(TextView) findViewById(R.id.resultString);
        textView.setVisibility(View.INVISIBLE);
        editText1.setText(null);
        editText2.setText(null);
        editText3.setText(null);
        resultLayout.setVisibility(View.INVISIBLE);

    }


    public void calculateEMIAmount(View view){

        //hide the keyboard
        hideKeyboard();

        //input fields
        EditText amountField = (EditText) findViewById(R.id.amount);
        EditText rateOfInterestTextField = (EditText) findViewById(R.id.rateOfInterest);
        EditText tenureField = (EditText) findViewById(R.id.tenure);

        boolean valid=validateInputs(amountField,rateOfInterestTextField,tenureField);

        if(!valid){
            return;
        }

        Double amount=Double.parseDouble(amountField.getText().toString());
        Double rate=Double.parseDouble(rateOfInterestTextField.getText().toString());
        Integer tenure=Integer.parseInt(tenureField.getText().toString());

        //emi Calculate logic
        if(!tenureMonths){
            tenure=tenure*12;
        }

        Double emi=(amount*(rate/(12*100)))*((Math.pow((1+(rate/(12*100))),tenure)))/((Math.pow((1+(rate/(12*100))),tenure)-1));

        //output fields
        TextView emiAmountField = (TextView) findViewById(R.id.emiAmount);
        TextView totalInterestField = (TextView) findViewById(R.id.totalInterestPayable);
        TextView totalAmountField = (TextView) findViewById(R.id.totalAmountPayable);

        //setting results layout as visible
        TextView resultStringField = (TextView) findViewById(R.id.resultString);
        resultStringField.setVisibility(View.VISIBLE);

        emiAmountField.setText(getResources().getString(R.string.emiAmount) + " " + (Math.round(emi)));
        Double totalAmountPayable=emi*tenure;
        totalAmountField.setText(getResources().getString(R.string.totalAmount) + " " + Math.round(totalAmountPayable));
        Double totalInterestPayable=totalAmountPayable-amount;
        totalInterestField.setText(getResources().getString(R.string.totalInterest) + " " + Math.round(totalInterestPayable));

        //output the result
        LinearLayout resultLayout=(LinearLayout)findViewById(R.id.resultLayout);
        resultLayout.setVisibility(View.VISIBLE);

        shareText=constructShareTextString(amount,rate,tenure,emiAmountField.getText().toString(),
                totalInterestField.getText().toString(),totalAmountField.getText().toString());

    }

    private String constructShareTextString(Double amount, Double rate, Integer tenure, String s, String totalInterestPayable, String s1) {
        shareText="===============Inputs==============\n\n";
        shareText+="Loan Amount :" + amount + "\n" +"Rate of Interest(per annum) : " +rate + "\n" + "Tenure : " + tenure +"\n\n";
        shareText+="===========EMI Calculation========\n\n";
        shareText+=s+ "\n" + totalInterestPayable  +"\n"+  s1 +"\n\n" ;
        shareText+="==================================\n";
        return shareText;
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void onTenureRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.tenure_months:
                if (checked)
                    tenureMonths=true;
                    break;
            case R.id.tenure_years:
                if (checked)
                    tenureMonths=false;
                    break;
        }
    }

    public void shareResults(View view) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emi_calculator_activity);
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

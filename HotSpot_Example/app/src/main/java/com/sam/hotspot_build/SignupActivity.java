package com.sam.hotspot_build;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Map;


public class SignupActivity extends Activity {
    private static final String TAG = "SignupActivity";

    private EditText _nameText;
    private EditText _passwordText;
    private EditText _confirmPasswordText;
    private Button _signupButton;
    private TextView _loginLink;
    private ArrayList<Integer> ages;
    private Spinner _spinner;
    private RadioGroup _gender;
    private TextView errorText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        _nameText = (EditText) findViewById(R.id.input_name);
        _confirmPasswordText = (EditText) findViewById(R.id.input_confirm_password);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);
        _spinner = (Spinner) findViewById(R.id.spinner);
        _gender = (RadioGroup) findViewById(R.id.myRadioGroup);
        errorText = (TextView) findViewById(R.id.errorTextSpinner);

        ages = new ArrayList<Integer>(82);

        for(int i = 0; i < 100; i++){
            ages.add(i);
        }

        Log.i("gfender", _gender.toString());

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
        R.layout.custom_spinner, ages);

        _spinner.setAdapter(adapter);


    _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String passwordConfirm = _confirmPasswordText.getText().toString();
        String password = _passwordText.getText().toString();
        String gender = "male";

        if(_gender.getCheckedRadioButtonId() == R.id.femaleRadio){
            gender = "female";
        }

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {


                        //onSignupSuccess();
                         onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String passwordConfirm = _confirmPasswordText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (name.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(name).matches()) {
            _nameText.setError("enter a valid email address");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if(!password.toString().equals(passwordConfirm.toString())){
            _confirmPasswordText.setError("Passwords must match!");
            valid = false;
        }else if (password.isEmpty() || passwordConfirm.isEmpty()|| password.length() < 4 || password.length() > 10
                || passwordConfirm.length() < 4 || passwordConfirm.length() > 10) {
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            _confirmPasswordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
            _confirmPasswordText.setError(null);
        }

        if(_gender.getCheckedRadioButtonId() == R.id.maleRadio){

        } else if (_gender.getCheckedRadioButtonId() == R.id.femaleRadio){

        }

        if(Integer.parseInt(_spinner.getSelectedItem().toString()) < 18){
            if(errorText.getVisibility() == View.GONE){
                errorText.setVisibility(View.VISIBLE);
            }

            errorText.setError("anything here, just to add the icon");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Must be 18 or older!");//changes the selected item text to this
        } else {
            errorText.setVisibility(View.GONE);
        }
        return valid;
    }
}
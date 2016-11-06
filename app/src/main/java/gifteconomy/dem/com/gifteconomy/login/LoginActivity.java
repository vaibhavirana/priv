package gifteconomy.dem.com.gifteconomy.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.regex.Pattern;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.home.activity.HomeActivity;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private EditText edtEmail;
    private EditText edtPassword;
    private ScrollView mLoginFormView;
    private Button btnSignin;
    private TextView txtForgetpwsd, txtRegister;
    private EditText edtForgetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }

    private void init() {
        // Set up the login form.

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null)
            toolbar.setTitle("Login");
        setSupportActionBar(toolbar);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        btnSignin = (Button) findViewById(R.id.btnLogin);
        btnSignin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        txtForgetpwsd = (TextView) findViewById(R.id.txtForgetpwsd);
        txtForgetpwsd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog dialog = new MaterialDialog.Builder(LoginActivity.this)
                        .title("Forget Password")
                        .customView(R.layout.dialog_forget, true)
                        .show();
                initDialog(dialog);


            }
        });
        /*txtRegister = (TextView) findViewById(R.id.txtRegister);
        txtRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i1);
            }
        });
*/
        mLoginFormView = (ScrollView) findViewById(R.id.login_form);
    }

    private void initDialog(final MaterialDialog dialog) {
        edtForgetEmail = (EditText) dialog.getCustomView().findViewById(R.id.edtForgetEmail);
        Button txtSubmit = (Button) dialog.getCustomView().findViewById(R.id.txtSubmit);
        txtSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtForgetEmail.getText().toString().trim())) {
                    Toast.makeText(LoginActivity.this, "Enter Email-id", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    // call forget api
                }
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        edtEmail.setError(null);
        edtPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edtPassword.setError(getString(R.string.error_invalid_password));
            focusView = edtPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        } else if (!isValidEmail(email)) {
            edtEmail.setError(getString(R.string.error_invalid_email));
            focusView = edtEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            if (password.equals("12345")) {
                // Toast.makeText(LoginActivity.this,"Login successfull",Toast.LENGTH_SHORT).show();
                // Log.d("m in 1","in success");
                Intent i1 = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i1);
                finish();
            }

            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            //return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
            return pattern.matcher(target).matches();
        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


}


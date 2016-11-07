package gifteconomy.dem.com.gifteconomy.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cloudrail.si.CloudRail;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.constant.Cons;
import gifteconomy.dem.com.gifteconomy.home.activity.HomeActivity;
import gifteconomy.dem.com.gifteconomy.login.API.LoginApi;
import gifteconomy.dem.com.gifteconomy.login.model.LoginRequest;
import gifteconomy.dem.com.gifteconomy.login.model.LoginResponse;
import gifteconomy.dem.com.gifteconomy.login.service.LoginService;
import gifteconomy.dem.com.gifteconomy.utils.Functions;
import gifteconomy.dem.com.gifteconomy.utils.MyApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivityRevised extends AppCompatActivity implements OnClickListener {

    private Cons.SocialMediaProvider mProfile;
    private View mRootView;
    private TextView btnRegister,txtForgetpwsd;
    private Button btnLogin, btnFb, btnGplus;
    private EditText edtEmail, edtPassword;
    private ProgressDialog progressDialog;
    private EditText edtForgetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        CloudRail.setAppKey("58201425d1167570c553c925");
    }

    private void init() {
        mRootView = findViewById(android.R.id.content);
        // txtTagLine = (TextView) findViewById(R.id.txtTagLine);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        // txtTerms = (TextView) findViewById(R.id.txtTerms);

        btnRegister = (TextView) findViewById(R.id.btnRegister);
        txtForgetpwsd = (TextView) findViewById(R.id.txtForgetpwsd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnFb = (Button) findViewById(R.id.btnfacebook);
        btnGplus = (Button) findViewById(R.id.btnGoogle);

        progressDialog = new ProgressDialog(this);
        actionListener();
        // printKeyHash(this);
    }


    private void actionListener() {
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnFb.setOnClickListener(this);
        btnGplus.setOnClickListener(this);
        txtForgetpwsd.setOnClickListener(this);
        //  txtTerms.setOnClickListener(this);
    }

    private void initDialog(final MaterialDialog dialog) {
        edtForgetEmail = (EditText) dialog.getCustomView().findViewById(R.id.edtForgetEmail);
        Button txtSubmit = (Button) dialog.getCustomView().findViewById(R.id.txtSubmit);
        txtSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtForgetEmail.getText().toString().trim())) {
                    Toast.makeText(LoginActivityRevised.this, "Enter Email-id", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    // call forget api
                }
            }
        });
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
              //  Functions.fireIntent(this, SignupActivity.class);
                break;

            case R.id.btnLogin:
                Functions.hideKeyPad(this, view);
                login();
                // presenter.checkLogin(Functions.getValue(edtEmail), Functions.getValue(edtPassword));
                break;

            case R.id.btnfacebook:
                if (Functions.isConnected(this)) {
                    // FBLogin();
                    mProfile = Cons.SocialMediaProvider.FACEBOOK;
                    performLogin();
                    Functions.showSnack(mRootView, getString(R.string.app_name));
                }
                else
                Functions.showSnack(mRootView, getString(R.string.no_internet_connection_message));
                break;

            case R.id.btnGoogle:
                if (Functions.isConnected(this)) {
                    // GooglesignIn();
                    mProfile = Cons.SocialMediaProvider.FACEBOOK;
                    performLogin();
                    Functions.showSnack(mRootView, getString(R.string.app_name));
                }
                else
                Functions.showSnack(mRootView, getString(R.string.no_internet_connection_message));
                break;

            case R.id.txtForgetpwsd:
                MaterialDialog dialog = new MaterialDialog.Builder(LoginActivityRevised.this)
                        .title("Forget Password")
                        .customView(R.layout.dialog_forget, true)
                        .show();
                initDialog(dialog);
                break;
        }
    }


    public void performLogin() {
        Intent intent = new Intent(this, LoginService.class);
        intent.putExtra(LoginService.EXTRA_PROFILE, mProfile);
        startService(intent);
    }

    private void login() {
        if (TextUtils.isEmpty(edtEmail.getText().toString().trim())) {
            edtEmail.setError(this.getString(R.string.enter_email));
            return;

        }/* else if (!Functions.emailValidation(edtEmail.getText().toString().trim())) {
            edtEmail.setError(this.getString(R.string.invalid_email));
            return;

        } */else if (TextUtils.isEmpty(edtPassword.getText().toString().trim())) {
            edtPassword.setError(this.getString(R.string.enter_pwd));
            return;

        } else if (edtPassword.getText().toString().trim().length() < 6) {
            edtPassword.setError(this.getString(R.string.invalid_pwd));
            return;

        } else {
            // call WS for check credentials and response save to realm
            callLogin(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim());
            //  Functions.fireIntent(LoginActivityRevised.this, HomeActivity.class);
        }
    }


    protected void showHideProgressDialog(boolean isShow) {
        if (isShow) {
            progressDialog.setMessage("Please Wait..");
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    private void callLogin(final String email, final String password) {
        // login webservice
        if (Functions.isConnected(this)) {
            showHideProgressDialog(true);

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.username = email;
            loginRequest.password = password;

            LoginApi loginApiService = MyApplication.retrofit.create(LoginApi.class);
            Call<LoginResponse> call = loginApiService.login(loginRequest);
            //Call<LoginResponse> call = loginApiService.login(email,password);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.e("onResponse", response.body().toString());
                    if(response.body().data.flag)
                    {
                        Functions.fireIntent(LoginActivityRevised.this, HomeActivity.class);
                    }
                    else
                    {
                        Functions.showSnack(mRootView,response.body().data.message);
                        showHideProgressDialog(false);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Functions.showSnack(mRootView,getResources().getString(R.string.api_error));
                    showHideProgressDialog(false);
                }
            });

            //  Functions.fireIntent(LoginActivityRevised.this, Homep.class);
        } else {
            Functions.showSnack(mRootView, getString(R.string.no_internet_connection_message));
            showHideProgressDialog(false);
        }
    }

}


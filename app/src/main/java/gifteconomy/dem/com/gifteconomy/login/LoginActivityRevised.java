package gifteconomy.dem.com.gifteconomy.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cloudrail.si.CloudRail;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.constant.Cons;
import gifteconomy.dem.com.gifteconomy.home.activity.HomeActivity;
import gifteconomy.dem.com.gifteconomy.login.API.LoginApi;
import gifteconomy.dem.com.gifteconomy.login.model.LoginRequest;
import gifteconomy.dem.com.gifteconomy.login.model.LoginResponse;
import gifteconomy.dem.com.gifteconomy.login.service.LoginService;
import gifteconomy.dem.com.gifteconomy.signup.SignupActivity;
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
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        init();
        CloudRail.setAppKey("58201425d1167570c553c925");
        printHashKey();
    }

    public  void printHashKey() {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
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
                Functions.fireIntent(this, SignupActivity.class);
                break;

            case R.id.btnLogin:
                Functions.hideKeyPad(this, view);
                login();
                // presenter.checkLogin(Functions.getValue(edtEmail), Functions.getValue(edtPassword));
                break;

            case R.id.btnfacebook:
                if (Functions.isConnected(this)) {
                     FBLogin();
                    //mProfile = Cons.SocialMediaProvider.FACEBOOK;
                    //performLogin();
                   // Functions.showSnack(mRootView, getString(R.string.app_name));
                }
                else
                Functions.showSnack(mRootView, getString(R.string.no_internet_connection_message));
                break;

            case R.id.btnGoogle:
                if (Functions.isConnected(this)) {
                    // GooglesignIn();
                    mProfile = Cons.SocialMediaProvider.GOOGLE_PLUS;
                    performLogin();
                  //  Functions.showSnack(mRootView, getString(R.string.app_name));
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

    private void FBLogin() {
        // set read permission for accessing user info
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(Cons.FB_READ_PERMISSIONS));
        // create callback manager for handling login/logout
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                try {
                                    // Log.d("user info", object.toString());
                                    String email = object.getString("email");
                                    //  String birthday = object.getString("birthday");
                                    String id = object.getString("id");
                                    String fname = object.getString("first_name");
                                    String lname = object.getString("last_name");
                                    String gender = object.getString("gender");
                                    //tv_profile_name.setText(name);
                                    String imageurl = "https://graph.facebook.com/" + id + "/picture?type=large";
                                      Log.e("user info", fname + " || " + lname + " || " +email +" || "+ gender );


                                    ///////////////////////////////////  please code here//////////////////////
                                    // presenter.checkFBLogin(fname, lname, email, gender);

                                    // Login(Functions.getValue(edtEmail), Functions.getValue(edtPassword));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

                /* AccessTokenTracker to manage logout*/
                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                               AccessToken currentAccessToken) {
                        if (currentAccessToken == null) {
                            // do action like  navigate to login page
                            // Functions.fireIntent(LoginActivity.this, LoginActivity.class);
                            Functions.showSnack(mRootView, getResources().getString(R.string.fail_msg));
                        }
                    }
                };
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Functions.showSnack(mRootView, getResources().getString(R.string.fail_msg));
            }
        });
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


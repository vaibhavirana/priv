package gifteconomy.dem.com.gifteconomy.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.helper.Functions;
import gifteconomy.dem.com.gifteconomy.home.activity.HomeActivity;
import gifteconomy.dem.com.gifteconomy.login.API.LoginApi;
import gifteconomy.dem.com.gifteconomy.login.model.LoginRequest;
import gifteconomy.dem.com.gifteconomy.signup.SignupActivity;
import gifteconomy.dem.com.gifteconomy.utils.Cons;
import gifteconomy.dem.com.gifteconomy.utils.MyApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityRevised extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private View mRootView;
    private TextView btnRegister;
    private Button btnLogin, btnFb, btnGplus;
    private EditText edtEmail, edtPassword;
    private ProgressDialog progressDialog;
    private static GoogleCloudMessaging gcm;
    private static String GCM_ID = "";

    private static final int RC_SIGN_IN = 9001;
    // FB Login Variables
    // private LoginButton facebookButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private GoogleApiClient mGoogleApiClient;

    // private TextView txtTagLine, txtTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);
        initGoogle();

        // presenter = new LoginPresenterImpl(this, this);
        init();

    }

    private void initGoogle() {

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

    }

    private void init() {
        mRootView = findViewById(android.R.id.content);
        // txtTagLine = (TextView) findViewById(R.id.txtTagLine);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        // txtTerms = (TextView) findViewById(R.id.txtTerms);

        btnRegister = (TextView) findViewById(R.id.btnRegister);
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
        //  txtTerms.setOnClickListener(this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("G+ Login", "onConnectionFailed:" + connectionResult);
        Functions.showSnack(mRootView, "Google Play Services error.");
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                Functions.fireIntent(this, SignupActivity.class);
                break;

            case R.id.btnLogin:
                Functions.hideKeyPad(this, v);
                login();
                // presenter.checkLogin(Functions.getValue(edtEmail), Functions.getValue(edtPassword));
                break;

            case R.id.btnfacebook:
                if (Functions.isConnected(this))
                    FBLogin();
                else
                    Functions.showSnack(mRootView, "Check Internet Connection");
                break;

            case R.id.btnGoogle:
                if (Functions.isConnected(this))
                    GooglesignIn();
                else
                    Functions.showSnack(mRootView, "Check Internet Connection");
                break;


        }
    }

    private void login() {
        if (TextUtils.isEmpty(edtEmail.getText().toString().trim())) {
            edtEmail.setError(this.getString(R.string.enter_email));
            return;

        } else if (!Functions.emailValidation(edtEmail.getText().toString().trim())) {
            edtEmail.setError(this.getString(R.string.invalid_email));
            return;

        } else if (TextUtils.isEmpty(edtPassword.getText().toString().trim())) {
            edtPassword.setError(this.getString(R.string.enter_pwd));
            return;

        } else if (edtPassword.getText().toString().trim().length() < 6) {
            edtPassword.setError(this.getString(R.string.invalid_pwd));
            return;

        } else {
            // call WS for check credentials and response save to realm
            //callLogin(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim());
            Functions.fireIntent(LoginActivityRevised.this, HomeActivity.class);
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
            Call<LoginRequest> call = loginApiService.login(loginRequest);
            //Call<LoginResponse> call = loginApiService.login(email,password);
            call.enqueue(new Callback<LoginRequest>() {
                @Override
                public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response) {
                    Log.e("onResponse", response.body().toString());
                }

                @Override
                public void onFailure(Call<LoginRequest> call, Throwable t) {

                }
            });
            /*call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.d("onResponse", response.body().toString());
                    LoginResponse response1 = response.body();
                    // setInterestArray(response1);
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e("onFailure", t.toString());
                }
            });*/

            //  Functions.fireIntent(LoginActivityRevised.this, Homep.class);
        } else {
            Functions.showSnack(mRootView, getString(R.string.no_internet_connection_message));
            showHideProgressDialog(false);
        }
    }

    public void printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

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

    public void getGCM() {
        // GET GCM_ID
        if (Functions.isGooglePlayServiceAvailable(this)) {
            new AsyncTask<Void, Void, Void>() {
                // String GCM_ID = "";

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging.getInstance(LoginActivityRevised.this);
                        }
                        GCM_ID = gcm.register(LoginActivityRevised.this.getString(R.string.project_id));
                        Log.e("GCM ID :", GCM_ID);
                        if (GCM_ID == null || GCM_ID == "") {
                            GCM_ID = "";
                        }
                        ///////////////////////////////////  please code here//////////////////////
                        //doLogin(email, password);
                    } catch (Exception ex) {
                    }
                    return null;
                }
            }.execute();
        } else {
            Functions.showSnack(mRootView, getString(R.string.no_play_services));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Functions.hideKeyPad(this, findViewById(android.R.id.content));
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
                                    //  Log.d("user info", fname + " || " + lname + " || " +email +" || "+ gender );


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

    private void GooglesignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent((GoogleApiClient) mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                        //   googleButton.setVisibility(View.VISIBLE);
                        // findViewById(R.id.btnSignout).setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("G+Login", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String[] words = acct.getDisplayName().split(" ");
            Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String fname = words[0];
            String lname = words[1];

            ///////////////////////////////////  please code here//////////////////////
            //presenter.checkGPlusLogin(fname, lname, acct.getEmail(), person.getGender());

            //Log.d("Google user info", fname + " || "+ lname +  " || " +acct.getDisplayName() + " || " + acct.getEmail());
            //Log.e("onSuccess", "Success");

            // redirecting to main page..
            // Functions.fireIntent(LoginActivity.this, MainPageActivity.class);
            // LoginActivity.this.finish();

        } else {
            // Signed out, show unauthenticated UI.
            Log.e("onFailure", "Fail login");
            Functions.showSnack(mRootView, getResources().getString(R.string.fail_msg));
        }
    }

    public void socialLogin(String fname,String lname,String email,String gender,String type)
    {

    }

}

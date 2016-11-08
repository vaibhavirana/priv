package gifteconomy.dem.com.gifteconomy.signup;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.signup.API.SignUpApi;
import gifteconomy.dem.com.gifteconomy.signup.model.SignUpRequest;
import gifteconomy.dem.com.gifteconomy.signup.model.SignUpResponse;
import gifteconomy.dem.com.gifteconomy.utils.Functions;
import gifteconomy.dem.com.gifteconomy.utils.MyApplication;
import gifteconomy.dem.com.gifteconomy.utils.Prefs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class SignupActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    // UI references.
    private View mRootView;
    private EditText edtEmail, edtFName, edtLName, edtIntro, edtDateofBirth;
    private EditText edtPassword, edtConfirmPassword, edtLocation;
    private View mLoginFormView;
    private Button btnSignup;
    private String lat = "22.3071587", lon = " 73.1812187";
    private static GoogleCloudMessaging gcm;
    private static String GCM_ID = "";

    String myFormat = "MM/dd/yy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


   /* private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(22.3071587, 73.1812187), new LatLng(22.3071587, 73.1812187));*/
    private ProgressDialog progressDialog;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        checkGPSstatus();
        init();
    }

    private void checkGPSstatus() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!statusOfGPS) {
            buildAlertMessageNoGps();
           /* if(!checkPermission())
            {
               requestPermission();
            }*/
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void init() {
        // Set up the login form.

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null)
            toolbar.setTitle("Sign Up");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getGCM();
        ;



        mRootView = findViewById(android.R.id.content);
        progressDialog = new ProgressDialog(this);
        edtLocation = (EditText) findViewById(R.id.edtLocation);
        edtFName = (EditText) findViewById(R.id.edtFName);
        edtLName = (EditText) findViewById(R.id.edtLName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtDateofBirth = (EditText) findViewById(R.id.edtDateofBirth);
        edtIntro = (EditText) findViewById(R.id.edtIntro);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);


        myCalendar = Calendar.getInstance();
    //    edtDateofBirth.setText(sdf.format(myCalendar.getTime()));
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //updateLabel();
                edtDateofBirth.setText(sdf.format(myCalendar.getTime()));
            }
        };

        edtDateofBirth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignupActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                edtDateofBirth.setText(sdf.format(myCalendar.getTime()));
            }
        });

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
                //
             //    Functions.fireIntent(SignupActivity.this, SelectInterestActivity.class);

            }
        });
        edtLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(SignupActivity.this, LocationPickerActivity.class);
                startActivityForResult(i, 1);*/

                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    //intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = intentBuilder.build(SignupActivity.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                final Place place = PlacePicker.getPlace(this, data);
                String area = place.getName().toString();
                edtLocation.setText(area);
                Log.e("LATLoNG****", place.getLatLng() + "");
                Log.e("LATITUDE****", String.valueOf(place.getLatLng().latitude));
                Log.e("LONGITUDE****", String.valueOf(place.getLatLng().longitude));
                lat = String.valueOf(place.getLatLng().latitude);
                lon = String.valueOf(place.getLatLng().longitude);

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void attemptSignup() {

        // Reset errors.
        edtFName.setError(null);
        edtLName.setError(null);
        edtEmail.setError(null);
        edtLocation.setError(null);
        edtIntro.setError(null);
        edtDateofBirth.setError(null);
        edtPassword.setError(null);
        edtConfirmPassword.setError(null);

        // Store values at the time of the login attempt.
        String fname = edtFName.getText().toString();
        String lname = edtLName.getText().toString();
        String email = edtEmail.getText().toString();
        String dateOfBirth = edtDateofBirth.getText().toString();
        String location = edtLocation.getText().toString();
        String intro = edtIntro.getText().toString();
        String password = edtPassword.getText().toString();
        String cpassword = edtConfirmPassword.getText().toString();
        String gender = "MALE";

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(fname)) {
            edtFName.setError(getString(R.string.error_field_required));
            focusView = edtFName;
            cancel = true;
        } else if (TextUtils.isEmpty(lname)) {
            edtLName.setError(getString(R.string.error_field_required));
            focusView = edtLName;
            cancel = true;
        }

        // Check for a valid email address.
        else if (TextUtils.isEmpty(email)) {
            edtEmail.setError(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        }/* else if (!Functions.emailValidation(email)) {
            edtEmail.setError(getString(R.string.error_invalid_email));
            focusView = edtEmail;
            cancel = true;
        }*/ else if (TextUtils.isEmpty(dateOfBirth)) {
            edtDateofBirth.setError(getString(R.string.error_field_required));
            focusView = edtDateofBirth;
            cancel = true;
        } else if (TextUtils.isEmpty(intro)) {
            edtIntro.setError(getString(R.string.error_field_required));
            focusView = edtIntro;
            cancel = true;
        } else if (TextUtils.isEmpty(location)) {
            edtLocation.setError(getString(R.string.error_field_required));
            focusView = edtLocation;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        else if (TextUtils.isEmpty(password)) {
            edtPassword.setError(getString(R.string.error_field_required));
            focusView = edtPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            edtPassword.setError(getString(R.string.error_invalid_password));
            focusView = edtPassword;
            cancel = true;
        }

        // Check for a valid confirm password, if the user entered one.
        else if (TextUtils.isEmpty(cpassword)) {
            edtConfirmPassword.setError(getString(R.string.error_field_required));
            focusView = edtConfirmPassword;
            cancel = true;
        } else if (!isPasswordValid(cpassword)) {
            edtConfirmPassword.setError(getString(R.string.error_invalid_password));
            focusView = edtConfirmPassword;
            cancel = true;
        } else {
            if (cancel) {
                focusView.requestFocus();
            } else {

                if (password.equals(cpassword)) {
                    Toast.makeText(SignupActivity.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
                    callSignUpApi(fname, lname, email, password, gender, dateOfBirth, intro, lat, lon, location, "N", GCM_ID, Functions.getDeviceId(SignupActivity.this), Functions.getDeviceModel());
                } else {
                    edtConfirmPassword.setError(getString(R.string.error_incorrect_password));
                    edtConfirmPassword.requestFocus();
                }

            }
        }
    }

    private void callSignUpApi(String fname, String lname, String email, String password, String gender, String dateOfBirth, String intro, String lat, String lon, String location, String type, String gcm, String deviceId, String deviceModel) {
        if (Functions.isConnected(this)) {
            // showHideProgressDialog(true);

            SignUpRequest request = new SignUpRequest();
            request.firstname = fname;
            request.lastname = lname;
            request.email = email;
            request.password = password;
            request.gender = gender;
            request.dateofbirth = dateOfBirth;
            request.introduction = intro;
            request.latitude = lat;
            request.longitude = lon;
            request.location = location;
            request.logintype = type;
            request.gcmid = gcm;
            request.deviceid = deviceId;
            request.devicename = deviceModel;

            Log.d("Request", request.toString());
            SignUpApi signUpApi= MyApplication.retrofit.create(SignUpApi.class);
            Call<SignUpResponse> call = signUpApi.signup(request);
            //Call<LoginResponse> call = loginApiService.login(email,password);
            call.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                  //  Log.e("onResponse", response.body().toString());
                   // Log.e("onResponse", response.body().data.toString());
                    if(response.body().data.flag)
                    {
                        // go next
                        Prefs.with(SignupActivity.this).getString("username",response.body().data.user.username);
                        Prefs.with(SignupActivity.this).getString("password",response.body().data.user.password);
                        Prefs.with(SignupActivity.this).getString("status",response.body().data.user.status);
                        Prefs.with(SignupActivity.this).getString("uid",response.body().data.user.profile.uid);
                        Prefs.with(SignupActivity.this).getString("lat",response.body().data.user.profile.lat);
                        Prefs.with(SignupActivity.this).getString("longitude",response.body().data.user.profile.longitude);
                        Prefs.with(SignupActivity.this).getString("location",response.body().data.user.profile.location);

                        Functions.fireIntent(SignupActivity.this, SelectInterestActivity.class);
                        finish();
                    }
                    else
                    {
                        Functions.showSnack(mRootView,response.body().data.message);
                        showHideProgressDialog(false);
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    Functions.showSnack(mRootView,getResources().getString(R.string.api_error));
                    showHideProgressDialog(false);
                }
            });
        }
        else
        {
            Functions.showSnack(mRootView, getString(R.string.no_internet_connection_message));
            showHideProgressDialog(false);
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

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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
                            gcm = GoogleCloudMessaging.getInstance(SignupActivity.this);
                        }
                        GCM_ID = gcm.register(SignupActivity.this.getString(R.string.project_id));
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            Toast.makeText(this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Snackbar.make(findViewById(android.R.id.content), "Permission Granted, Now you can access location data.", Snackbar.LENGTH_LONG).show();

                } else {

                    Snackbar.make(findViewById(android.R.id.content), "Permission Denied, You cannot access location data.", Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }

}


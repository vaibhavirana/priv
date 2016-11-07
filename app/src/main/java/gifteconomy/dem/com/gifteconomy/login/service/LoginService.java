package gifteconomy.dem.com.gifteconomy.login.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.cloudrail.si.exceptions.AuthenticationException;
import com.cloudrail.si.interfaces.Profile;
import com.cloudrail.si.services.Facebook;
import com.cloudrail.si.types.DateOfBirth;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.constant.Cons;
import gifteconomy.dem.com.gifteconomy.login.model.UserAccount;



/**
 * Created by vaibhavirana on 27-09-2016.
 */
public class LoginService extends IntentService {
    public static final String EXTRA_PROFILE = "profile";
    public static final String EXTRA_SOCIAL_ACCOUNT = "account";
    private static final String TAG = "VIVZ";
  /*  private RegisterRequest registerRequest;
    private LoginResponse loginResponse;*/
    private Profile profileForRegi;
    private UserAccount account;

    public LoginService() {
        super("Social Login Service");
    }

    private Profile init(Cons.SocialMediaProvider provider) throws AuthenticationException {
        //Superclass reference variable = subclass object
        Profile profile = null;
        switch (provider) {
            case FACEBOOK:
                profile = new Facebook(this, getString(R.string.facebook_app_id), getString(R.string.facebook_app_secret));
                break;
           /* case TWITTER:
                profile = new Twitter(this, getString(R.string.twitter_app_key), getString(R.string.twitter_app_secret));
                break;*/
            case GOOGLE_PLUS:
                //profile = new GooglePlus(this, getString(R.string.google_plus_app_key), getString(R.string.google_plus_app_secret));
                break;

        }
        //At run time, profile will contain one of the 8 objects above and will fetch details from that particular provider
        return profile;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                Bundle extras = intent.getExtras();
                //Guess what I sent from the Activity which calls this Service! Our Enumeration variable!
                //It contains FACEBOOK or TWITTER or ... and is Serializable!
                Cons.SocialMediaProvider provider = (Cons.SocialMediaProvider) extras.getSerializable(EXTRA_PROFILE);

                //You already know which social account the user will use, just create an object of it.
                Profile profile = init(provider);

                //Create a User Account object with all the details
                account = init(profile, provider);
                Log.e("user detail", account.getFullName() + " || " + account.getEmail() + " || " + account.getIdentifier());


                profileForRegi = profile;


                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);
              /*  final LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail(account.getEmail());
                loginRequest.setPassword("");
                loginRequest.setRegister_type(Prefs.with(this).getString(Constants.PREF_LOGINTYPE, null));
                loginRequest.setLogintype_id(account.getIdentifier());

                Handler mainHandler = new Handler(getApplicationContext().getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        sendLoginToServer(loginRequest);
                    }
                };
                mainHandler.post(myRunnable);*/




    /*            //Start another Activity to show the user details
                Intent detailsIntent = new Intent(getApplicationContext(), HomeActivity.class);

                //To directly start activities from an IntentService, we need to add this flag, Google if curious!


                //Send the User Account object with all details
                detailsIntent.putExtra(EXTRA_SOCIAL_ACCOUNT, account);
                startActivity(detailsIntent);*/
            } catch (AuthenticationException e) {

                //When you cancel in the middle of a login, cloud rail was throwing this exception, hence!
                Log.e(TAG, "onHandleIntent: You cancelled", e);

            }
        }
    }

   /* private void sendLoginToServer(LoginRequest loginRequest) {
        LoginApi loginApi = MyApplication.retrofit1.create(LoginApi.class);
        Log.e("login resuest", loginRequest.toString());
        Call<LoginResponse> call = loginApi.loginCall(loginRequest);


        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().getMessage().equals("Success")) {
                    loginResponse = response.body();
                    if (loginResponse.getoAuth().getToken() != null && !loginResponse.getoAuth().getToken().equals("")) {
                        UserPojo user = response.body().getDescription().getData().getUser();
                        UsersettingPojo usersettingPojo = response.body().getDescription().getData().getUsersetting();
                        Prefs.with(LoginService.this).save(Constants.TOKEN, loginResponse.getoAuth().getToken());
                        PrefsUtil.setLogin(LoginService.this, true);
                        Log.e("User1", user.toString());
                        Log.e("UserSetting1", usersettingPojo.toString());
                        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginService.this, Constants.USER, MODE_PRIVATE);
                        complexPreferences.putObject("user", user);
                        complexPreferences.putObject("usersetting", usersettingPojo);
                        complexPreferences.commit();
                        MyApplication.dbHelper.insertUser(user, account.getIdentifier());
                        MyApplication.dbHelper.insertUserSetting(usersettingPojo);
                        Intent i = new Intent(Constants.NEW_LOGIN);
                        LocalBroadcastManager.getInstance(LoginService.this).sendBroadcast(i);

                    } else {
                        Log.e("1", "1");
                        if (profileForRegi != null) {
                            Handler mainHandler = new Handler(getApplicationContext().getMainLooper());

                            Runnable myRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    register(account);
                                }
                            };
                            mainHandler.post(myRunnable);
                        }
                    }
                } else {
                    Log.e("2", "2");
                    if (profileForRegi != null) {
                        if (profileForRegi != null) {
                            Handler mainHandler = new Handler(getApplicationContext().getMainLooper());

                            Runnable myRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    register(account);
                                }
                            };
                            mainHandler.post(myRunnable);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("failuer1", "Failuer");
            }
        });
    }*/

    private UserAccount init(Profile profile, Cons.SocialMediaProvider provider) {
        //The usual get-set stuff
        UserAccount account = new UserAccount();
        account.setIdentifier(profile.getIdentifier());
        account.setFullName(profile.getFullName());
        account.setEmail(profile.getEmail());
        account.setDescription(profile.getDescription());
        DateOfBirth dob = profile.getDateOfBirth();
        if (dob != null) {
            Long day = dob.getDay();
            Long month = dob.getMonth();
            Long year = dob.getYear();
            if (day != null) {
                account.setDay(day);
            }
            if (month != null) {
                account.setMonth(month);
            }
            if (year != null) {
                account.setYear(year);
            }
        }
        account.setGender(profile.getGender());
        account.setPictureURL(profile.getPictureURL());
        account.setLocale(profile.getLocale());

//        PrefsUtil.setLogin(this, true);
     /*   PrefsUtil.setLoginAsGuest(this, false);
        //PrefsUtil.setIsEng(this, true);
        Prefs.with(this).save(Cons.PREF_NAME, profile.getFullName());
        Prefs.with(this).save(Cons.PREF_EMAIL, profile.getEmail());
        Prefs.with(this).save(Cons.PREF_PROFILE_URL, profile.getPictureURL());
        Prefs.with(this).save(Cons.PREF_GENDER, profile.getGender());
        switch (provider) {
            case FACEBOOK:
                Prefs.with(this).save(Cons.PREF_LOGINTYPE, "fb");
                break;
            case GOOGLE_PLUS:
                Prefs.with(this).save(Cons.PREF_LOGINTYPE, "google");
                break;

        }*/

        return account;
    }

  /*  private void register(UserAccount profile) {
        registerRequest = new RegisterRequest();
        registerRequest.name = profile.getFullName();
        registerRequest.email = profile.getEmail();
        registerRequest.gender = profile.getGender();
        registerRequest.profile_pic = profile.getPictureURL();
        registerRequest.register_type = Prefs.with(this).getString(Constants.PREF_LOGINTYPE, null);
        registerRequest.android_id = Function.getDeviceId(this);
        registerRequest.logintype_id = profile.getIdentifier();
        if (profileForRegi != null) {
            Handler mainHandler = new Handler(getApplicationContext().getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    callRegisterApi(registerRequest);
                }
            };
            mainHandler.post(myRunnable);
        }

    }

    private void callRegisterApi(final RegisterRequest registerRequest) {
        RegisterApi categoryApi = MyApplication.retrofit1.create(RegisterApi.class);
        Log.e("regi", registerRequest.toString());
        Call<RegisterResp> call = categoryApi.callregister(registerRequest);


        call.enqueue(new Callback<RegisterResp>() {
            @Override
            public void onResponse(Call<RegisterResp> call, Response<RegisterResp> response) {
                Log.e("registration", response.body().toString());
                UserPojo user = response.body().description.data.User;
                UsersettingPojo usersettingPojo = response.body().description.data.Usersetting;
                Log.e("User", user.toString());
                Log.e("UserSetting", usersettingPojo.toString());
                PrefsUtil.setLogin(LoginService.this, true);

                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(LoginService.this, Constants.USER, MODE_PRIVATE);
                complexPreferences.putObject("user", user);
                complexPreferences.putObject("usersetting", usersettingPojo);
                complexPreferences.commit();

//        UserData u=complexPreferences.getObject("user",UserData.class);

                MyApplication.dbHelper.insertUser(user, registerRequest.logintype_id);
                MyApplication.dbHelper.insertUserSetting(usersettingPojo);
                Intent i = new Intent(Constants.NEW_LOGIN);
                LocalBroadcastManager.getInstance(LoginService.this).sendBroadcast(i);
            }

            @Override
            public void onFailure(Call<RegisterResp> call, Throwable t) {
                Log.e("registration", "Failuer");
            }
        });
    }*/

}

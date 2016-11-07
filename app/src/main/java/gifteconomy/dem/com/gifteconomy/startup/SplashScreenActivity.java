package gifteconomy.dem.com.gifteconomy.startup;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.login.LoginActivityRevised;
import gifteconomy.dem.com.gifteconomy.utils.Functions;
import gifteconomy.dem.com.gifteconomy.utils.PrefsUtil;
import gifteconomy.dem.com.gifteconomy.utils.UIUtil;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtil.setActivityToFullScreen(this);
        setContentView(R.layout.activity_splash_screen);

        TextView txtAppName = (TextView) findViewById(R.id.txtAppName);
        assert txtAppName != null;
       // txtAppName.setTypeface(Functions.getBoldFont(this));

        new CountDownTimer(2500, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                if (!PrefsUtil.getIsGuideRead(SplashScreenActivity.this)) {
                    Functions.fireIntent(SplashScreenActivity.this, StartUpActivity.class);

                } else if (!PrefsUtil.getLogin(SplashScreenActivity.this)) {
                    Functions.fireIntent(SplashScreenActivity.this, LoginActivityRevised.class);

                } else {
                    //Functions.fireIntent(SplashScreenActivity.this, MainPageActivity.class);

                }

                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Functions.hideKeyPad(SplashScreenActivity.this, findViewById(android.R.id.content));
    }
}

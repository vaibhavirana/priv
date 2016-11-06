package gifteconomy.dem.com.gifteconomy.home.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.home.model.UserProfile;

public class UserProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private UserProfile profile;
    private ImageView imgAvtar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profile= (UserProfile) getIntent().getExtras().getSerializable("userProfile");
       // Log.e("profile",profile.NAME);
        inittoolbar();
        init();
    }

    private void init() {

    }

    private void inittoolbar() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        imgAvtar=(ImageView)findViewById(R.id.imgAvtar);
        toolbar.setTitle(profile.NAME);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        imgAvtar.setImageResource(profile.AVATAR);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}

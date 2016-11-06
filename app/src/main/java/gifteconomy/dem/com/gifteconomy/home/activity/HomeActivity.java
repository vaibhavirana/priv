package gifteconomy.dem.com.gifteconomy.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.home.aapter.PagerAdapter;
import gifteconomy.dem.com.gifteconomy.ui.FeedbackActivity;
import gifteconomy.dem.com.gifteconomy.ui.InviteEarnActivity;
import gifteconomy.dem.com.gifteconomy.ui.MyBartersActivity;
import gifteconomy.dem.com.gifteconomy.ui.MyBookShelfActivity;
import gifteconomy.dem.com.gifteconomy.ui.MyInventoryActivity;
import gifteconomy.dem.com.gifteconomy.ui.ProfileActivity;
import gifteconomy.dem.com.gifteconomy.ui.SettingsActivity;
import gifteconomy.dem.com.gifteconomy.utils.Functions;

public class HomeActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ImageView profilePic;
    private TextView txtName;
    private TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

    }

    private void init() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        View headerLayout = navigationView.getHeaderView(0);
        profilePic = (ImageView) headerLayout.findViewById(R.id.profilePic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo Profile Activity
                Functions.fireIntent(HomeActivity.this, ProfileActivity.class);
            }
        });
        txtName = (TextView) headerLayout.findViewById(R.id.txtName);
        txtEmail = (TextView) headerLayout.findViewById(R.id.txtEmail);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                setDrawerClick(menuItem.getItemId());

                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Skill"));
        tabLayout.addTab(tabLayout.newTab().setText("Things"));
        tabLayout.addTab(tabLayout.newTab().setText("Book"));
        tabLayout.addTab(tabLayout.newTab().setText("Notify"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), HomeActivity.this);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                ImageView imageView = (ImageView) tab.getCustomView();
                assert imageView != null;
                imageView.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.button_bg), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ImageView imageView = (ImageView) tab.getCustomView();
                assert imageView != null;
                imageView.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            ImageView imageView = null;
            if (tab != null) {
                tab.setCustomView(adapter.getTabView(i));
                imageView = (ImageView) adapter.getTabView(i);
                if (i == 0) {
                    imageView.setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.button_bg), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }

    private void setDrawerClick(int itemId) {
        switch (itemId) {
            case R.id.drawer_shared:
                Functions.fireIntent(HomeActivity.this, InviteEarnActivity.class);
                break;

            case R.id.drawer_inventory:
                Functions.fireIntent(HomeActivity.this, MyInventoryActivity.class);
                break;

            case R.id.drawer_barters:
                Functions.fireIntent(HomeActivity.this, MyBartersActivity.class);
                break;

            case R.id.drawer_bookshelf:
                Functions.fireIntent(HomeActivity.this, MyBookShelfActivity.class);
                break;

            case R.id.drawer_settings:
                Functions.fireIntent(HomeActivity.this, SettingsActivity.class);
                break;

            case R.id.drawer_tips:
                /*Intent intent=new Intent(HomeActivity.this,TutorialActiivty.class);
                intent.putExtra("activity","tips");
                startActivity(intent);*/

               // Functions.fireIntent(HomeActivity.this, TutorialActiivty.class);
                break;

            case R.id.drawer_feedback:
                Functions.fireIntent(HomeActivity.this, FeedbackActivity.class);
                break;

            case R.id.drawer_tc:
                String url = "http://www.google.co.in/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

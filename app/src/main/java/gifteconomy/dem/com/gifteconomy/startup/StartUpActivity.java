package gifteconomy.dem.com.gifteconomy.startup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.constant.IntConstant;
import gifteconomy.dem.com.gifteconomy.constant.IntentConstant;
import gifteconomy.dem.com.gifteconomy.login.LoginActivityRevised;
import gifteconomy.dem.com.gifteconomy.utils.Functions;
import gifteconomy.dem.com.gifteconomy.utils.PrefsUtil;
import gifteconomy.dem.com.gifteconomy.utils.UIUtil;
import gifteconomy.dem.com.gifteconomy.widget.intro.TutorialItem;
import gifteconomy.dem.com.gifteconomy.widget.intro.tutorial.MaterialTutorialActivity;


/**
 * Created by vaibhavirana on 01-06-2016.
 */
public class StartUpActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        loadTutorial();

    }

    public void loadTutorial() {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);

    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem("Take your Medicine and be healthy!", null, R.color.slide_1, R.drawable.heart);
        TutorialItem tutorialItem2 = new TutorialItem("We will remind you about your medicine", null, R.color.slide_2, R.drawable.heart);
        TutorialItem tutorialItem3 = new TutorialItem("Opps...Injections it will hurt you :(", null, R.color.slide_3, R.drawable.heart);
        TutorialItem tutorialItem4 = new TutorialItem("Just Chill...We are in your family :)", null, R.color.slide_4, R.drawable.heart);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {


            int getButtonClickValue = data.getIntExtra(IntentConstant.INTENT_EXTRA_BUTTON_CLICK, IntConstant.getStartedClick);

            if (getButtonClickValue == IntConstant.getStartedClick) {
               // UIUtil.startActivity(this, MainPageActivity.class, true);
            } else if (getButtonClickValue == IntConstant.loginClick) {
                PrefsUtil.setIsGuideRead(StartUpActivity.this, true);
                UIUtil.startActivity(this, LoginActivityRevised.class, true);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_translate_rigth_left);
            }
            // user has read the guide screen or pressed the skip button
            //PrefsUtil.setIsGuideRead(this, true);

        } else {

            //User has pressed back button
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Functions.hideKeyPad(StartUpActivity.this, findViewById(android.R.id.content));
    }
}

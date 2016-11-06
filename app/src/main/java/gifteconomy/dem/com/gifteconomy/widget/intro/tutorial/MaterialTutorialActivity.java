package gifteconomy.dem.com.gifteconomy.widget.intro.tutorial;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.constant.IntConstant;
import gifteconomy.dem.com.gifteconomy.constant.IntentConstant;
import gifteconomy.dem.com.gifteconomy.startup.StartUpActivity;
import gifteconomy.dem.com.gifteconomy.utils.Functions;
import gifteconomy.dem.com.gifteconomy.widget.intro.MaterialTutorialFragment;
import gifteconomy.dem.com.gifteconomy.widget.intro.TutorialItem;
import gifteconomy.dem.com.gifteconomy.widget.intro.adapter.MaterialTutorialAdapter;
import gifteconomy.dem.com.gifteconomy.widget.intro.view.CirclePageIndicator;


public class MaterialTutorialActivity extends AppCompatActivity implements MaterialTutorialContract.View {

    private static final String TAG = "MaterialTutActivity";
    public static final String MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS = "tutorial_items";
    private ViewPager mHelpTutorialViewPager;
    private View mRootView;
    private TextView mTextViewSkip;
    private ImageButton mNextButton;
    private Button mDoneButton;
    private MaterialTutorialPresenter materialTutorialPresenter;

    private View.OnClickListener finishTutorialClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            materialTutorialPresenter.doneOrSkipClick();
            //PrefsUtil.setIsGuideRead(MaterialTutorialActivity.this, true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_tutorial);

        materialTutorialPresenter = new MaterialTutorialPresenter(this, this);
        setStatusBarColor();
        ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            getActionBar().hide();
        }
        mRootView = findViewById(R.id.activity_help_root);
        mHelpTutorialViewPager = (ViewPager) findViewById(R.id.activity_help_view_pager);
        mTextViewSkip = (TextView) findViewById(R.id.activity_help_skip_textview);
        mNextButton = (ImageButton) findViewById(R.id.activity_next_button);
        mDoneButton = (Button) findViewById(R.id.activity_tutorial_done);

        mDoneButton.setTypeface(Functions.getBoldFont(this));
        mTextViewSkip.setTypeface(Functions.getBoldFont(this));

        mTextViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTutorialPresenter.loginClick();
            }
        });
        mDoneButton.setOnClickListener(finishTutorialClickListener);


        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialTutorialPresenter.nextClick();


            }
        });

        List<TutorialItem> tutorialItems = getIntent().getParcelableArrayListExtra(MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS);
        materialTutorialPresenter.loadViewPagerFragments(tutorialItems);
    }

    private void setStatusBarColor() {
        if (isFinishing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void showNextTutorial() {
        int currentItem = mHelpTutorialViewPager.getCurrentItem();
        if (currentItem < materialTutorialPresenter.getNumberOfTutorials()) {
            mHelpTutorialViewPager.setCurrentItem(mHelpTutorialViewPager.getCurrentItem() + 1);
        }
    }

    @Override
    public void showEndTutorial() {
        Intent intent = new Intent(this, StartUpActivity.class);
        intent.putExtra(IntentConstant.INTENT_EXTRA_BUTTON_CLICK, IntConstant.getStartedClick);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void setBackgroundColor(int color) {
        mRootView.setBackgroundColor(color);
    }

    @Override
    public void showDoneButton() {
        mTextViewSkip.setVisibility(View.INVISIBLE);
        mNextButton.setVisibility(View.GONE);
        mDoneButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSkipButton() {
        mTextViewSkip.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.GONE);
        mDoneButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setViewPagerFragments(final List<MaterialTutorialFragment> materialTutorialFragments) {
        mHelpTutorialViewPager.setAdapter(new MaterialTutorialAdapter(getSupportFragmentManager(), materialTutorialFragments));
        CirclePageIndicator mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.activity_help_view_page_indicator);

        mCirclePageIndicator.setViewPager(mHelpTutorialViewPager);
        mCirclePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                materialTutorialPresenter.onPageSelected(mHelpTutorialViewPager.getCurrentItem());

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mHelpTutorialViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
                    @Override
                    public void transformPage(View page, float position) {
                        materialTutorialPresenter.transformPage(page, position);
                    }
                }

        );
    }

    @Override
    public void showLoginView() {
        Intent intent = new Intent(this, StartUpActivity.class);
        intent.putExtra(IntentConstant.INTENT_EXTRA_BUTTON_CLICK, IntConstant.loginClick);
        setResult(RESULT_OK, intent);
        finish();
    }
}

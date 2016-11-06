package gifteconomy.dem.com.gifteconomy.home.aapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.home.fragment.BookFragment;
import gifteconomy.dem.com.gifteconomy.home.fragment.HomeFragment;
import gifteconomy.dem.com.gifteconomy.home.fragment.NotifyFragment;
import gifteconomy.dem.com.gifteconomy.home.fragment.SkillFragment;
import gifteconomy.dem.com.gifteconomy.home.fragment.ThingFragment;

/**
 * Created by Workstation01 on 7/20/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    private int tabIcons[] = new int[]{R.drawable.ic_user, R.drawable.ic_skill, R.drawable.ic_thing,
            R.drawable.ic_book, R.drawable.ic_notifications};
    Context context;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, Context context) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                SkillFragment tab2 = new SkillFragment();
                return tab2;
            case 2:
                ThingFragment tab3 = new ThingFragment();
                return tab3;
            case 3:
                BookFragment tab4 = new BookFragment();
                return tab4;
            case 4:
                NotifyFragment tab5 = new NotifyFragment();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    public View getTabView(int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(tabIcons[position]);
        if (position == 0) {
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.button_bg), PorterDuff.Mode.SRC_ATOP);
        } else {
            imageView.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        }

        return imageView;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

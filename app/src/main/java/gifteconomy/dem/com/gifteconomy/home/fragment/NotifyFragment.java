package gifteconomy.dem.com.gifteconomy.home.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import gifteconomy.dem.com.gifteconomy.R;

/**
 * Created by Workstation01 on 7/20/2016.
 */
public class NotifyFragment extends Fragment {

    private View rootView;
    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notify, container, false);
        init();
        return rootView;
    }

    private void init() {
        initTabs();
    }

    private void initTabs() {
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        /*tabLayout.addTab(tabLayout.newTab().setText("Notify"));
        tabLayout.addTab(tabLayout.newTab().setText("Offer"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));*/

        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        final PagerAdapter adapter = new PagerAdapter
                (getActivity().getSupportFragmentManager());
        adapter.addFragment(new BlankFragment(), "Notify");
        adapter.addFragment(new BlankFragment(), "Offer");
        adapter.addFragment(new BlankFragment(), "Chat");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(BlankFragment blankFragment, String notify) {
            mFragmentList.add(blankFragment);
            mFragmentTitleList.add(notify);
        }
    }
}

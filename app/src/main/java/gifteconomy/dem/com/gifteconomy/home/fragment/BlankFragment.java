package gifteconomy.dem.com.gifteconomy.home.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gifteconomy.dem.com.gifteconomy.R;

/**
 * Created by Workstation01 on 7/20/2016.
 */
public class BlankFragment extends Fragment {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        init();
        return rootView;
    }

    private void init() {

    }

}

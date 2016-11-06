package gifteconomy.dem.com.gifteconomy.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.custom.EmptyLayout;
import gifteconomy.dem.com.gifteconomy.helper.Functions;
import gifteconomy.dem.com.gifteconomy.home.aapter.UserThingListAdapter;
import gifteconomy.dem.com.gifteconomy.home.activity.AddThingsActivity;
import gifteconomy.dem.com.gifteconomy.home.activity.ThingdetailActivty;
import gifteconomy.dem.com.gifteconomy.home.model.UserThing;

/**
 * Created by Workstation01 on 7/20/2016.
 */
public class ThingFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private RecyclerView rvThingList;
    private EmptyLayout emptyLayout;
    private FloatingActionButton fabAddThings;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_thing, container, false);
        init();
        return rootView;
    }

    private void init() {
        fabAddThings = (FloatingActionButton) rootView.findViewById(R.id.fabAddThing);
        fabAddThings.setOnClickListener(this);
        rvThingList=(RecyclerView)rootView.findViewById(R.id.rvList);
        rvThingList.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvThingList.setLayoutManager(mLayoutManager);
        rvThingList.setItemAnimator(new DefaultItemAnimator());
        emptyLayout = (EmptyLayout) rootView.findViewById(R.id.emptyLayout);

        setData();
    }

    private void setData() {

        /*rvThingList.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
        emptyLayout.setType(Cons.NO_THINGS);*/

        final List<UserThing> thingList=new ArrayList<>();
        /*Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();*/

        int[] avatars = {
                R.drawable.anastasia,
                R.drawable.andriy,
                R.drawable.dmitriy};

        String[] names = getResources().getStringArray(R.array.array_names);

        for (int i = 0; i < avatars.length; i++) {
            UserThing userThing=new UserThing();
            userThing.thingImage=avatars[i];
            userThing.thing=names[i];
            userThing.thingDesc=getString(R.string.lorem_ipsum_short);
            //userSkill.DESCRIPTION_SHORT=getString(R.string.lorem_ipsum_short);
            thingList.add(userThing);
        }

        rvThingList.setAdapter(new UserThingListAdapter(getActivity(), thingList, new UserThingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserThing userThing) {
                Toast.makeText(getActivity(),"Item click" + userThing.thing,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(), ThingdetailActivty.class);
                intent.putExtra("userThing", (Serializable) userThing);
                startActivity(intent);
            }
        }));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddThing:
                Functions.fireIntent(getActivity(), AddThingsActivity.class);
                break;
        }
    }
}

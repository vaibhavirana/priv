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

import java.util.ArrayList;
import java.util.List;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.helper.Functions;
import gifteconomy.dem.com.gifteconomy.home.aapter.UserSkillListAdapter;
import gifteconomy.dem.com.gifteconomy.home.activity.AddSkillActivity;
import gifteconomy.dem.com.gifteconomy.home.activity.SkilldetailActivty;
import gifteconomy.dem.com.gifteconomy.home.model.Userskill;

/**
 * Created by Workstation01 on 7/20/2016.
 */
public class SkillFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private RecyclerView rvSkillList;

    private FloatingActionButton fabAddSkill;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_skill, container, false);
        init();
        return rootView;
    }

    private void init() {
        fabAddSkill = (FloatingActionButton) rootView.findViewById(R.id.fabAddSkill);
        fabAddSkill.setOnClickListener(this);
        rvSkillList = (RecyclerView) rootView.findViewById(R.id.rvList);
        rvSkillList.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvSkillList.setLayoutManager(mLayoutManager);
        rvSkillList.setItemAnimator(new DefaultItemAnimator());


        setData();
    }

    private void setData() {

        final List<Userskill> skillList = new ArrayList<>();
        /*Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();*/

        int[] avatars = {
                R.drawable.skill,
                R.drawable.skill,
                R.drawable.skill};

        String[] names = getResources().getStringArray(R.array.array_names);

        for (int i = 0; i < avatars.length; i++) {
            Userskill userSkill = new Userskill();
            userSkill.skillImage = avatars[i];
            userSkill.skill = names[i];
            userSkill.skillDesc = getString(R.string.lorem_ipsum_short);
            //userSkill.DESCRIPTION_SHORT=getString(R.string.lorem_ipsum_short);
            skillList.add(userSkill);
        }

        rvSkillList.setAdapter(new UserSkillListAdapter(getActivity(), skillList, new UserSkillListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Userskill userskill) {
                Toast.makeText(getActivity(), "Item click" + userskill.skill, Toast.LENGTH_LONG).show();
                //Toast.makeText(getActivity(),"Item click" + userBook.book,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(), SkilldetailActivty.class);
               // intent.putExtra("userskill", (Serializable) userskill);
                startActivity(intent);
            }
        }));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddSkill:
                Functions.fireIntent(getActivity(), AddSkillActivity.class);
                break;
        }
    }
}


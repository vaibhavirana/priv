package gifteconomy.dem.com.gifteconomy.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.custom.cardstack.SwipeDeck;
import gifteconomy.dem.com.gifteconomy.home.model.UserProfile;

/**
 * Created by Workstation01 on 7/20/2016.
 */
public class HomeFragment extends Fragment {

    private View rootView;
   // private RecyclerView rvUserList;
    private SwipeDeck cardStack;
    private SwipeDeckAdapter adapter;

    // private SwipeDeckAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       
        rootView=inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return rootView;
    }

    private void init() {
       /* rvUserList=(RecyclerView)rootView.findViewById(R.id.rvList);
        rvUserList.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvUserList.setLayoutManager(mLayoutManager);
        rvUserList.setItemAnimator(new DefaultItemAnimator());*/

        cardStack = (SwipeDeck) rootView.findViewById(R.id.swipe_deck);
        cardStack.setHardwareAccelerationEnabled(true);

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("Activity", "card was swiped left, position in adapter: " + position);
            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("Activity", "card was swiped right, position in adapter: " + position);
            }

            @Override
            public void cardsDepleted() {
                Log.i("Activity", "no more cards");
            }

            @Override
            public void cardActionDown() {
                Log.i("Activity", "cardActionDown");
            }

            @Override
            public void cardActionUp() {
                Log.i("Activity", "cardActionUp");
            }

        });
        setData();
    }

    private void setData() {

        final List<UserProfile> profilesList=new ArrayList<>();
        /*Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();*/

        int[] avatars = {
                R.drawable.anastasia,
                R.drawable.andriy,
                R.drawable.dmitriy,
                R.drawable.dmitry_96,
                R.drawable.ed,
                R.drawable.illya,
                R.drawable.kirill,
                R.drawable.konstantin,
                R.drawable.oleksii,
                R.drawable.pavel,
                R.drawable.vadim};
        String[] names = getResources().getStringArray(R.array.array_names);

        for (int i = 0; i < avatars.length; i++) {
            UserProfile user=new UserProfile();
            user.AVATAR=avatars[i];
            user.NAME=names[i];
            user.DESCRIPTION_FULL=getString(R.string.lorem_ipsum_long);
            user.DESCRIPTION_SHORT=getString(R.string.lorem_ipsum_short);
            profilesList.add(user);
        }

        adapter = new SwipeDeckAdapter(profilesList, getActivity());
        cardStack.setAdapter(adapter);
      /*  rvUserList.setAdapter(new UserProfileListAdapter(getActivity(), profilesList, new UserProfileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserProfile userProfile) {
                Toast.makeText(getActivity(),"Item click" + userProfile.NAME,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(), ProfileActivity.class);
               // intent.putExtra("userProfile", (Serializable) userProfile);
                startActivity(intent);
            }
        }));*/
    }


    public class SwipeDeckAdapter extends BaseAdapter {

        private List<UserProfile> mData;
        private Context context;

        CircularImageView mListItemAvatar;
        TextView mListItemName,txtOccupation,txtViewProfile;
        TextView txtDscr;

        public SwipeDeckAdapter(List<UserProfile> data, Context context) {
            this.mData = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            View v = convertView;
            if (v == null) {
              //  LayoutInflater inflater = getLayoutInflater(context);
                 v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_profile, parent, false);
                // normally use a viewholder
                //v = inflater.inflate(R.layout.list_user_profile, parent, false);
            }
            mListItemAvatar = (CircularImageView) v.findViewById(R.id.image_view_avatar);
            mListItemName = (TextView) v.findViewById(R.id.txtName);
            txtDscr = (TextView) v.findViewById(R.id.txtDscr);
            txtOccupation = (TextView) v.findViewById(R.id.txtOccupation);
            txtViewProfile = (TextView) v.findViewById(R.id.txtViewProfile);

            //((TextView) v.findViewById(R.id.textView2)).setText(data.get(position));
            Picasso.with(context).load((Integer) mData.get(position).AVATAR)
                    //.resize(EuclidActivity.sScreenWidth, EuclidActivity.sProfileImageHeight).centerCrop()
                    .placeholder(R.color.blue)
                    .into(mListItemAvatar);

            mListItemName.setText(mData.get(position).NAME.toUpperCase());
            txtDscr.setText((String) mData.get(position).DESCRIPTION_SHORT);

            return v;
        }
    }
}

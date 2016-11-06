package gifteconomy.dem.com.gifteconomy.home.aapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.home.model.UserThing;

/**
 * Created by Workstation01 on 7/21/2016.
 */
public class UserThingListAdapter extends RecyclerView.Adapter<UserThingListAdapter.ViewHolder> {

    private final Context context;
    private List<UserThing> mData;
    private OnItemClickListener listener;


    public UserThingListAdapter(Context context, List<UserThing> data, OnItemClickListener listener) {
        // super();
        this.context = context;
        mData = data;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_things, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(context).load((Integer) mData.get(position).thingImage)
                //.resize(EuclidActivity.sScreenWidth, EuclidActivity.sProfileImageHeight).centerCrop()
                .placeholder(R.color.blue)
                .into(holder.mListItemAvatar);

        holder.mListItemName.setText(mData.get(position).thing.toUpperCase());
        holder.mListItemDescription.setText((String) mData.get(position).thingDesc);

        holder.bind(mData.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(UserThing userThing);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //View mViewOverlay;
        CircularImageView mListItemAvatar;
        TextView mListItemName;
        TextView mListItemDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            mListItemAvatar = (CircularImageView) itemView.findViewById(R.id.image_view_avatar);
            mListItemName = (TextView) itemView.findViewById(R.id.text_view_name);
            mListItemDescription = (TextView) itemView.findViewById(R.id.text_view_description);
        }

        public void bind(final UserThing userThing, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(userThing);
                }
            });
        }
    }
}


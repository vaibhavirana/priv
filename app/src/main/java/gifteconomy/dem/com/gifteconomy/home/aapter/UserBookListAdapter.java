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
import gifteconomy.dem.com.gifteconomy.home.model.UserBook;

/**
 * Created by Workstation01 on 7/21/2016.
 */
public class UserBookListAdapter extends RecyclerView.Adapter<UserBookListAdapter.ViewHolder> {

    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION_SHORT = "description_short";
    public static final String KEY_DESCRIPTION_FULL = "description_full";

    private final Context context;
    private List<UserBook> mData;
    private OnItemClickListener listener;


    public UserBookListAdapter(Context context, List<UserBook> data, OnItemClickListener listener) {
        // super();
        this.context = context;
        mData = data;
        this.listener=listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_book, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(context).load((Integer) mData.get(position).bookImage)
                //.resize(EuclidActivity.sScreenWidth, EuclidActivity.sProfileImageHeight).centerCrop()
                .placeholder(R.color.blue)
                .into(holder.mListItemAvatar);

        holder.mListItemName.setText(mData.get(position).book.toUpperCase());
        holder.mListItemDescription.setText((String) mData.get(position).bookDesc);

        holder.bind(mData.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(UserBook userBook);
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

        public void bind(final UserBook userBook, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(userBook);
                }
            });
        }
    }
}


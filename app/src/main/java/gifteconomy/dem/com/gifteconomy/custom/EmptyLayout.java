package gifteconomy.dem.com.gifteconomy.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.constant.Cons;


/**
 * Created by sagartahelyani on 20-06-2016.
 */
public class EmptyLayout extends LinearLayout {

    private Context context;
    private View rootView;
    private LayoutInflater inflater;

    private ImageView emptyImage;
    private TextView emptyTextView;

    public EmptyLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = inflater.inflate(R.layout.layout_empty_view, this, true);

        emptyImage = (ImageView) rootView.findViewById(R.id.emptyImage);
        emptyTextView = (TextView) rootView.findViewById(R.id.emptyTextView);

        //emptyTextView.setTypeface(Functions.getRegularFont(context));
    }

    public void setType(int emptyType) {
        switch (emptyType) {
            case Cons.NO_USER:
                emptyImage.setImageResource(R.drawable.user);
                emptyTextView.setText("No user");
                break;

            case Cons.NO_SKILL:
                emptyImage.setImageResource(R.drawable.user);
                emptyTextView.setText("No Skill");
                break;

            case Cons.NO_THINGS:
                emptyImage.setImageResource(R.drawable.user);
                emptyTextView.setText(context.getString(R.string.no_things));
                break;

            case Cons.NO_BOOK:
                emptyImage.setImageResource(R.drawable.user);
               // emptyImage.setColorFilter(R.color.colorAccent, PorterDuff.Mode.SRC_ATOP);
                emptyTextView.setText("No Book");
                break;

        }
    }
}

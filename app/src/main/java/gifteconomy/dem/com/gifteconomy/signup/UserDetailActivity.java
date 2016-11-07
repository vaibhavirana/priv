package gifteconomy.dem.com.gifteconomy.signup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.constant.Cons;


/**
 * Created by Workstation01 on 7/16/2016.
 */
public class UserDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_PHOTO = 100;
    private CircularImageView circularImageView;
    private RadioGroup rgGender;
    private EditText edtAge, edtIntro;
    private Button btnInterest, btnSubmit;
    private String gender,Skills="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        init();

    }

    private void init() {
        circularImageView = (CircularImageView) findViewById(R.id.avtarImg);
        circularImageView.setOnClickListener(this);
// Set Border
        circularImageView.setBorderColor(getResources().getColor(R.color.colorPrimary));
        circularImageView.setBorderWidth(10);
// Add Shadow with default param
        //circularImageView.addShadow();
// or with custom param
        //circularImageView.setShadowRadius(15);
        // circularImageView.setShadowColor(Color.RED);
        gender = Cons.male;
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdMale) {
                    //Toast.makeText(UserDetailActivity.this, "Male", Toast.LENGTH_SHORT).show();
                    gender = Cons.male;
                    //  circularImageView.setImageResource(R.drawable.boy);
                } else {
                    //Toast.makeText(UserDetailActivity.this, "FeMale", Toast.LENGTH_SHORT).show();
                    gender = Cons.female;
                    //circularImageView.setImageResource(R.drawable.girl);
                }
            }
        });

        edtAge = (EditText) findViewById(R.id.edtAge);
        edtIntro = (EditText) findViewById(R.id.edtIntro);

        btnInterest = (Button) findViewById(R.id.btnInterest);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnInterest.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInterest:
                showInterestPopup();
                break;

            case R.id.btnSubmit:
                AttemptSubmit();
                break;

            case R.id.avtarImg:
                showAvtarPopup();
                break;
        }
    }

    private void showAvtarPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_pic);
        builder.setMessage(R.string.choose_pic_message);
        //builder.setBody(R.string.choose_interest);

        // Add the buttons
        builder.setPositiveButton(R.string.choose_pic, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // when clicked OK button
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        // Set other dialog properties

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showInterestPopup() {
        AlertDialog.Builder builder;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.choose_interest, null);
        GridView gridview = (GridView) layout.findViewById(R.id.gridInterest);
        gridview.setAdapter(new GridElementAdapter(this));


        builder = new AlertDialog.Builder(this);
        builder.setView(layout);

        final AlertDialog dialog = builder.create();
        dialog.show();
        Button btnCancel = (Button) layout.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnOk = (Button) layout.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("skills",Skills);
                dialog.dismiss();
            }
        });
    }

    private void AttemptSubmit() {
        // Reset errors.
        edtAge.setError(null);
        edtIntro.setError(null);

        boolean cancel = false;
        View focusView = null;

        String age = edtAge.getText().toString();
        String intro = edtIntro.getText().toString();

        if (TextUtils.isEmpty(age)) {
            edtAge.setError(getString(R.string.error_field_required));
            focusView = edtAge;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = imageReturnedIntent.getData();
                        InputStream imageStream = null;
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap img = BitmapFactory.decodeStream(imageStream);
                        circularImageView.setImageBitmap(img);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }


    private String[] categoryContent = {
            "Pubs", "Restuarants", "shopping",
            "theatre", "train", "taxi",
            "gas", "police", "hospital"
    };

    public class GridElementAdapter extends BaseAdapter {

        //String[] data;
        Context context;
        LayoutInflater layoutInflater;


        public GridElementAdapter(Context context) {
            super();
            // this.data = data;
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            return categoryContent.length;
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            convertView = layoutInflater.inflate(R.layout.item_interest, null);

            final TextView txt = (TextView) convertView.findViewById(R.id.txtinterest);

            txt.setText(categoryContent[position]);

            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        txt.setTextColor(Color.WHITE);
                        txt.setBackground(getResources().getDrawable(R.drawable.fill_rectangle));
                        Skills=Skills+","+txt.getText();
                }
            });

            return convertView;
        }

    }

}

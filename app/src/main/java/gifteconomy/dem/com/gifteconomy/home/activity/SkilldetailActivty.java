package gifteconomy.dem.com.gifteconomy.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.custom.MDDialog;
import gifteconomy.dem.com.gifteconomy.like.LikeButton;
import gifteconomy.dem.com.gifteconomy.ui.ProfileActivity;

/**
 * Created by vaibhavirana on 04-08-2016.
 */
public class SkilldetailActivty extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
  //  private Userskill userskill;

    private ImageView imgBook;
    private TextView txtBookName,txtBookAuthorName,txtBookDesc,txtUserName,txtShare,txtFav;
    private CircularImageView imgUser;
    private LikeButton heart_button;
    private LinearLayout llUser;
    private FloatingActionButton fabRequestBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        //userskill = (Userskill) getIntent().getExtras().getSerializable("userskill");
        //Log.e("userBook", userskill.skill);
        initToolbar();
        init();
    }

    private void init() {
        fabRequestBook = (FloatingActionButton) findViewById(R.id.fabRequestBook);
        fabRequestBook.setOnClickListener(this);

        imgBook=(ImageView)findViewById(R.id.imgBook);
        imgBook.setImageDrawable(getResources().getDrawable(R.drawable.skill));

        txtBookName=(TextView)findViewById(R.id.txtBookName);
        txtBookAuthorName=(TextView)findViewById(R.id.txtBookAuthorName);
        txtBookDesc=(TextView)findViewById(R.id.txtBookDesc);
        txtUserName=(TextView)findViewById(R.id.txtUserName);
        txtShare=(TextView)findViewById(R.id.txtShare);
        txtFav=(TextView)findViewById(R.id.txtFav);
        imgUser=(CircularImageView) findViewById(R.id.imgUser);
        heart_button=(LikeButton)findViewById(R.id.heart_button);
        llUser=(LinearLayout) findViewById(R.id.llUser);

        llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SkilldetailActivty.this,ProfileActivity.class);
                startActivity(intent);
            }
        });

        txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null)
            toolbar.setTitle("Skill");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabRequestBook:
               // Functions.fireIntent(this, AddBookActivity.class);
                showPopup();
                break;
        }
    }

    private void showPopup() {
        new MDDialog.Builder(this)
                .setContentView(R.layout.layout_request_borrow)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        setLayoutDetail(contentView);
                    }
                })
                .setShowTitle(false)
                .setWidthMaxDp(600)
                .setShowButtons(false)
                .create()
                .show();
    }

    private void setLayoutDetail(View contentView) {
        final EditText edtDuration=(EditText)contentView.findViewById(R.id.edtDuration);
        final EditText edtMsg=(EditText)contentView.findViewById(R.id.edtMsg);
        Button btnSend=(Button) contentView.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtDuration.getText().length()==0)
                {
                    edtDuration.setError("Please enter duration");
                }else if(edtMsg.getText().length()==0)
                {
                    edtMsg.setError("Please enter Message");
                }else
                {
                    // do action
                    Toast.makeText(SkilldetailActivty.this,"Message send successfully",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

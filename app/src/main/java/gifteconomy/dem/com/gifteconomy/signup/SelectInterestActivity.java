package gifteconomy.dem.com.gifteconomy.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import gifteconomy.dem.com.gifteconomy.R;
import gifteconomy.dem.com.gifteconomy.signup.API.InterestApi;
import gifteconomy.dem.com.gifteconomy.signup.model.InterestResponse;
import gifteconomy.dem.com.gifteconomy.utils.MyApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectInterestActivity extends AppCompatActivity {

    private EditText edtInterest;
    private ArrayList<String> interest;
    private ArrayList<Integer> interestid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interest);

        init();
    }

    private void init() {
        initToolbar();
        edtInterest = (EditText) findViewById(R.id.edtInterest);

        InterestApi userApiService = MyApplication.retrofit.create(InterestApi.class);
        Call<InterestResponse> call = userApiService.loadInterest();

        call.enqueue(new Callback<InterestResponse>() {
            @Override
            public void onResponse(Call<InterestResponse> call, Response<InterestResponse> response) {
                Log.d("onResponse", response.body().toString());
                //resp = response.body();
               // callXyz();
                InterestResponse response1=response.body();
                setInterestArray(response1);
            }

            @Override
            public void onFailure(Call<InterestResponse> call, Throwable t) {
                Log.e("onResponse", t.toString());
            }
        });


    }

    private void setInterestArray(InterestResponse response1) {
        interest=new ArrayList<>();
        interestid=new ArrayList<>();
        for (int i=0;i<response1.skill.size();i++) {
            Log.d("onResponse", response1.skill.get(i).toString());
            interest.add(response1.skill.get(i).skillname);
            interestid.add(response1.skill.get(i).id);
        }
        actionListener();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null)
            toolbar.setTitle("Your Wish List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void actionListener() {
        edtInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(SelectInterestActivity.this)
                        .title("Select Interest")
                        .items(interest)
                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                /**
                                 * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected check box to actually be selected.
                                 * See the limited multi choice dialog example in the sample project for details.
                                 **/
                                StringBuilder str = new StringBuilder();
                                for (int i = 0; i < which.length; i++) {
                                    if (i > 0) str.append(',');
                                    //str.append(which[i]);
                                    str.append(text[i]);
                                }
                               // showToast(str.toString());
                                edtInterest.setText(str.toString());
                                return true;
                            }
                        })
                        .positiveText(android.R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();
            }
        });
    }
}

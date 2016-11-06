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
import gifteconomy.dem.com.gifteconomy.helper.Functions;
import gifteconomy.dem.com.gifteconomy.home.aapter.UserBookListAdapter;
import gifteconomy.dem.com.gifteconomy.home.activity.AddBookActivity;
import gifteconomy.dem.com.gifteconomy.home.activity.BookdetailActivty;
import gifteconomy.dem.com.gifteconomy.home.model.UserBook;

/**
 * Created by Workstation01 on 7/20/2016.
 */
public class BookFragment extends Fragment implements View.OnClickListener
{
    private View rootView;
    private RecyclerView rvBookList;
    private FloatingActionButton fabAddBook;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_book, container, false);
        init();
        return rootView;
    }
    private void init() {
        fabAddBook = (FloatingActionButton) rootView.findViewById(R.id.fabAddBook);
        fabAddBook.setOnClickListener(this);

        rvBookList=(RecyclerView)rootView.findViewById(R.id.rvList);
        rvBookList.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvBookList.setLayoutManager(mLayoutManager);
        rvBookList.setItemAnimator(new DefaultItemAnimator());


        setData();
    }

    private void setData() {

        final List<UserBook> bookList=new ArrayList<>();
        /*Map<String, Object> profileMap;
        List<Map<String, Object>> profilesList = new ArrayList<>();*/

        int[] avatars = {
                R.drawable.book,
                R.drawable.book,
                R.drawable.book};

        String[] names = getResources().getStringArray(R.array.array_names);

        for (int i = 0; i < avatars.length; i++) {
            UserBook userBook=new UserBook();
            userBook.bookImage=avatars[i];
            userBook.book=names[i];
            userBook.bookDesc=getString(R.string.lorem_ipsum_short);
            //userSkill.DESCRIPTION_SHORT=getString(R.string.lorem_ipsum_short);
            bookList.add(userBook);
        }

        rvBookList.setAdapter(new UserBookListAdapter(getActivity(), bookList, new UserBookListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(UserBook userBook) {
                Toast.makeText(getActivity(),"Item click" + userBook.book,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(), BookdetailActivty.class);
                intent.putExtra("userBook", (Serializable) userBook);
                startActivity(intent);
            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddBook:
                Functions.fireIntent(getActivity(), AddBookActivity.class);
                break;
        }
    }
}

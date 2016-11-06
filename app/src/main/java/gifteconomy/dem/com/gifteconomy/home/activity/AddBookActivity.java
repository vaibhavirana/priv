package gifteconomy.dem.com.gifteconomy.home.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import gifteconomy.dem.com.gifteconomy.R;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class AddBookActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imgAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        init();
    }

    private void init() {
        initToolbar();

        imgAdd = (ImageView) findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddBookActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    captureImage();
                    dialog.dismiss();

                } else if (items[item].equals("Choose from Gallery")) {
                    fromGallery();
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void fromGallery() {
        RxImagePicker.with(AddBookActivity.this).requestImage(Sources.GALLERY)
                .flatMap(new Func1<Uri, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(Uri uri) {
                        return RxImageConverters.uriToBitmap(AddBookActivity.this, uri);
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        imgAdd.setImageBitmap(bitmap);
                    }
                });
    }

    private void captureImage() {
        RxImagePicker.with(AddBookActivity.this).requestImage(Sources.CAMERA)
                .flatMap(new Func1<Uri, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(Uri uri) {
                        return RxImageConverters.uriToBitmap(AddBookActivity.this, uri);
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        imgAdd.setImageBitmap(bitmap);
                    }
                });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null)
            toolbar.setTitle("Add Book");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
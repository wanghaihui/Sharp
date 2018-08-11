package com.conquer.sharp.photo.system;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择系统照片Gallery和拍照功能--直接调用系统方法实现
 */
public class PhotoSelectActivity extends BaseActivity {
    private static final String TAG = "PhotoSelectActivity";

    public static final int PICK_IMAGE = 1;

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_photo_select);
        ButterKnife.bind(this);

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });
    }

    private void selectPhoto() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "选择照片");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { pickIntent });

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        return;
                    }

                    Uri uri = data.getData();
                    if (uri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            ivAvatar.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            Bitmap bitmap = bundle.getParcelable("data");
                            ivAvatar.setImageBitmap(bitmap);
                        }
                    }
                }
                break;
        }
    }
}

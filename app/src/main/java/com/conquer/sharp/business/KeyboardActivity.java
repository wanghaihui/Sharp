package com.conquer.sharp.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.conquer.sharp.R;

import butterknife.ButterKnife;

public class KeyboardActivity extends AppCompatActivity {

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, KeyboardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_keyboard);
        ButterKnife.bind(this);
    }
}

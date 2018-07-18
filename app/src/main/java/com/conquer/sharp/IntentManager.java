package com.conquer.sharp;

import android.content.Context;
import android.content.Intent;

import com.conquer.sharp.danmaku.DanMuActivity;

/**
 * Created by ac on 18/7/17.
 *
 */

public class IntentManager {

    public static void intentDanMu(Context context) {
        Intent intent = new Intent(context, DanMuActivity.class);
        context.startActivity(intent);
    }

}

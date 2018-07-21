package com.conquer.sharp;

import android.content.Context;
import android.content.Intent;

import com.conquer.sharp.danmaku.DanMuActivity;
import com.conquer.sharp.danmaku.ksong.DanMuKSongActivity;

/**
 * Created by ac on 18/7/17.
 *
 */

public class IntentManager {

    public static void intentDanMu(Context context) {
        Intent intent = new Intent(context, DanMuActivity.class);
        context.startActivity(intent);
    }

    public static void intentDanMuKSong(Context context) {
        Intent intent = new Intent(context, DanMuKSongActivity.class);
        context.startActivity(intent);
    }
}

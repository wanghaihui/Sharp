package com.conquer.sharp;

import android.content.Context;
import android.content.Intent;

import com.conquer.sharp.danmaku.DanMuActivity;
import com.conquer.sharp.danmaku.ksong.DanMuKSongActivity;
import com.conquer.sharp.danmaku.normal.DanMu3Activity;
import com.conquer.sharp.danmaku.recycler.DanMu4Activity;

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

    public static void intentDanMu3(Context context) {
        Intent intent = new Intent(context, DanMu3Activity.class);
        context.startActivity(intent);
    }

    public static void intentDanMuContent(Context context) {
        Intent intent = new Intent(context, DanMu4Activity.class);
        context.startActivity(intent);
    }
}

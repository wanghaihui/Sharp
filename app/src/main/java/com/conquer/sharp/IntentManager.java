package com.conquer.sharp;

import android.content.Context;
import android.content.Intent;

import com.conquer.sharp.base.glide.GlideApp;
import com.conquer.sharp.business.LuckyActivity;
import com.conquer.sharp.business.VerticalSeekBarActivity;
import com.conquer.sharp.business.WaitNotifyActivity;
import com.conquer.sharp.camera.CameraActivity;
import com.conquer.sharp.cs.CSActivity;
import com.conquer.sharp.deeplink.DeepLinkActivity;
import com.conquer.sharp.dialog.DialogActivity;
import com.conquer.sharp.glide.GlideActivity;
import com.conquer.sharp.guide.GuideActivity;
import com.conquer.sharp.leak.LeakActivity;
import com.conquer.sharp.opengl.OpenGLActivity;
import com.conquer.sharp.photo.pager.DanMuActivity;
import com.conquer.sharp.danmaku.normal.DanMu3Activity;
import com.conquer.sharp.danmaku.recycler.DanMu4Activity;
import com.conquer.sharp.photo.system.PhotoSelectActivity;

/**
 * Created by ac on 18/7/17.
 *
 */

public class IntentManager {

    public static void intentDanMu(Context context) {
        Intent intent = new Intent(context, DanMuActivity.class);
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

    public static void intentPhotoSelect(Context context) {
        Intent intent = new Intent(context, PhotoSelectActivity.class);
        context.startActivity(intent);
    }

    public static void intentDialog(Context context) {
        Intent intent = new Intent(context, DialogActivity.class);
        context.startActivity(intent);
    }

    public static void intentDeepLink(Context context) {
        Intent intent = new Intent(context, DeepLinkActivity.class);
        context.startActivity(intent);
    }

    public static void intentOpenGL(Context context) {
        Intent intent = new Intent(context, OpenGLActivity.class);
        context.startActivity(intent);
    }

    public static void intentLucky(Context context) {
        Intent intent = new Intent(context, LuckyActivity.class);
        context.startActivity(intent);
    }

    public static void intentVerticalSeekBar(Context context) {
        Intent intent = new Intent(context, VerticalSeekBarActivity.class);
        context.startActivity(intent);
    }

    public static void intentWaitNotify(Context context) {
        Intent intent = new Intent(context, WaitNotifyActivity.class);
        context.startActivity(intent);
    }

    public static void intentCamera(Context context) {
        Intent intent = new Intent(context, CameraActivity.class);
        context.startActivity(intent);
    }

    public static void intentCS(Context context) {
        Intent intent = new Intent(context, CSActivity.class);
        context.startActivity(intent);
    }

    public static void intentGuide(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

    public static void intentLeak(Context context) {
        Intent intent = new Intent(context, LeakActivity.class);
        context.startActivity(intent);
    }

    public static void intentGlide(Context context) {
        Intent intent = new Intent(context, GlideActivity.class);
        context.startActivity(intent);
    }
}

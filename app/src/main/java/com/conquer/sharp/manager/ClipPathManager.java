package com.conquer.sharp.manager;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ClipPathManager implements ClipManager {

    private final Path path = new Path();
    private final Paint paint = new Paint();

    // 路径创建器
    private ClipPathCreator createClipPath = null;

    public ClipPathManager() {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
    }

    public Paint getPaint() {
        return paint;
    }

    @Override
    public boolean requiresBitmap() {
        return createClipPath != null && createClipPath.requiresBitmap();
    }

    @Nullable
    private Path createClipPath(int width, int height) {
        if (createClipPath != null) {
            return createClipPath.createClipPath(width, height);
        }
        return null;
    }

    public void setClipPathCreator(ClipPathCreator createClipPath) {
        this.createClipPath = createClipPath;
    }

    @NonNull
    @Override
    public Path createMask(int width, int height) {
        return path;
    }

    @Nullable
    @Override
    public Path getShadowConvexPath( ){
        return path;
    }

    @Override
    public void setupClipLayout(int width, int height) {
        path.reset();
        final Path clipPath = createClipPath(width, height);
        if (clipPath != null) {
            path.set(clipPath);
        }
    }

    public interface ClipPathCreator {
        Path createClipPath(int width, int height);
        boolean requiresBitmap();
    }
}

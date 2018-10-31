package com.conquer.sharp.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;

public class OpenGLActivity extends BaseActivity {

    private GLSurfaceView mGLView;

    private MyGLRenderer mRenderer;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        // setContentView(R.layout.activity_opengl);
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);

        mGLView.setVisibility(View.GONE);

        mRenderer = new MyGLRenderer();
        mGLView.setRenderer(mRenderer);
        mGLView.setVisibility(View.VISIBLE);
        Log.d("haihui", "set renderer");
    }

    // 内部类
    // 内部类实现了更好的封装，除了外部类，其他类都不能访问
    // 无限制的访问外部类的元素
    // 默认持有外部类的引用
    // static修饰的静态内部类不持有外部类的引用
    class MyGLSurfaceView extends GLSurfaceView {

        //private final MyGLRenderer mRenderer;

        public MyGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(2);

            //mRenderer = new MyGLRenderer();
            //setRenderer(mRenderer);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            super.surfaceCreated(holder);
            Log.d("haihui", "surface created");
        }
    }
}

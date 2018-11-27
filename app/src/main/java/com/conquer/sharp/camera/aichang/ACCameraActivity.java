package com.conquer.sharp.camera.aichang;

import android.hardware.Camera;
import android.os.Bundle;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;

public class ACCameraActivity extends BaseActivity {

    private Camera camera;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ai_camera);
    }



    private void releaseCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}

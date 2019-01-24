package com.wc.camera;

public abstract class CameraViewImpl {

    public interface Callback {
        void onCameraOpened();
        void onCameraClosed();
        void onPictureTaken(byte[] data);
    }


}

package com.conquer.sharp.camera.base;

public abstract class CameraViewImpl {

    public interface Callback {
        void onCameraOpened();
        void onCameraClosed();
        void onPictureTaken(byte[] data);
    }
}

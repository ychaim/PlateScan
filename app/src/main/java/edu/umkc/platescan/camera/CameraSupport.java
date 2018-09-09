package edu.umkc.platescan.camera;

public interface CameraSupport {
    CameraSupport open(int cameraId);
    int getOrientation(int cameraId);
}
package com.mineexcellence.sblastingapp.utils;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;

public class FlashLightBlinkUtils {

    Context context;
    boolean isOn = false;
    boolean isBlinking = false;
    Handler handler = new Handler();
    CameraManager camManager = null;
    String cameraId = null;
    Thread blinkThread;

    public FlashLightBlinkUtils(Context context) {
        this.context = context;
    }

    public void setCamManager() {
        camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        blinkFlash();
    }

    public void blinkFlash() {
        if(!isBlinking){
            BlinkMode();
            isBlinking = true;
        } else {
            if(blinkThread != null){
                blinkThread.interrupt(); // stop blinking
                offFlash();
            }
            isBlinking = false;
        }
    }

    public void BlinkMode(){
        blinkThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(100);

                        if(!isOn){
                            onFlash();
                            isOn = true;
                        } else {
                            isOn = false;
                            offFlash();
                        }

                    }
                } catch (Exception e) {
                }
            }
        };
        blinkThread.start();
    }

    public void onFlash(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, true);   //Turn ON
                isOn = true;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void offFlash(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, false);   //Turn ON
                isOn = false;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopBlink() {
        if (blinkThread != null) {
            try {
                blinkThread.interrupt();
                isBlinking = false;
                blinkThread = null;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        offFlash();
    }

}

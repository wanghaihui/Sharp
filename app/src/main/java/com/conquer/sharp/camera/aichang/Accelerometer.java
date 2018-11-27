package com.conquer.sharp.camera.aichang;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Accelerometer 用于开启重力传感器，以获得当前手机朝向
 */
public class Accelerometer {
    /**
     *
     * CLOCKWISE_ANGLE为手机旋转角度
     * 其Deg0定义如下图所示
     *  ___________________
     * | +--------------+  |
     * | |              |  |
     * | |              |  |
     * | |              | O|
     * | |              |  |
     * | |______________|  |
     * ---------------------
     * 顺时针旋转后得到Deg90，即手机竖屏向上，如下图所示
     *  ___________
     * |           |
     * |+---------+|
     * ||         ||
     * ||         ||
     * ||         ||
     * ||         ||
     * ||         ||
     * |+---------+|
     * |_____O_____|
     */

    public enum CLOCKWISE_ANGLE {
        Deg0(0), Deg90(1), Deg180(2), Deg270(3);
        private int value;
        CLOCKWISE_ANGLE(int value){
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    private SensorManager sensorManager = null;

    private boolean hasStarted = false;

    private static CLOCKWISE_ANGLE rotation;

    public Accelerometer(Context ctx) {
        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        rotation = CLOCKWISE_ANGLE.Deg0;
    }

    public void start() {
        if (hasStarted) return;
        hasStarted = true;
        rotation = CLOCKWISE_ANGLE.Deg0;
        sensorManager.registerListener(accListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        if (!hasStarted) return;
        hasStarted = false;
        sensorManager.unregisterListener(accListener);
    }

    static public int getDirection() {
        if (rotation != null) {
            return rotation.getValue();
        }
        return CLOCKWISE_ANGLE.Deg0.getValue();
    }

    private SensorEventListener accListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];

                if (Math.abs(x) > 3 || Math.abs(y) > 3) {
                    if (Math.abs(x) > Math.abs(y)) {
                        if (x > 0) {
                            rotation = CLOCKWISE_ANGLE.Deg0;
                        } else {
                            rotation = CLOCKWISE_ANGLE.Deg180;
                        }
                    } else {
                        if (y > 0) {
                            rotation = CLOCKWISE_ANGLE.Deg90;
                        } else {
                            rotation = CLOCKWISE_ANGLE.Deg270;
                        }
                    }
                }
            }
        }
    };
}

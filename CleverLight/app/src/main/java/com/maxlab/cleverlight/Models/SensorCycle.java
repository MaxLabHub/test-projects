package com.maxlab.cleverlight.Models;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorCycle extends HelperCycle {
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorListener;
    private boolean isNear, isNearOld;
//    private float power;
//    private String data;

    public SensorCycle() {
        sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged( SensorEvent event ) {
                if ( event.sensor.getType() == Sensor.TYPE_PROXIMITY ) {
                    isNear = event.values[0] < event.sensor.getMaximumRange();
                    if ( isNear != isNearOld && null != listener ) {
                        listener.onProximityChanged( isNear );
                    }
                    isNearOld = isNear;
                }
            }

            @Override
            public void onAccuracyChanged( Sensor sensor, int accuracy ) {

            }
        };
    }

    public static SensorCycle create() {
        return new SensorCycle();
    }

    public SensorCycle init( final Context context ) {
        sensorManager = ( SensorManager ) context.getSystemService( Context.SENSOR_SERVICE);
        cleanup();
        if ( sensorManager != null ) {
//            List< Sensor > sensors =  sensorManager.getSensorList( Sensor.TYPE_ALL );
//            StringBuilder sensorsStr = new StringBuilder();
//            for ( Sensor sensor : sensors ) {
//                sensorsStr.append( sensorMeaningByType( sensor.getType() ) ).append( "\n" );
//            }
//            data = sensorsStr.toString();
            sensor = sensorManager.getDefaultSensor( Sensor.TYPE_PROXIMITY );
            sensorManager.registerListener( sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL );
        }
        return this;
    }

    public boolean getValue() {
        return isNear;
    }

    public final String sensorMeaningByType( final int type ) {
        switch ( type ) {
            case Sensor.TYPE_ACCELEROMETER:
                return "ACCELEROMETER";
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return "AMBIENT TEMPERATURE";
            case Sensor.TYPE_GRAVITY:
                return "GRAVITY";
            case Sensor.TYPE_GYROSCOPE:
                return "GYROSCOPE";
            case Sensor.TYPE_LIGHT:
                return "LIGHT";
            case Sensor.TYPE_LINEAR_ACCELERATION:
                return "LINEAR ACCELERATION";
            case Sensor.TYPE_MAGNETIC_FIELD:
                return "MAGNETIC FIELD";
            case Sensor.TYPE_ORIENTATION:
                return "ORIENTATION";
            case Sensor.TYPE_PRESSURE:
                return "PRESSURE";
            case Sensor.TYPE_PROXIMITY:
                return "PROXIMITY";
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return "RELATIVE HUMIDITY";
            case Sensor.TYPE_ROTATION_VECTOR:
                return "ROTATION VECTOR";
            case Sensor.TYPE_TEMPERATURE:
                return "TEMPERATURE";
            default:
                return String.valueOf( type );
        }
    }

    @Override
    public void cleanup() {
        if ( null != sensorManager && null != sensorListener ) {
            sensorManager.unregisterListener( sensorListener );
        }
        super.cleanup();
    }
}

package com.maxlab.cleverlight;

import android.util.Log;

import com.maxlab.cleverlight.Models.EventsListener;
import com.maxlab.cleverlight.Models.HelperCycle;
import com.maxlab.cleverlight.Models.SensorCycle;
import com.maxlab.cleverlight.Models.TimingCycle;
import com.maxlab.cleverlight.Views.ViewEvents;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Presenter implements EventsListener {
    private static final String TAG = "Presenter";
    private WeakReference< ViewEvents > viewRef;

    // Models
    SensorCycle sensorCycle;
    TimingCycle timingCycle;
    ArrayList< HelperCycle > cycles;

    public Presenter( final ViewEvents viewEvents ) {
        viewRef = new WeakReference<>( viewEvents );

        sensorCycle = SensorCycle.create();
        sensorCycle.init( viewRef.get().getContext() );
        timingCycle = TimingCycle.create();

        cycles = new ArrayList<>();
        cycles.add( sensorCycle );
        cycles.add( timingCycle );

        for ( HelperCycle cycle : cycles ) {
            cycle.setOnSensorsChangedListener( this );
        }
    }

    public static Presenter create( final ViewEvents viewRef ) {
        return new Presenter( viewRef );
    }

    public void onViewCreate() {
        if ( checkContextLost() ) return;

        timingCycle.start();
    }

    public void onViewDestroy() {
        if ( checkContextLost() ) return;

        for ( HelperCycle cycle : cycles ) {
            cycle.cleanup();
        }
        cycles.clear();
    }

    public void turnScreenOnOff( final boolean isOn ) {
        if ( checkContextLost() ) return;
        viewRef.get().setBrightness( isOn ? 1 : 0 );
    }

    public void setTimeout() {
        timingCycle.setTimeout( viewRef.get().getTimeout() );
        timingCycle.start();
    }

    public int getTimeout() {
        return timingCycle.getTimeout();
    }

    public boolean checkContextLost() {
        if ( null == viewRef.get() ) {
            Log.e( TAG, "View reference is empty!" );
            return true;
        }
        return false;
    }

    @Override
    public void onProximityChanged( boolean isNear ) {
        if ( checkContextLost() ) return;

        if ( isNear ) {
            timingCycle.start();
        }
    }

    @Override
    public void onTimeStarted( int seconds ) {
        turnScreenOnOff( true );
    }

    @Override
    public void onTimeChanged( int seconds ) {
        // TODO: displaying timeout in the view
    }

    @Override
    public void onTimeElapsed() {
        turnScreenOnOff( false );
    }
}

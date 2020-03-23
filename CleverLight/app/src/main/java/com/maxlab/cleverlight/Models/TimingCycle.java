package com.maxlab.cleverlight.Models;

public class TimingCycle extends HelperCycle {
    RealTimer realTimer;
    int seconds;
    // Values
    private int Timeout = 10; // seconds

    public int getTimeout() {
        return Timeout;
    }

    public void setTimeout( final int seconds ) {
        Timeout = seconds;
    }

    public TimingCycle() {
        realTimer = RealTimer.create();
        realTimer.addOnTimeChangeListener(
                new RealTimer.Callback( RealTimer.Callback.TRIGGER_EVERY_SECOND ) {
                    @Override
                    public void onChange( long currentTime ) {
                        if ( null != listener ) {
                            listener.onTimeChanged( seconds );
                        }
                        --seconds;
                        if ( 0 == seconds ) {
                            stop();
                        }
                    }
                } );
    }

    public static TimingCycle create() {
        return new TimingCycle();
    }

    public void start() {
        seconds = Timeout;
        if ( !realTimer.isRunning() ) {
            realTimer.start();
        }
        if ( null != listener ) {
            listener.onTimeStarted( seconds );
        }
    }

    public void stop() {
        realTimer.stop();
        if ( null != listener ) {
            listener.onTimeElapsed();
        }
    }
}

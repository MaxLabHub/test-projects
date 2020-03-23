package com.maxlab.cleverlight.Models;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Max on 27.02.2018.
 */

public class RealTimer {
    private Handler updateTimersHandler = new Handler();
    private long delayTime = 30; // 30 ms by default
    private ArrayList<Callback> mListeners;
    private int mListenersSize = 0;
    private boolean isRunning = false;
//    private CallbackComparator mComparator = new CallbackComparator(); // TODO: uncomment if sorting is needed

    private Runnable timerRunnable = new Runnable() {
        private long currentTime = 0;
        private int i;
        private Callback callback;

        @Override
        public void run() {
            updateTimersHandler.removeCallbacks( this );
            currentTime = System.currentTimeMillis();
            try {
                // Cycle of all timers triggered
                if ( null != mListeners ) {
                    for ( i = 0; i < mListenersSize ; ++i ) {
                        callback = mListeners.get( i );
                        if ( callback.isTimeChanged( currentTime ) ) {
                            callback.onChange( currentTime );
                        }
                    }
                    updateTimersHandler.postDelayed( this, delayTime );
                }
            } catch ( Exception e ) {
                isRunning = false;
                throw e;
            }
        }
    };

    public void setAccuracy( final long accuracyDelayMillis ) {
        delayTime = accuracyDelayMillis;
    }

    public RealTimer( final long accuracyDelayMillis ) {
        setAccuracy( accuracyDelayMillis );
    }

    public RealTimer() {
    }

    public static RealTimer create() {
        return new RealTimer();
    }

    public void start() {
        isRunning = true;
        timerRunnable.run();
    }

    public void stop() {
        updateTimersHandler.removeCallbacks( timerRunnable );
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public RealTimer addOnTimeChangeListener( Callback listener ) {
        if ( null == mListeners ) {
            mListeners = new ArrayList<>();
        }
        mListeners.add( listener );
        mListenersSize = mListeners.size();
//        Collections.sort( mListeners, mComparator ); // TODO: uncomment if sorting is needed

        return this;
    }

    public void clearOnTimeChangeListeners() {
        if ( null != mListeners ) {
            mListeners.clear();
        }
        mListenersSize = 0;
    }

    private class CallbackComparator implements Comparator<Callback> {
        @Override
        public int compare( Callback o1, Callback o2 ) {
            return o1.triggeredInterval > o2.triggeredInterval ? 1 :
                    ( o1.triggeredInterval < o2.triggeredInterval ? -1 : 0 );
        }
    }

    public static abstract class Callback {
        public static long TRIGGER_ALWAYS = 0;
        public static long TRIGGER_EVERY_SECOND = 1000;
        public static long TRIGGER_EVERY_MINUTE = 60000;
        public static long TRIGGER_EVERY_HOUR = 3600000;

        private long prevValue = 0;
        private long triggeredInterval = 1;
        private boolean timeChanged = true;
        private boolean enabled = true;

        public boolean isTimeChanged( final long currentTime ) {
            if ( !enabled ) {
                return false;
            }
            if ( TRIGGER_ALWAYS == triggeredInterval ) {
                return true;
            }
            timeChanged = ( currentTime / triggeredInterval ) != prevValue;
            if ( timeChanged ) {
                prevValue = currentTime / triggeredInterval;
            }
            return timeChanged;
        }

        public void setInterval( final long interval ) {
            triggeredInterval = interval;
        }

        public long getInterval() {
            return triggeredInterval;
        }

        public void disable() {
            enabled = false;
        }

        public void enable() {
            enabled = true;
        }

        public Callback( final long interval ) {
            setInterval( interval );
        }

        public abstract void onChange( final long currentTime );
    }
}
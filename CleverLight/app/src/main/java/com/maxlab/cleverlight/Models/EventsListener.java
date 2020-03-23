package com.maxlab.cleverlight.Models;

public interface EventsListener {
    void onProximityChanged( final boolean isNear );
    void onTimeStarted( final int seconds );
    void onTimeChanged( final int seconds );
    void onTimeElapsed();
}

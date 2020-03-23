package com.maxlab.cleverlight.Models;

public class HelperCycle {
    protected EventsListener listener;

    public void setOnSensorsChangedListener( EventsListener eventsListener ) {
        listener = eventsListener;
    }

    public void cleanup() {
        setOnSensorsChangedListener( null );
    }
}

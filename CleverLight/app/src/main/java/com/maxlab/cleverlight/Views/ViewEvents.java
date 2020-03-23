package com.maxlab.cleverlight.Views;

import android.content.Context;

public interface ViewEvents {
    void setBrightness( float brightnessLevel );
    int getTimeout();
    void setTimeout( int seconds );
    void showToast( final String message );
    Context getContext();
}

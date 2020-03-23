package com.maxlab.cleverlight.Models;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.view.WindowManager;

public class ScreenCycle extends HelperCycle {
    private final String lockTag = "CleverLight:unlocked screen";
    PowerManager.WakeLock wl;

    public ScreenCycle() {
        //
    }

    public static ScreenCycle create() {
        return new ScreenCycle();
    }

    public void setBrightness( final Activity activity, final float value ) {
        if ( null == activity ) {
            return;
        }
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
//        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.screenBrightness = value;
        activity.getWindow().setAttributes( params );

        if ( value <= 0.001 ) {
//            params.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            if ( null != wl && wl.isHeld() ) {
                wl.release();
            }
        } else {
            if ( null == wl ) {
                PowerManager pm = ( PowerManager ) activity.getApplicationContext().getSystemService( Context.POWER_SERVICE );
                if ( pm != null ) {
                    wl = pm.newWakeLock( PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, lockTag );
                }
            }
            if ( null != wl ) {
                wl.acquire( 60 * 60000 );
            }
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        if ( null != wl ) {
            wl.release();
        }
    }
}

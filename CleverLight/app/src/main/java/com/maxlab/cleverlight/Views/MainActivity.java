package com.maxlab.cleverlight.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maxlab.cleverlight.Models.ScreenCycle;
import com.maxlab.cleverlight.Presenter;
import com.maxlab.cleverlight.R;

public class MainActivity extends AppCompatActivity implements ViewEvents {
    Presenter presenter;
    ScreenCycle screenCycle;

    Button turnScreenOffButton;
    EditText timeoutText;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        turnScreenOffButton = findViewById( R.id.turnScreenOffButton );
        turnScreenOffButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                presenter.turnScreenOnOff( false );
            }
        } );
        timeoutText = findViewById( R.id.timeoutEditText );
        timeoutText.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {
            }

            @Override
            public void onTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {
                presenter.setTimeout();
            }

            @Override
            public void afterTextChanged( Editable editable ) {
            }
        } );
        screenCycle = ScreenCycle.create();
        presenter = Presenter.create( this );
        presenter.onViewCreate();
    }

    @Override
    protected void onDestroy() {
        presenter.onViewDestroy();
        screenCycle.cleanup();
        super.onDestroy();
    }

    @Override
    public void setBrightness( float brightnessLevel ) {
        screenCycle.setBrightness( this, brightnessLevel );
    }

    @Override
    public int getTimeout() {
        try {
            return Integer.valueOf( timeoutText.getText().toString() );
        } catch ( NumberFormatException e ) {
            showToast( e.getMessage() );
        }
        return presenter.getTimeout();
    }

    @Override
    public void setTimeout( int seconds ) {
        timeoutText.setText( String.valueOf( seconds ) );
    }

    @Override
    public void showToast( String message ) {
        Toast.makeText( this.getApplicationContext(), message, Toast.LENGTH_LONG ).show();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}

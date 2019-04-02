package com.example.screenalike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.greenrobot.eventbus.EventBus;

import screenAlike.BusMessages;
import screenAlike.screenHelper;

import static screenAlike.BusMessages.MESSAGE_ACTION_STREAMING_STOP;
import static screenAlike.BusMessages.MESSAGE_ACTION_STREAMING_TRY_START;


public class MainActivity extends AppCompatActivity {
private screenHelper mscreenHelper;
private static MainActivity sAppInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sAppInstance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mscreenHelper = new screenHelper(this);
       TextView myAwesomeTextView = (TextView)findViewById(R.id.editText);
        myAwesomeTextView.setText(mscreenHelper.getServerAddress());
        ToggleButton toggle = (ToggleButton) findViewById(R.id.btn_start_stream);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled successfully
                } else {
                    // The toggle is disabled
                }
            }
        });
    }




}

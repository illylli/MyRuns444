package com.example.yuzhong.myruns3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MapActivity extends Activity {

    private Button mSaveButton;
    private Button mCancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mSaveButton = (Button) findViewById(R.id.map_save_button);
        mCancelButton = (Button) findViewById(R.id.map_cancel_button);
    }

    public void onMapSaveButtonClick(View view) { this.finish();}

    public void onMapCancelButtonClick(View view) { this.finish();}
}

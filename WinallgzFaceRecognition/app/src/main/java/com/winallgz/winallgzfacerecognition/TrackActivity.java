package com.winallgz.winallgzfacerecognition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by kitchee on 2018/9/6.
 * desc:
 */

public class TrackActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonTrack;
    Button buttonRecognize;
    Button btnCompare;
    Button btnCount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        buttonTrack = findViewById(R.id.btn_detect);
        buttonRecognize = findViewById(R.id.btn_recognize);
        btnCount = findViewById(R.id.btn_count);
        btnCompare = findViewById(R.id.btn_compare);

        buttonTrack.setOnClickListener(this);
        buttonRecognize.setOnClickListener(this);
        btnCount.setOnClickListener(this);
        btnCompare.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_detect:

                break;
            case R.id.btn_recognize:
                break;
            case R.id.btn_count:
                break;
            case R.id.btn_compare:
                break;

        }

    }
}

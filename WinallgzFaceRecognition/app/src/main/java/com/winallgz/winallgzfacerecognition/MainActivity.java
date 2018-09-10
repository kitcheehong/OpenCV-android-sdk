package com.winallgz.winallgzfacerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.opencv.core.Mat;

import java.util.Map;

/**
 * Created by kitchee on 2018/9/7.
 * desc:
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
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
                Log.d("kitchee", "onClick: 点击了人脸检测");
                Intent intent = new Intent(MainActivity.this,FaceDetectActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_recognize:
                break;
            case R.id.btn_count:
                break;
            case R.id.btn_compare:
                Intent intent1 = new Intent(MainActivity.this,FaceCompareActivity.class);
                startActivity(intent1);
                break;

        }

    }
}

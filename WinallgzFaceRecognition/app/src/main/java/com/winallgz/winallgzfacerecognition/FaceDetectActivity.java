package com.winallgz.winallgzfacerecognition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongqw.ObjectDetectingView;
import com.kongqw.ObjectDetector;
import com.kongqw.listener.OnFaceDetectorListener;
import com.kongqw.listener.OnOpenCVLoadListener;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kitchee on 2018/9/7.
 * desc: 人脸检测页面
 */

public class FaceDetectActivity extends BaseCameraActivity implements OnFaceDetectorListener{


    @BindView(R.id.page_back)
    ImageView pageBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.person_count)
    TextView personCount;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.shot)
    Button shot;
    @BindView(R.id.detect_view)
    ObjectDetectingView detectView;

    ObjectDetector faceDetector;
    @BindView(R.id.options)
    ImageView options;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);
        ButterKnife.bind(this);

        detectView.setOnOpenCVLoadListener(new OnOpenCVLoadListener() {
            @Override
            public void onOpenCVLoadSuccess() {
                showLongToast("OpenCV 加载成功");
                faceDetector = new ObjectDetector(FaceDetectActivity.this, R.raw.lbpcascade_frontalface_improved, 6, 0.2F, 0.2F, new Scalar(255, 0, 0, 255));
            }

            @Override
            public void onOpenCVLoadFail() {
                showLongToast("OpenCV 加载失败");
            }

            @Override
            public void onNotInstallOpenCVManager() {
                showInstallDialog();
            }
        });

        detectView.loadOpenCV(getApplicationContext());
        // 设置人数检测监听
        detectView.setOnFaceDetectorListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick({R.id.page_back, R.id.start, R.id.shot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.page_back:
                onBackPressed();
                break;
            case R.id.start:
                detectView.addDetector(faceDetector);
                if (detectView.isEnabled()) {
                    start.setText("暂停");
//                    detectView.disableView();
                } else {
                    start.setText("开始");
                    detectView.enableView();
                }
                break;
            case R.id.shot:
                detectView.setIsGetFaceFrame(true);

                break;
        }
    }

    @OnClick(R.id.options)
    public void onViewClicked() {
        // 切换相机
        detectView.swapCamera();
    }

    @Override
    public void onFace(Mat mat, Rect rect) {

    }

    @Override
    public void onFaceCount(int count) {
        personCount.setText("检测到的人数有："+count);
    }
}

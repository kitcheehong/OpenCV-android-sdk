package com.winallgz.winallgzfacerecognition;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongqw.ObjectDetectingView;
import com.kongqw.ObjectDetector;
import com.kongqw.listener.OnFaceDetectorListener;
import com.kongqw.listener.OnOpenCVLoadListener;
import com.kongqw.util.FaceUtil;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kitchee on 2018/9/10.
 * desc:
 */

public class FaceCompareActivity extends BaseCameraActivity implements OnFaceDetectorListener{

    @BindView(R.id.page_back)
    ImageView pageBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.options)
    ImageView options;
    @BindView(R.id.img_1)
    ImageView img1;
    @BindView(R.id.similarity)
    TextView similarity;
    @BindView(R.id.img_2)
    ImageView img2;
    @BindView(R.id.shot)
    Button shot;
    @BindView(R.id.detect_view)
    ObjectDetectingView detectView;

    ObjectDetector faceObject;
    ObjectDetector eyesObject;
    private boolean isGettingFace = false;
    private Bitmap mBitmapFace1;
    private Bitmap mBitmapFace2;
    private static final String FACE1 = "face1";
    private static final String FACE2 = "face2";
    private double cmp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_compare);
        ButterKnife.bind(this);

        detectView.setOnOpenCVLoadListener(new OnOpenCVLoadListener() {
            @Override
            public void onOpenCVLoadSuccess() {
                showLongToast("OpenCV加载成功");
                // 构建相关的级联分类器
                faceObject = new ObjectDetector(FaceCompareActivity.this,R.raw.lbpcascade_frontalface_improved,3,0.2f,0.2f,new Scalar(255, 0, 0, 255));
                eyesObject = new ObjectDetector(FaceCompareActivity.this,R.raw.haarcascade_eye,3,0.2f,0.2f,new Scalar(255, 0, 0, 255));
                detectView.addDetector(faceObject);
            }

            @Override
            public void onOpenCVLoadFail() {
                //
                showLongToast("OpenCV加载失败");
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

    @OnClick({R.id.page_back, R.id.options, R.id.shot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.page_back:
                onBackPressed();
                break;
            case R.id.options:
                // 切换摄像头
                detectView.swapCamera();
                break;
            case R.id.shot:
                // 截屏比对
                isGettingFace = true;

                break;
        }
    }

    @Override
    public void onFace(Mat mat, Rect rect) {
        if (isGettingFace) {
            if (null == mBitmapFace1 || null != mBitmapFace2) {
                mBitmapFace1 = null;
                mBitmapFace2 = null;

                // 保存人脸信息并显示
                FaceUtil.saveImage(this, mat, rect, FACE1);
                mBitmapFace1 = FaceUtil.getImage(this, FACE1);
                cmp = 0.0d;
            } else {
                FaceUtil.saveImage(this, mat, rect, FACE2);
                mBitmapFace2 = FaceUtil.getImage(this, FACE2);

                // 计算相似度
//                cmp = FaceUtil.compare(this, FACE1, FACE2);
                cmp = FaceUtil.comPareHist(mBitmapFace1,mBitmapFace2) * 100;
                Log.i("kitchee", "onFace: 相似度 : " + cmp);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null == mBitmapFace1) {
                        img1.setImageResource(R.mipmap.ic_launcher_round);
                    } else {
                        img1.setImageBitmap(mBitmapFace1);
                    }
                    if (null == mBitmapFace2) {
                        img2.setImageResource(R.mipmap.ic_launcher_round);
                    } else {
                        img2.setImageBitmap(mBitmapFace2);
                    }
                    similarity.setText(String.format("相似度 :  %.2f", cmp) + "%");
                }
            });

            isGettingFace = false;
        }
    }

    @Override
    public void onFaceCount(int count) {

    }
}

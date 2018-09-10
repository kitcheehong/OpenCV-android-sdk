package com.kongqw;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.kongqw.listener.OnFaceDetectorListener;
import com.kongqw.util.FaceUtil;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * Created by kqw on 2016/7/13.
 * RobotCameraView
 */
public class ObjectDetectingView extends BaseCameraView {

    private static final String TAG = "ObjectDetectingView";
    private ArrayList<ObjectDetector> mObjectDetects;

    private MatOfRect mObject;
    private boolean isGettingFace = false;

    private OnFaceDetectorListener mOnFaceDetectorListener;
//    private Mat mRgbaT;
    private float mRelativeFaceSize   = 0.2f;
    private int mAbsoluteFaceSize   = 0;

    @Override
    public void onOpenCVLoadSuccess() {
        Log.i(TAG, "onOpenCVLoadSuccess: ");

        mObject = new MatOfRect();

        mObjectDetects = new ArrayList<>();

//        mRgbaT=new Mat();
    }

    @Override
    public void onOpenCVLoadFail() {
        Log.i(TAG, "onOpenCVLoadFail: ");
    }

    public ObjectDetectingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        // 子线程（非UI线程）
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

//        if (mAbsoluteFaceSize == 0) {
//            int height = mGray.rows();
//            if (Math.round(height * mRelativeFaceSize) > 0) {
//                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
//            }
////            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
//        }

        // 反转图像页面
//        Core.transpose(mRgba,mRgbaT); //转置函数，可以水平的图像变为垂直
//        Imgproc.resize(mRgbaT,mRgba, mRgba.size(), 0.0D, 0.0D, 0); //将转置后的图像缩放为mRgbaF的大小
//        Core.flip(mRgba, mRgba,0); //根据x,y轴翻转，0-x 1-y
//
//        Core.transpose(mGray,mRgbaT); //转置函数，可以水平的图像变为垂直
//        Imgproc.resize(mRgbaT,mGray, mGray.size(), 0.0D, 0.0D, 0); //将转置后的图像缩放为mRgbaF的大小
//        Core.flip(mGray, mGray,0); //根据x,y轴翻转，0-x 1-y

        if(isGettingFace){
            // 获取一帧图片来保存并检测
            FaceUtil.saveCompleteImage(getContext(),mRgba);
            isGettingFace = false;
        }

        for (ObjectDetector detector : mObjectDetects) {

            // 检测目标
            Rect[] object = detector.detectObject(mGray, mObject);
            for (Rect rect : object) {
                Imgproc.rectangle(mRgba, rect.tl(), rect.br(), detector.getRectColor(), 3);
                Log.d(TAG, "onCameraFrame: 检测到目标");
                if (null != mOnFaceDetectorListener) {
                    mOnFaceDetectorListener.onFace(mRgba, rect);
                }
            }
            if (null != mOnFaceDetectorListener) {
                mOnFaceDetectorListener.onFaceCount(object.length);
            }
        }

        return mRgba;
    }

    /**
     * 添加检测器
     *
     * @param detector 检测器
     */
    public synchronized void addDetector(ObjectDetector detector) {
        if (!mObjectDetects.contains(detector)) {
            mObjectDetects.add(detector);
        }
    }

    /**
     * 移除检测器
     *
     * @param detector 检测器
     */
    public synchronized void removeDetector(ObjectDetector detector) {
        if (mObjectDetects.contains(detector)) {
            mObjectDetects.remove(detector);
        }
    }

    public void setIsGetFaceFrame(boolean isGettingFace){
        this.isGettingFace = isGettingFace;
    }

    /**
     * 添加人脸识别额监听
     *
     * @param listener 回调接口
     */
    public void setOnFaceDetectorListener(OnFaceDetectorListener listener) {
        mOnFaceDetectorListener = listener;
    }

}

package com.kongqw.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import org.opencv.imgproc.Imgproc;



import java.io.File;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;


/**
 * Created by kqw on 2016/9/9.
 * FaceUtil
 */
public final class FaceUtil {

    private static final String TAG = "FaceUtil";

    private FaceUtil() {
    }

    public static boolean saveCompleteImage(Context context, Mat image){
        Mat gratMat = new Mat();
        Imgproc.cvtColor(image,gratMat,Imgproc.COLOR_BGR2GRAY);
        return imwrite(getFilePath(context,getFormatTime(context)),gratMat);
    }

    /**
     * 特征保存
     *
     * @param context  Context
     * @param image    Mat
     * @param rect     人脸信息
     * @param fileName 文件名字
     * @return 保存是否成功
     */
    public static boolean saveImage(Context context, Mat image, Rect rect, String fileName) {
        // 原图置灰
        Mat grayMat = new Mat();
        Imgproc.cvtColor(image, grayMat, Imgproc.COLOR_BGR2GRAY);
        // 把检测到的人脸重新定义大小后保存成文件
        Mat sub = grayMat.submat(rect);
        Mat mat = new Mat();
        Size size = new Size(100, 100);
        Imgproc.resize(sub, mat, size);

        return imwrite(getFilePath(context, fileName), mat);
    }

    /**
     * 删除特征
     *
     * @param context  Context
     * @param fileName 特征文件
     * @return 是否删除成功
     */
    public static boolean deleteImage(Context context, String fileName) {
        // 文件名不能为空
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        // 文件路径不能为空
        String path = getFilePath(context, fileName);
        if (path != null) {
            File file = new File(path);
            return file.exists() && file.delete();
        } else {
            return false;
        }
    }

    /**
     * 提取特征
     *
     * @param context  Context
     * @param fileName 文件名
     * @return 特征图片
     */
    public static Bitmap getImage(Context context, String fileName) {
        String filePath = getFilePath(context, fileName);
        if (TextUtils.isEmpty(filePath)) {
            return null;
        } else {
            return BitmapFactory.decodeFile(filePath);
        }
    }

    /**
     * 特征对比
     *
     * @param context   Context
     * @param fileName1 人脸特征
     * @param fileName2 人脸特征
     * @return 相似度
     */
    public static double compare(Context context, String fileName1, String fileName2) {
//        try {
//            String pathFile1 = getFilePath(context, fileName1);
//            String pathFile2 = getFilePath(context, fileName2);
//            IplImage image1 = cvLoadImage(pathFile1, CV_LOAD_IMAGE_GRAYSCALE);
//            IplImage image2 = cvLoadImage(pathFile2, CV_LOAD_IMAGE_GRAYSCALE);
//            if (null == image1 || null == image2) {
//                return -1;
//            }
//
//            int l_bins = 256;
//            int hist_size[] = {l_bins};
//            float v_ranges[] = {0, 255};
//            float ranges[][] = {v_ranges};
//
//            IplImage imageArr1[] = {image1};
//            IplImage imageArr2[] = {image2};
//            CvHistogram Histogram1 = CvHistogram.create(1, hist_size, CV_HIST_ARRAY, ranges, 1);
//            CvHistogram Histogram2 = CvHistogram.create(1, hist_size, CV_HIST_ARRAY, ranges, 1);
//            cvCalcHist(imageArr1, Histogram1, 0, null);
//            cvCalcHist(imageArr2, Histogram2, 0, null);
//            cvNormalizeHist(Histogram1, 100.0);
//            cvNormalizeHist(Histogram2, 100.0);
//            // 参考：http://blog.csdn.net/nicebooks/article/details/8175002
//            double c1 = cvCompareHist(Histogram1, Histogram2, CV_COMP_CORREL) * 100;
//            double c2 = cvCompareHist(Histogram1, Histogram2, CV_COMP_INTERSECT);
////            Log.i(TAG, "compare: ----------------------------");
////            Log.i(TAG, "compare: c1 = " + c1);
////            Log.i(TAG, "compare: c2 = " + c2);
////            Log.i(TAG, "compare: 平均值 = " + ((c1 + c2) / 2));
////            Log.i(TAG, "compare: ----------------------------");
//            return (c1 + c2) / 2;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -1;
//        }

        return -1;
    }

    /**
     * 获取人脸特征路径
     *
     * @param fileName 人脸特征的图片的名字
     * @return 路径
     */
    private static String getFilePath(Context context, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        // 内存路径
//        String path = context.getApplicationContext().getFilesDir().getPath() + fileName + ".jpg";
//        Log.d(TAG, "getFilePath: 内存路径path = "+ path);
//        return path;
        // 内存卡路径 需要SD卡读取权限
        getSDPath();
        String path = Environment.getExternalStorageDirectory().getPath() + "/FaceDetect/" + fileName + ".jpg";
        Log.d(TAG, "getFilePath: 截图存储路径path = "+ path);
         return path;
    }
    /**获取Sd卡路径**/
    private static void getSDPath() {
        // 先判断SD卡是否存在
        boolean isExistSd = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(isExistSd){
            // 判断是否存在/FaceDetect目录（文件夹）
            File file = new File(Environment.getExternalStorageDirectory().getPath()+"/FaceDetect");
            if(!file.exists()){
                file.mkdir();
            }
        }
    }


    public static final String DATE_FORMAT_YMD_HMS = "yyyy-MM-dd-HH-mm-ss";

    public static String format(long time, String format) {
        format = format == null || "".equals(format) ? DATE_FORMAT_YMD_HMS : format;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if (time > 0) {
            return formatter.format(time);
        }
        return null;
    }

    private static String getFormatTime(Context context){
        return format(System.currentTimeMillis(),DATE_FORMAT_YMD_HMS);
    }


    /**
     * 比较来个矩阵的相似度
     *
     * @param mBitmap1
     * @param mBitmap2
     * @return
     */
    public static double comPareHist(Bitmap mBitmap1, Bitmap mBitmap2) {
        Mat mat1 = new Mat();
        Mat mat2 = new Mat();
        Utils.bitmapToMat(mBitmap1, mat1);
        Utils.bitmapToMat(mBitmap2, mat2);
        return comPareHist(mat1, mat2);
    }

    /**
     * 比较来个矩阵的相似度
     *
     * @param mat1
     * @param mat2
     * @return
     */
    public static double comPareHist(Mat mat1, Mat mat2) {
        Mat srcMat = new Mat();
        Mat desMat = new Mat();
        Imgproc.cvtColor(mat1, srcMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(mat2, desMat, Imgproc.COLOR_BGR2GRAY);
        srcMat.convertTo(srcMat, CvType.CV_32F);
        desMat.convertTo(desMat, CvType.CV_32F);
        double target = Imgproc.compareHist(srcMat, desMat,
                Imgproc.CV_COMP_CORREL);
        return target < 0 ? 0:target;
    }
}

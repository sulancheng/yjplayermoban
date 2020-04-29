package com.example.mypublib.widge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.mypublib.utils.MyLog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 作者：sucheng on 2019/6/28 13:03
 * https://www.jianshu.com/p/2b6d2f24dbc7
 * https://www.jianshu.com/p/af39024896ce
 * https://www.cnblogs.com/Sharley/p/5964456.html 普通view和SurfaceView的镜像和翻转
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private Camera mCamera = null;
    private SurfaceHolder mSurfaceHolder;
    public int DEFAULT_WIDTH = 1280;
    public int DEFAULT_HEIGHT = 720;
    private Context context;


    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
//        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (surfiscross){
            switchCamera(0);
        }
        //打开预览
        startPreviewDisplay();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceHolder = holder;
        switchCamera(mCameraID);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamer();
    }

    private int mCameraID = -1;

    /**
     * 切换相机
     *
     * @param cameraID
     */
    public void switchCamera(int cameraID) {
        if (mCameraID == cameraID) {
            return;
        }
        mCameraID = cameraID;
        // 打开相机
        openCamera(cameraID);
    }

    private void openCamera(int cameraNum) {
        try {
            releaseCamer();//之前调用过则先释放
            mCamera = Camera.open(cameraNum);
            mCamera.setPreviewCallback(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
        setmyParameters();
    }

    private void setmyParameters() {
        if (mCamera == null) {
            return;
        }
        try {
            //设置参数
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setRecordingHint(true);//可能会导致部分手机变形
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            DisplayMetrics dm = getResources().getDisplayMetrics();
            DEFAULT_WIDTH = dm.widthPixels;
            DEFAULT_HEIGHT = dm.heightPixels;
            setPreviewSize(mCamera, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            setPictureSize(mCamera, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            mCamera.setDisplayOrientation(calculateCameraPreviewOrientation(mCameraID, (Activity) context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        startPreviewDisplay();
    }

    /**
     * 开始预览
     */
    private void startPreviewDisplay() {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MyLog.e("Camera", "Camera = null");
        }
    }

    public void releaseCamer() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCameraID = -1;
            mCamera = null;
        }
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
    }

    public interface Takepic {
        void takepicFinish(boolean isSucess);
    }

    public void takePicture(final Takepic takepic) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    try {
                        Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
                                data.length);

                        //转正图片
                        Bitmap saveBitmap = setTakePicktrueOrientation(mCameraID, bm);

                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                        String times = format.format((new Date()));
                        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File myCaptureFile = new File(dir, times + ".jpg");
                        BufferedOutputStream bos = new BufferedOutputStream(
                                new FileOutputStream(myCaptureFile));
                        saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                        if (!bm.isRecycled()) {
                            bm.recycle();
                        }
                        if (!saveBitmap.isRecycled()) {
                            saveBitmap.recycle();
                        }
                        //发送广播通知系统图库刷新数据
                        Uri uri = Uri.fromFile(dir);
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                        takepic.takepicFinish(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        takepic.takepicFinish(false);
                    }
                    if (mCamera != null) {
                        mCamera.startPreview();
                    }
                }
            });

        }
    }

    public Bitmap setTakePicktrueOrientation(int id, Bitmap bitmap) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(id, info);
        bitmap = rotaingImageView(id, info.orientation, bitmap);
        return bitmap;
    }

    /**
     * 把相机拍照返回照片转正
     *
     * @param angle 旋转角度
     * @return bitmap 图片
     */
    private Bitmap rotaingImageView(int id, int angle, Bitmap bitmap) {
        //矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        //加入翻转 把相机拍照返回照片转正
        if (id == 1) {
            matrix.postScale(-1, 1);
        }
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 设置预览角度，setDisplayOrientation本身只能改变预览的角度
     * previewFrameCallback以及拍摄出来的照片是不会发生改变的，拍摄出来的照片角度依旧不正常的
     * 拍摄的照片需要自行处理
     * 这里Nexus5X的相机简直没法吐槽，后置摄像头倒置了，切换摄像头之后就出现问题了。
     */
    public int calculateCameraPreviewOrientation(int mCameraID, Activity activity) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraID, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }


    private boolean surfiscross = true;

    public void startPreview() {
        surfiscross = false;
        switchCamera(1);
    }

    public void stopPreview() {
        releaseCamer();
    }

    /**
     * 获取预览大小
     */
    public Camera.Size getPreviewSize(Camera mCamera) {
        if (mCamera != null) {
            return mCamera.getParameters().getPreviewSize();
        }
        return null;
    }

    /**
     * 设置预览大小
     */
    public void setPreviewSize(Camera camera, int expectWidth, int expectHeight) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = getOptimalSize(parameters.getSupportedPreviewSizes(),
                expectWidth, expectHeight);
        parameters.setPreviewSize(size.width, size.height);
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        camera.setParameters(parameters);
    }

    /**
     * 设置拍摄的照片大小
     */
    public void setPictureSize(Camera camera, int expectWidth, int expectHeight) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = getOptimalSize(parameters.getSupportedPictureSizes(),
                expectWidth, expectHeight);
        parameters.setPictureSize(size.width, size.height);
        camera.setParameters(parameters);
    }

    /**
     * 解决横竖会被拉伸压缩的问题
     */
    private Camera.Size getOptimalSize(@NonNull List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }

    /**
     * 计算最完美的Size
     */
    public Camera.Size calculatePerfectSize(List<Camera.Size> sizes, int expectWidth,
                                            int expectHeight) {
        sortList(sizes); // 根据宽度进行排序
        Camera.Size result = sizes.get(0);
        boolean widthOrHeight = false; // 判断存在宽或高相等的Size
        // 辗转计算宽高最接近的值
        for (Camera.Size size : sizes) {
            // 如果宽高相等，则直接返回
            if (size.width == expectWidth && size.height == expectHeight) {
                result = size;
                break;
            }
            // 仅仅是宽度相等，计算高度最接近的size
            if (size.width == expectWidth) {
                widthOrHeight = true;
                if (Math.abs(result.height - expectHeight)
                        > Math.abs(size.height - expectHeight)) {
                    result = size;
                }
            }
            // 高度相等，则计算宽度最接近的Size
            else if (size.height == expectHeight) {
                widthOrHeight = true;
                if (Math.abs(result.width - expectWidth)
                        > Math.abs(size.width - expectWidth)) {
                    result = size;
                }
            }
            // 如果之前的查找不存在宽或高相等的情况，则计算宽度和高度都最接近的期望值的Size
            else if (!widthOrHeight) {
                if (Math.abs(result.width - expectWidth)
                        > Math.abs(size.width - expectWidth)
                        && Math.abs(result.height - expectHeight)
                        > Math.abs(size.height - expectHeight)) {
                    result = size;
                }
            }
        }
        return result;
    }

    /**
     * 排序
     */
    private void sortList(List<Camera.Size> list) {
        Collections.sort(list, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size pre, Camera.Size after) {
                if (pre.width > after.width) {
                    return 1;
                } else if (pre.width < after.width) {
                    return -1;
                }
                return 0;
            }
        });
    }

    /**
     * 打开闪关灯
     */
    public void turnLightOn() {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes == null) {
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            }
        }
    }

    /**
     * 自动模式闪光灯
     */
    public void turnLightAuto() {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes == null) {
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            }
        }
    }

    /**
     * 关闭闪光灯
     */
    public void turnLightOff() {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        if (flashModes == null) {
            return;
        }
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            }
        }
    }
}

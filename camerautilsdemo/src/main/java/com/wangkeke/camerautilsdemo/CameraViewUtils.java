package com.wangkeke.camerautilsdemo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CameraViewUtils implements Camera.PreviewCallback {
    private SurfaceView surfaceView;
    private Camera camera;
    private int cameraId;
    private boolean markIng;
    private CameraMarkCallBack cameraMarkCallBack;
    private byte[] mPreviewBuffer;
    private int preWidth, preHeight;
    private Disposable mDisposable;
    private boolean hasHdmiData;
    private boolean needPaint;
    private boolean isDestroyed;
    private Disposable hdmiDispose;
    private Camera.Parameters parameters;
    private String currentResolution;
    private String currentDisplay;
    private static int times = 0;
    private boolean loop = true;
    private Thread checkHdmiStateThread;
    private Disposable frameDispose;
    private SurfaceHolder.Callback surCallback;

    private boolean isReOpenCamera = false;

    private static final int HDMI_STATE_NORMAL = 1;
    private static final int HDMI_STATE_RELEASEING = 2;
    private static final int HDMI_STATE_OPNEING = 3;

    //1:正常状态，2：release释放中， 3.open中
    private int currentHdmiState = HDMI_STATE_NORMAL;

    public interface CameraMarkCallBack {
        void markCallBack(String filePath);

        void hasHdmiData();

        void showDataSize(String resolution, String previewSize, String inputSize, int fps);

        void hdmiState(boolean state);
    }

    public CameraViewUtils(boolean needPaint, SurfaceView surfaceView, int cameraId, CameraMarkCallBack cameraMarkCallBack) {
        this.cameraMarkCallBack = cameraMarkCallBack;
        this.surfaceView = surfaceView;
        this.cameraId = cameraId;
        this.needPaint = needPaint;
        loop = true;
        init();
        checkHdmiSizeTask();
        checkFrameCallBackState();
    }

    private SurfaceHolder currentHolder;

    private void init() {

        Log.e("wangkeke", " init CameraViewUtils ");

        /*if(null != camera){
            camera.stopPreview();
            Camera.Size newSize = getCloselyPreSize(width, height, parameters.getSupportedPreviewSizes());
            preWidth = newSize.width;
            preHeight = newSize.height;
            Log.e("wangkeke", "------------surfaceChanged---重新设置preview大小  width："+preWidth+"   height: "+preHeight);
            parameters.setPreviewSize(preWidth,preHeight);
            camera.startPreview();
        }*/
        surCallback = new SurfaceHolder.Callback() {
            @SuppressLint("CheckResult")
            @Override
            public void surfaceCreated(final SurfaceHolder holder) {
                try {
                    currentHolder = holder;
                    isDestroyed = false;
                    Log.e("wangkeke", "check thread 开始判断检测线程是否正在运行");
                    if (null == checkHdmiStateThread || !checkHdmiStateThread.isAlive()) {
                        Log.e("wangkeke", "thread over 线程结束，重新开启");
                        loop = true;
                        checkHdmiSizeTask();
                    }
                    if (needPaint) {
                        needPaint = false;
                        Canvas canvas = holder.lockCanvas();
                        canvas.drawColor(Color.BLACK);
                        holder.unlockCanvasAndPost(canvas);
                    }
                    Log.e("wangkeke", "------------surfaceCreated----  " + Thread.currentThread().getName());
                    Observable.timer(400, TimeUnit.MILLISECONDS)
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    Log.e("wangkeke", "------------surfaceCreated-----开始打开camera   " + Thread.currentThread().getName());
                                    doOpenCarmera();
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.e("wangkeke", "------------surfaceChanged");
                /*if(null != camera){
                    camera.stopPreview();
                    Camera.Size newSize = getCloselyPreSize(width, height, parameters.getSupportedPreviewSizes());
                    preWidth = newSize.width;
                    preHeight = newSize.height;
                    Log.e("wangkeke", "------------surfaceChanged---重新设置preview大小  width："+preWidth+"   height: "+preHeight);
                    parameters.setPreviewSize(preWidth,preHeight);
                    camera.startPreview();
                }*/
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.e("wangkeke", "------------surfaceDestroyed");
                needPaint = true;
                onDestory();
            }
        };
        surfaceView.getHolder().addCallback(surCallback);
    }

    public void doOpenCarmera() {

        /*long delayTime = 0;
        if (needDelay) {
            delayTime = 0;
        }
        Disposable dispose = Observable.timer(delayTime, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        try {
                            initCamera();
                        } catch (Exception e) {
                            Log.e("wangkeke", "doOpenCarmera : " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });*/


        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                try {
                    initCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("wangkeke", "------------error : " + e.toString());
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("wangkeke", "------------error : " + e.toString());
                    showHdmiErrorDialog();
                }
            }
        }).start();*/
    }

    private void initCamera() {
        if (camera != null || isDestroyed) return;
        camera = Camera.open(cameraId);
        //给照相机设置参数
        parameters = camera.getParameters();
        //设置照片的格式
        parameters.setPreviewFormat(PixelFormat.YCbCr_420_SP);
        //设置照片的质量
        parameters.setJpegQuality(80);

        parameters.setPictureFormat(PixelFormat.JPEG);

        /*int previewWidth = 0;
        int previewHeight = 0;
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();      // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
        if (sizeList.size() > 1) {
            Iterator<Camera.Size> itor = sizeList.iterator();
            while (itor.hasNext()) {
                Camera.Size cur = itor.next();

                Log.e("CameraSize","支持分辨率"+cur.width+"*"+cur.height);


                if (cur.width >= previewWidth && cur.height >= previewHeight) {
                    preWidth = cur.width;
                    preHeight = cur.height;
                    Log.e("Chris", "cameraId:" + cameraId + "----previewWidth: " + preWidth + " previewHeight: " + preHeight);
                    break;
                }
            }
        }*/


//        if (cameraId == 1) {
        /*Camera.Size fixSize;
        if (surfaceView.getWidth() <= 1) {
            fixSize = getCloselyPreSize(1920, 1080, parameters.getSupportedPreviewSizes());
//            Log.e("wangkeke", "计算的宽度<=1 ：" + fixSize.width + "    计算的高度<=1：" + fixSize.height);
        } else {
            fixSize = getCloselyPreSize(surfaceView.getWidth(), surfaceView.getHeight(), parameters.getSupportedPreviewSizes());
            Log.e("wangkeke", "计算的宽度>1 ：" + fixSize.width + "    计算的高度>1：" + fixSize.height);
        }*/

//        Log.e("wangkeke", "isHdmiPreviewAutoSize : " + AppPreferences.getInstance().isHdmiPreviewAutoSize());

        /*if (AppPreferences.getInstance().isHdmiPreviewAutoSize()) {
            //自适应分辨率处理
            setAutoSize(parameters);
        } else {
            preWidth = AppPreferences.getInstance().getHdmiPreviewSizeWidth();
            preHeight = AppPreferences.getInstance().getHdmiPreviewSizeHeight();
            parameters.setPreviewSize(preWidth, preHeight);
        }*/
        preWidth = 1280;
        preHeight = 720;
        parameters.setPreviewSize(preWidth, preHeight);
        Log.e("wangkeke", "当前预览宽度：" + preWidth + "    当前预览高度：" + preHeight);
        callbackCurrentSize(preWidth, preHeight, 0);
        parameters.setPreviewFpsRange(30000, 30000);
        parameters.setRecordingHint(true);
        parameters.setAutoExposureLock(true);
        parameters.setAutoWhiteBalanceLock(true);

        mPreviewBuffer = new byte[preWidth * preHeight * 3 / 2];
        //将画面展示到SurfaceView
        try {
            // 给照相机设置参数
            camera.setParameters(parameters);
            camera.setPreviewDisplay(surfaceView.getHolder());
//            camera.setPreviewCallback(this);

            camera.addCallbackBuffer(mPreviewBuffer);
            camera.setPreviewCallbackWithBuffer(this);
            Log.e("wangkeke", "-------startPreview");
            //开启预览
            camera.startPreview();

            isReOpenCamera = false;
            /*//如果发现需要关闭
            if(currentHdmiState == HDMI_STATE_RELEASEING){
                currentHdmiState = HDMI_STATE_NORMAL;
                onDestory();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("wangkeke", "initCamera : " + e.toString());
        }
    }

    private void callbackCurrentSize(final int previewWidth, final int previewHeight, final int fps) {
        final String curResolution = ReflectUtil.getProperty("sys.hdmiin.resolution", "1");
        HashMap hashMapSize = MonitorInUtils.getMonitorInSize();
        final String needPreviewSize = (String) hashMapSize.get(Integer.parseInt(curResolution));

        if (null != cameraMarkCallBack) {
            Observable.just("").observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            cameraMarkCallBack.showDataSize(curResolution, previewWidth + "*" + previewHeight, needPreviewSize, fps);
                        }
                    });
        }
    }

    private boolean isHdmiWindowShow = false;

    private void checkHdmiSizeTask() {

        try {
            currentResolution = ReflectUtil.getProperty("sys.hdmiin.resolution", "-1");
            currentDisplay = ReflectUtil.getProperty("sys.hdmiin.display", "-2");

            Log.e("wangkeke", "init currentResolution : " + currentResolution + "     currentDisplay:" + currentDisplay);

            checkHdmiStateThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (loop) {
                        String newResolution = ReflectUtil.getProperty("sys.hdmiin.resolution", "-1");
                        String newDisplay = ReflectUtil.getProperty("sys.hdmiin.display", "-2");

//                        Log.e("display", "newResolution : " + newResolution + "     newDisplay:" + newDisplay + "    thread = " + Thread.currentThread().getName());
                        if (!newResolution.equals(currentResolution)) {
                            Log.e("wangkeke", "hdmi in change!");
                            dealHdmiState(1);
                            SystemClock.sleep(1500);
                        }
                        if (!"1".equals(newDisplay)) {
                            /*if (currentDisplay.equals("1") && null != currentHolder) {
                                //处理最后一帧
                                Observable.create(new ObservableOnSubscribe<String>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                                        e.onNext("");
                                    }
                                }).observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<String>() {
                                            @Override
                                            public void accept(String s) throws Exception {
                                                camera.stopPreview();
                                                Canvas canvas = currentHolder.lockCanvas();
                                                canvas.drawColor(Color.BLACK);
                                                currentHolder.unlockCanvasAndPost(canvas);
                                           }
                                        });

                            }*/
                            dealHdmiState(0);
                        } else {
                            //if (!currentDisplay.equals("1")) {
                            if (!isHdmiWindowShow) {
                                //恢复正常
                                Observable.just("").observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<String>() {
                                            @Override
                                            public void accept(String s) throws Exception {
                                                cameraMarkCallBack.hasHdmiData();
                                            }
                                        });

                                dealHdmiState(1);
                                SystemClock.sleep(1500);

                                isHdmiWindowShow = true;
                            }
                        }

                        currentResolution = newResolution;
                        currentDisplay = newDisplay;

                        SystemClock.sleep(100);
                    }
                }
            });
            checkHdmiStateThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dealHdmiState(int state) {
        if (state == 1) {
            if (null != camera && !isDestroyed) {
                camera.stopPreview();
                mPreviewBuffer = new byte[preWidth * preHeight * 3 / 2];
                camera.addCallbackBuffer(mPreviewBuffer);
                camera.setPreviewCallbackWithBuffer(this);
                camera.startPreview();
                callbackCurrentSize(preWidth, preHeight, 0);
            }
        }

        if (state == 0) {
            Log.e("wangkeke", "times = " + times);
            times++;
            if (times > 5 && !isReOpenCamera) {
                doReOpenCamera();
                times = 0;
                SystemClock.sleep(3000);
            }
            SystemClock.sleep(200);
        }
    }

    public void reOpenCamera() {
        doReOpenCamera();
    }

    public boolean isReOpenCamera() {
        return isReOpenCamera;
    }

    public void changeHdmiState() {
        isDestroyed = false;
//        currentHdmiState = HDMI_STATE_NORMAL;
    }

    public void onDestory() {

        /*//正在释放相机，return
        if(currentHdmiState == HDMI_STATE_RELEASEING){
            Log.e("wangkeke","--------正在release过程中被关闭");
            return;
        }

        //正在open过程中
        if(currentHdmiState == HDMI_STATE_OPNEING){
            currentHdmiState = HDMI_STATE_RELEASEING;
            Log.e("wangkeke","--------正在open过程中被关闭");
            return;
        }*/

//        currentHdmiState = HDMI_STATE_RELEASEING;
        Log.e("wangkeke", "----------onDestory");
        if (null != frameDispose && !frameDispose.isDisposed()) {
            frameDispose.dispose();
        }
        loop = false;
        times = 0;
        lastPreviewFrameTime = 0;
        checkHdmiStateThread = null;
        if (isDestroyed) {
            return;
        }
        try {
            isDestroyed = true;
            hasHdmiData = false;
            if (mDisposable != null && !mDisposable.isDisposed()) {
                mDisposable.dispose();
            }

            //防止耗时 放在子线程中处理
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("wangkeke", "-----正在释放camera : " + camera);

                    try {
                        if (camera != null && currentHdmiState == HDMI_STATE_NORMAL) {
                            camera.stopPreview();
                            camera.setPreviewCallbackWithBuffer(null);
                            camera.release();//释放资源
                            camera = null;
                            parameters = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("wangkeke", "new Thread camera error: " + e.toString());
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("wangkeke", "onDestory camera error: " + e.toString());
        }
    }

    public void setMarkIng(boolean markIng) {
        this.markIng = markIng;
    }

    private int frameCount = 0;

    private int lastSconds = 0;

//    private int checkFrame = 0;
//    private int lastCheckFrame = 0;

    private long lastPreviewFrameTime = 0;

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        camera.addCallbackBuffer(mPreviewBuffer);
        Calendar calendar = Calendar.getInstance();
        int seconds = calendar.get(Calendar.SECOND);    // 秒
        frameCount++;
        if (lastSconds != seconds) {
            callbackCurrentSize(preWidth, preHeight, frameCount);
            //下一秒
            frameCount = 0;
            lastSconds = seconds;
        }

        lastPreviewFrameTime = System.currentTimeMillis();
    }

    private String currentFrameResolution;

    private boolean isFrameCheckPause = true;

    private void checkFrameCallBackState() {

        if (null == frameDispose) {
            frameDispose = Observable.interval(1500, 500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {

                            final String newResolution = ReflectUtil.getProperty("sys.hdmiin.resolution", "-1");

                            if (!newResolution.equals(currentFrameResolution)) {
                                isFrameCheckPause = false;
                                Observable.timer(1000, TimeUnit.MILLISECONDS)
                                        /*.subscribeOn(Schedulers.newThread())
                                        .observeOn(Schedulers.newThread())*/
                                        .subscribe(new Consumer<Long>() {
                                            @Override
                                            public void accept(Long aLong) throws Exception {
                                                isFrameCheckPause = true;
                                                currentFrameResolution = newResolution;
                                            }
                                        });
                                return;
                            }

                            if (isFrameCheckPause) {

                                if (new Date().getTime() - lastPreviewFrameTime > 3000) {
                                    if (null != cameraMarkCallBack) {
                                        Log.e("wangkeke", "------数据回调异常-----开始处理最后一帧");
                                        cameraMarkCallBack.hdmiState(false);
                                    }
                                } else {
                                    if (null != cameraMarkCallBack) {
                                        Log.e("wangkeke", "------数据回调正常-----");
                                        cameraMarkCallBack.hdmiState(true);
                                    }
                                }
//                                if (checkFrame > lastCheckFrame) {
//                                    checkFrame = lastCheckFrame;
//                                    if(null != cameraMarkCallBack){
//                                        Log.e("wangkeke", "------数据回调正常-----");
//                                        cameraMarkCallBack.hdmiState(true);
//                                    }
//                                } else {
//                                    if(null != cameraMarkCallBack){
//                                        Log.e("wangkeke", "------数据回调异常-----开始处理最后一帧");
////                                        String curDisplay = ReflectUtil.getProperty("sys.hdmiin.display", "-2");
////                                        if(!"1".equals(curDisplay)){
//                                            cameraMarkCallBack.hdmiState(false);
////                                        }
//                                    }
//                                }
                            }

                            currentFrameResolution = newResolution;
                        }
                    });
        }
    }

    private void checkHdmiData() {
        if (cameraMarkCallBack == null) return;
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mDisposable = Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                if (!hasHdmiData) return;
                cameraMarkCallBack.hasHdmiData();
                if (!mDisposable.isDisposed()) {
                    mDisposable.dispose();
                }
            }
        });
    }

    /**
     * 重新开启预览
     */
    private void doReOpenCamera() {

        if (isReOpenCamera) {
            return;
        }

        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                isReOpenCamera = true;
                Log.e("wangkeke", "-----重启摄像头");
                if (camera != null) {
                    camera.stopPreview();
                    camera.setPreviewCallbackWithBuffer(null);
                    currentHdmiState = HDMI_STATE_RELEASEING;
                    camera.release();//释放资源
                    camera = null;
                    currentHdmiState = HDMI_STATE_NORMAL;
                }
                if (!isDestroyed) {
                    e.onNext("start");
                }
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e("wangkeke", "-----重启摄像头---doOpenCarmera");
                        doOpenCarmera();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public synchronized void savePic(byte[] data) {
        if (markIng) {
            markIng = false;
        }
    }
}

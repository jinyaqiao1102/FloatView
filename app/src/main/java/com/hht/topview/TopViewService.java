package com.hht.topview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class TopViewService extends Service implements View.OnTouchListener {
    private final static String TAG = TopViewService.class.getSimpleName();

    private final static String TAG_TOP    = "TOP";
    private final static String TAG_LEFT   = "LEFT";
    private final static String TAG_RIGHT  = "RIGHT";
    private final static String TAG_BOTTOM = "BOTTOM";

    private WindowManager windowManager;
    private View leftView;
    private View rightView;
    private View topView;
    private View bottomView;
    private boolean isAddView;
    private float posX;
    private float posY;
    private float curPosX;
    private float curPosY;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        initFloatView();
        addFloatView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * 初始化要添加的View
     */
    private void initFloatView() {
        leftView = LayoutInflater.from(this).inflate(R.layout.layout_float_view,null);
        rightView = LayoutInflater.from(this).inflate(R.layout.layout_float_view,null);
        topView = LayoutInflater.from(this).inflate(R.layout.layout_float_view,null);
        bottomView = LayoutInflater.from(this).inflate(R.layout.layout_float_view,null);
        leftView.setTag(TAG_LEFT);
        topView.setTag(TAG_TOP);
        rightView.setTag(TAG_RIGHT);
        bottomView.setTag(TAG_BOTTOM);
        leftView.setOnTouchListener(this);
        rightView.setOnTouchListener(this);
        topView.setOnTouchListener(this);
        bottomView.setOnTouchListener(this);

    }

    /**
     * 初始化要添加的View的LayoutParams参数
     * @param direction 方向 1- Left 2-Right 3-Top 4-Bottom
     * @return 根据传入方向设置好的LayoutParams
     */
    private WindowManager.LayoutParams initFloatWindowParams(int direction){
        DisplayMetrics dm = TopViewService.this.getResources().getDisplayMetrics();
        int screenWidth =  dm.widthPixels;
        int screenHeight = dm.heightPixels;
        // 1- Left 2-Right 3-Top 4-Bottom
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.flags = flags;
        params.format = PixelFormat.TRANSLUCENT;
        if (direction ==1){
            params.x = 0;
            params.y = 0;
            params.width = 20;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.LEFT;
        }else if (direction == 2){
            params.x = screenWidth - 20;
            params.y = 0;
            params.width = 20;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.LEFT;
        }else if (direction == 3){
            params.x = 0;
            params.y = 0;
            params.width = screenWidth - 40;
            params.height = 20;
            params.gravity = Gravity.TOP;
        }else if (direction == 4){
            params.x = 20;
            params.y = screenHeight - 20;
            params.width = screenWidth - 40;
            params.height = 20;
            params.gravity = Gravity.LEFT;
        }

        return params;
    }


    /**
     * 添加浮动窗口
     */
    private void addFloatView(){
        if (isAddView){
            return;
        }
        windowManager.addView(leftView, initFloatWindowParams(1));
        windowManager.addView(rightView, initFloatWindowParams(2));
        windowManager.addView(topView,initFloatWindowParams(3));
        windowManager.addView(bottomView,initFloatWindowParams(4));
        isAddView= true;
    }


    /**
     * 移除浮动窗口
     */
    private void removeFloatView(){
        if (!isAddView) {
            return;
        }
        windowManager.removeViewImmediate(leftView);
        windowManager.removeViewImmediate(rightView);
        windowManager.removeViewImmediate(topView);
        windowManager.removeViewImmediate(bottomView);
        isAddView= false;
    }

    @Override
    public void onDestroy() {
        removeFloatView();
        super.onDestroy();
    }


    /**
     * 处理滑动事件
     * @param view 目标View
     * @param  event 手指滑动事件
     * @return 是否消费本次事件
     */
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                posX = event.getX();
                posY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                curPosX = event.getX();
                curPosY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if ((curPosX - posX > 0) && (Math.abs(curPosX - posX) > 25) && view.getTag().equals(TAG_LEFT)){
                    Log.v(TAG,"向右滑动");
                }
                else if ((curPosX - posX < 0) && (Math.abs(curPosX-posX) > 25) && view.getTag().equals(TAG_RIGHT)){
                    Log.v(TAG,"向左滑动");
                }
                if ((curPosY - posY > 0) && (Math.abs(curPosY - posY) > 25) && view.getTag().equals(TAG_TOP)){
                    Log.v(TAG,"向下滑动");
                }
                else if ((curPosY - posY < 0) && (Math.abs(curPosY-posY) > 25) && view.getTag().equals(TAG_BOTTOM)){
                    Log.v(TAG,"向上滑动");
                }
                break;
        }
        return false;
    }
}

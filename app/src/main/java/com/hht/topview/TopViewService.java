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
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hht.topview.entity.GridItemEntity;
import com.hht.topview.ui.view.RecyclerViewGridAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private LinearLayout quickView;
    private RecyclerView quickList;
    private boolean isShowFloatView;
    private boolean isShowQuickView;
    private int fromDirection;
    private float posX;
    private float posY;
    private float curPosX;
    private float curPosY;
    private List<GridItemEntity> data= new ArrayList<GridItemEntity>();
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
        quickView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_quick_view,null);
        quickList =  quickView.findViewById(R.id.rcl_quick_list);
        leftView.setTag(TAG_LEFT);
        topView.setTag(TAG_TOP);
        rightView.setTag(TAG_RIGHT);
        bottomView.setTag(TAG_BOTTOM);
        leftView.setOnTouchListener(this);
        rightView.setOnTouchListener(this);
        topView.setOnTouchListener(this);
        bottomView.setOnTouchListener(this);
        quickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeQuickView(fromDirection);
                Log.e("=======","removeQuickView");
            }
        });

        for(int i=0; i<12; i++) {
            GridItemEntity entity = new GridItemEntity();
            entity.setTitle("应用"+i);
            data.add(entity);
        }
        RecyclerViewGridAdapter adapter = new RecyclerViewGridAdapter(this,data);
        quickList.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        quickList.setLayoutManager(gridLayoutManager);


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


    private WindowManager.LayoutParams initQuickViewParams(int direction){
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
            params.width = screenWidth;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.LEFT;
        }else if (direction == 2){
            params.x = 0;
            params.y = 0;
            params.width = screenWidth;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.LEFT;
        }

        return params;
    }

    /**
     * 添加浮动窗口
     */
    private void addFloatView(){
        if (isShowFloatView){
            return;
        }
        windowManager.addView(leftView, initFloatWindowParams(1));
        windowManager.addView(rightView, initFloatWindowParams(2));
        windowManager.addView(topView,initFloatWindowParams(3));
        windowManager.addView(bottomView,initFloatWindowParams(4));
        isShowFloatView = true;
    }


    /**
     * 移除浮动窗口
     */
    private void removeFloatView(){
        if (!isShowFloatView) {
            return;
        }
        windowManager.removeViewImmediate(leftView);
        windowManager.removeViewImmediate(rightView);
        windowManager.removeViewImmediate(topView);
        windowManager.removeViewImmediate(bottomView);
        isShowFloatView = false;
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
                    Toast.makeText(TopViewService.this,"触发滑动了",Toast.LENGTH_SHORT).show();
                    addQuickView(1);
                }
                else if ((curPosX - posX < 0) && (Math.abs(curPosX-posX) > 25) && view.getTag().equals(TAG_RIGHT)){
                    Log.v(TAG,"向左滑动");
                    Toast.makeText(TopViewService.this,"触发滑动了",Toast.LENGTH_SHORT).show();
                    addQuickView(2);
                }
                if ((curPosY - posY > 0) && (Math.abs(curPosY - posY) > 25) && view.getTag().equals(TAG_TOP)){
                    Log.v(TAG,"向下滑动");
                    Toast.makeText(TopViewService.this,"触发滑动了",Toast.LENGTH_SHORT).show();
                }
                else if ((curPosY - posY < 0) && (Math.abs(curPosY-posY) > 25) && view.getTag().equals(TAG_BOTTOM)){
                    Log.v(TAG,"向上滑动");
                    Toast.makeText(TopViewService.this,"触发滑动了",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }


    private void addQuickView(int direction){
        if(isShowQuickView) {
            return;
        }
        if (direction == 1) {
            windowManager.removeViewImmediate(leftView);
            quickView.setGravity(Gravity.LEFT);
        }else if (direction ==2) {
            windowManager.removeViewImmediate(rightView);
            quickView.setGravity(Gravity.RIGHT);
        }
        windowManager.addView(quickView,initQuickViewParams(direction));
        isShowQuickView = true;
        fromDirection = direction;
    }

    private void removeQuickView(int direction){
        if(!isShowQuickView) {
            return;
        }
        windowManager.removeViewImmediate(quickView);

        if (direction == 1) {
            windowManager.addView(leftView,initFloatWindowParams(direction));
        } else if (direction == 2) {
            windowManager.addView(rightView,initFloatWindowParams(direction));
        }
        isShowQuickView = false;
    }
}

package Thousand_Dust;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import luaj.LuaError;

/**
 * @author Thousand-Dust
 */
public class MyWindowManager {

    private static MyWindowManager mWm;
    private static float alpha = 1f;

    public static final float MAXIMUM_OBSCURING_OPACITY = 0.8f;

    public static void newInstance(Context context) {
        if (mWm == null) {
            mWm = new MyWindowManager(context);
        }
    }

    public static boolean isInstanceEmpty() {
        return mWm == null;
    }

    public static MyWindowManager getInstance() {
        if (mWm == null) {
            throw new LuaError("悬浮窗未初始化，可能是无障碍功能可能未开启");
        }
        return mWm;
    }

    public static void destInstance() {
        mWm.removeAllViews();
        mWm.wm.removeView(mWm.viewGroup);
        mWm = null;
        System.gc();
    }

    public static void setAlpha(float alpha) {
        MyWindowManager.alpha = alpha;
        if (!isInstanceEmpty()) {
            //悬浮窗已实例化，更新悬浮窗透明度
            mWm.lp.alpha = alpha;
            mWm.handler.post(() -> {
                mWm.wm.updateViewLayout(mWm.viewGroup, mWm.lp);
            });
        }
    }

    public static float getAlpha() {
        return alpha;
    }

    private Handler handler = new Handler(Looper.getMainLooper());
    private WindowManager wm;
    private WindowManager.LayoutParams lp;
    private ViewGroup viewGroup;

    private MyWindowManager(Context context) {
        initView(context);
        initWindow(context);
    }

    private void initView(Context context) {
        viewGroup = new LinearLayout(context);
    }

    private void initWindow(Context context) {
        //悬浮窗类型
        int type;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alpha > MAXIMUM_OBSCURING_OPACITY) {
            type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        } else {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW,
                PixelFormat.RGBA_8888
        );
        lp.alpha = alpha;
        lp.gravity = Gravity.TOP | Gravity.LEFT;

        wm.addView(viewGroup, lp);
    }

    public synchronized void addView(View view) {
        handler.post(() -> viewGroup.addView(view));
    }

    public synchronized void removeView(View view) {
        handler.post(() -> viewGroup.removeView(view));
    }

    public synchronized void removeAllViews() {
        handler.post(() -> viewGroup.removeAllViews());
    }
}

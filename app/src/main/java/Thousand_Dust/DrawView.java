package Thousand_Dust;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import luaj.Globals;
import Thousand_Dust.luaj.LuaCanvas;
import luaj.LuaFunction;

import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * @author Thousand-Dust
 */
public class DrawView extends View {

    private Globals globals;
    private final Refresh refresh = new Refresh();

    private Paint paint;
    //缓冲图
    private Bitmap bufBitmap;
    //缓冲画布
    public Canvas canvas;
    public LuaCanvas luaCanvas;
    private LuaFunction drawFun;
    private boolean isRemove = false;

    public DrawView(Context context, Globals globals) {
        super(context);
        this.globals = globals;
        paint = new Paint();
        int[] wh = Tools.getWH(context);
        //防canvas的绘制内容画到缓冲图上
        bufBitmap = Bitmap.createBitmap(wh[0], wh[1], Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bufBitmap);
        luaCanvas = LuaCanvas.valueOf(canvas);
    }

    public void setDrawFun(LuaFunction drawFun) {
        this.drawFun = drawFun;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bufBitmap, 0, 0, paint);
    }

    @Override
    public void invalidate() {
        //将bitmap清空
        bufBitmap.eraseColor(0);
        if (drawFun != null) {
            canvas.save();
            drawFun.call(luaCanvas);
            canvas.restore();
        }
        super.invalidate();
    }

    @Override
    public void postInvalidate() {
        super.postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        close();
    }

    public void start(int fps) {
        refresh.fps = fps;
        if (!refresh.isStart) {
            new Thread(refresh).start();
        }
    }

    public void close() {
        refresh.isStart = false;
    }

    class Refresh implements Runnable {
        private boolean isStart = false;
        private int fps = 60;

        @Override
        public void run() {
            isStart = true;
            //每帧间隔时间
            long interval = 1000 / fps;
            try {
                while (isStart) {
                    //按设置帧率刷新画面
                    long startTime = System.currentTimeMillis();
                    DrawView.this.postInvalidate();
                    long timeConsuming = System.currentTimeMillis() - startTime;
                    if (timeConsuming < interval) {
                        Thread.sleep(interval - timeConsuming);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(globals.STDOUT);
                android.ext.Tools.showToast("error: " + e.getMessage());
            } finally {
                if (isStart) {
                    isStart = false;
                }
            }
        }
    }

}

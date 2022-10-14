package android.pro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import luaj.Globals;
import luaj.LuaCanvas;
import luaj.LuaError;
import luaj.LuaFunction;
import luaj.LuaValue;

import android.graphics.Paint;
import android.view.View;

/**
 * @author Thousand-Dust
 */
public class DrawView extends View {

    private Globals globals;
    private final Refresh refresh = new Refresh();

    private Paint paint;
    private Bitmap bitmap;
    //缓冲图
    private Bitmap bufBitmap;
    //缓冲画布
    public Canvas canvas;
    private LuaFunction drawFun;
    private boolean isRemove = false;

    public DrawView(Context context, Globals globals) {
        super(context);
        this.globals = globals;
        paint = new Paint();
        int[] wh = Tools.getWH(context);
        bitmap = Bitmap.createBitmap(wh[0], wh[1], Bitmap.Config.ARGB_8888);
        //防canvas的绘制内容画到缓冲图上
        bufBitmap = Bitmap.createBitmap(bitmap);
        canvas = new Canvas(bufBitmap);
    }

    public void setDrawFun(LuaFunction drawFun) {
        this.drawFun = drawFun;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bufBitmap, 0,0, paint);
    }

    @Override
    public void postInvalidate() {
        bufBitmap = Bitmap.createBitmap(bitmap);
        canvas.setBitmap(bufBitmap);
        if (drawFun != null) {
            drawFun.call(LuaCanvas.valueOf(canvas));
        }
        super.postInvalidate();
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
//                e.printStackTrace(globals.STDERR);
            } finally {
                if (isStart) {
                    isStart = false;
                }
            }
        }
    }

}

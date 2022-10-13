package android.pro;

import android.graphics.Canvas;

import luaj.LuaCanvas;
import luaj.LuaError;
import luaj.LuaFunction;
import luaj.LuaValue;
import android.view.View;

/**
 * @author Thousand-Dust
 */
public class DrawView extends View {

    private final Refresh refresh = new Refresh();
    private LuaFunction func = null;
    
    public DrawView(){
        super(android.pro.Tools.getContext());
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(func!=null) {
            try {
                func.call(LuaCanvas.valueOf(canvas));
            } catch (LuaError e) {
            }
        }
    }

    public void setDraw(LuaFunction func)
    {
        this.func = func;
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (isStart) {
                    isStart = false;
                }
            }
        }
    }

}

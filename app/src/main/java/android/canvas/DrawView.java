package android.canvas;

import android.graphics.Canvas;

import luaj.LuaCanvas;
import luaj.LuaFunction;
import luaj.LuaValue;
import android.view.View;

public class DrawView extends View {
    
    private LuaFunction func = null;
    
    public DrawView(){
        super(android.pro.Tools.getContext());
    }
    
    public void setDraw(LuaFunction func)
    {
        this.func = func;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(func!=null)
            func.call(LuaCanvas.valueOf(canvas));
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }
}

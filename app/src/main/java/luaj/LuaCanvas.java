package luaj;
import android.graphics.Canvas;

import luaj.compiler.LuaC;

public class LuaCanvas extends LuaValue {
    private Canvas canvas;
    
    @Override
    public int type() {
        return 13;
    }

    @Override
    public String typename() {
        return "canvas";
    }
    
    public static LuaCanvas valueOf(Canvas canvas){
        return new LuaCanvas(canvas);
    }

    private LuaCanvas(Canvas canvas){
        this.canvas = canvas;
    }

    public static Canvas checkcanvas(LuaValue luaValue) {
        return luaValue instanceof LuaCanvas ? ((LuaCanvas) luaValue).canvas : null;
    }
}

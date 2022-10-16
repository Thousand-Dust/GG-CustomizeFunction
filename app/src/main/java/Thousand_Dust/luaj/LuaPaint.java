package Thousand_Dust.luaj;
import android.graphics.Paint;

import luaj.LuaValue;

public class LuaPaint extends LuaValue {

    private Paint paint;
    public static LuaValue s_metatable;
    
    @Override
    public int type() {
        return 13;
    }

    @Override
    public String typename() {
        return "paint";
    }

    @Override
    public LuaValue getmetatable() {
        return s_metatable;
    }
    
    public static LuaPaint valueOf(Paint paint){
        return new LuaPaint(paint);
    }

    private LuaPaint(Paint paint){
        this.paint = paint;
    }

    public static Paint checkpaint(LuaValue luaValue) {
        return luaValue instanceof LuaPaint ? ((LuaPaint) luaValue).paint : null;
    }
    
}

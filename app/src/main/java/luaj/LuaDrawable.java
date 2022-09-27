package luaj;

import android.graphics.drawable.Drawable;

public class LuaDrawable extends LuaValue{
    private Drawable drawable;

    @Override
    public int type() {
        return 12;
    }

    @Override
    public String typename() {
        return "drawable";
    }


    public static LuaDrawable valueOf(Drawable drawable){
        return new LuaDrawable(drawable);
    }

    private LuaDrawable(Drawable drawable){
        this.drawable = drawable;
    }

    public static Drawable checkdrawable(LuaValue luaValue) {
        return luaValue instanceof LuaDrawable ? ((LuaDrawable) luaValue).drawable : null;
    }
    
}

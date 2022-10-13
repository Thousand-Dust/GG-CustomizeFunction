package luaj;

import android.pro.DrawView;

public class LuaView extends LuaValue {

    private DrawView view;
    public static LuaValue s_metatable;
    
    @Override
    public int type() {
        return 10;
    }

    @Override
    public String typename() {
        return "view";
    }

    @Override
    public LuaValue getmetatable() {
        return s_metatable;
    }
    
    public static LuaView valueOf(DrawView v){
        return new LuaView(v);
    }
    
    private LuaView(DrawView view){
        this.view = view;
    }

    public static DrawView checkview(LuaValue luaValue) {
        return luaValue instanceof LuaView ? ((LuaView) luaValue).view : null;
    }
}

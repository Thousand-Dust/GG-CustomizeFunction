package luaj;

import android.view.View;

public class LuaView extends LuaValue {
    private View view;
    
    @Override
    public int type() {
        return 10;
    }

    @Override
    public String typename() {
        return "view";
    }
    
    
    public static LuaView valueOf(View v){
        return new LuaView(v);
    }
    
    private LuaView(View view){
        this.view = view;
    }

    public static View checkview(LuaValue luaValue) {
        return luaValue instanceof LuaView ? ((LuaView) luaValue).view : null;
    }
}

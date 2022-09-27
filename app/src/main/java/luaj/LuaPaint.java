package luaj;
import android.graphics.Paint;
import android.view.View;

public class LuaPaint extends LuaValue {

    private Paint paint;
    
    @Override
    public int type() {
        return 14;
    }

    @Override
    public String typename() {
        return "paint";
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

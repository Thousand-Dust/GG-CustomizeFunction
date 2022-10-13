package android.canvas;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.pro.DrawView;
import android.pro.Tools;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import luaj.LuaString;
import luaj.LuaView;
import luaj.lib.TwoArgFunction;
import luaj.LuaValue;
import luaj.LuaTable;
import luaj.lib.VarArgFunction;
import luaj.Varargs;

/**
 * @author Thousand-Dust
 */
public class ViewLib extends TwoArgFunction {

    private WindowManager wm;
    private WindowManager.LayoutParams lp;
    private Handler handler = new Handler(Looper.getMainLooper());

    public ViewLib() {
        wm = (WindowManager) Tools.getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= 26 ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW,
                PixelFormat.RGBA_8888
        );
        lp.gravity = Gravity.TOP | Gravity.LEFT;
    }

    @Override
    public LuaValue call(LuaValue arg1, LuaValue env) {
        env.set("newView", new newView());

        LuaTable view = new LuaTable();
        //调用Lua绘制函数更新绘制
        view.set("invalidate", new invalidate());
        //显示绘制
        view.set("show", new showDraw());
        //删除绘制内容
        view.set("close", new closeDraw());

        if (!env.get("package").isnil()) {
            env.get("package").get("loaded").set("view", view);
        }
        if (LuaView.s_metatable == null) {
            LuaTable mt = LuaValue.tableOf(
                    new LuaValue[] { INDEX, view});
            LuaView.s_metatable = mt;
        }

        return env;
    }

    class newView extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            DrawView drawView = new DrawView();
            handler.post(() -> wm.addView(drawView, lp));
            return LuaView.valueOf(drawView);
        }
    }

    class invalidate extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaView.checkview(args.arg(1)).postInvalidate();
            return NONE;
        }
    }

    class showDraw extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs varargs) {
            DrawView luaView = LuaView.checkview(varargs.arg(1));
            luaView.setDraw(varargs.checkfunction(2));
            luaView.start(varargs.optint(3, 60));
            return LuaValue.NONE;
        }
    }

    class closeDraw extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs varargs) {
            DrawView luaView = LuaView.checkview(varargs.arg(1));
            luaView.close();
            handler.post(() -> wm.removeView(luaView));
            return LuaValue.NONE;
        }
    }

}

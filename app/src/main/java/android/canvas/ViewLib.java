package android.canvas;

import android.os.Build;
import android.pro.DrawView;
import android.pro.MyWindowManager;
import android.pro.Tools;

import luaj.Globals;
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

    private Globals globals;

    public ViewLib() {
        //android版本小于12直接显示悬浮窗
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            MyWindowManager.newInstance(Tools.getContext());
        }
    }

    @Override
    public LuaValue call(LuaValue arg1, LuaValue env) {
        globals = env.checkglobals();
        env.set("newView", new newView());
        env.set("removeAllView", new removeAllView());

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
            DrawView drawView = new DrawView(Tools.getContext(), globals);
            MyWindowManager.getInstance().addView(drawView);
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
            luaView.setDrawFun(varargs.checkfunction(2));
            luaView.start(varargs.optint(3, 60));
            return LuaValue.NONE;
        }
    }

    class closeDraw extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs varargs) {
            DrawView luaView = LuaView.checkview(varargs.arg(1));
            luaView.close();
            MyWindowManager.getInstance().removeView(luaView);
            return LuaValue.NONE;
        }
    }

    private class removeAllView extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs varargs) {
            MyWindowManager.getInstance().removeAllViews();
            return LuaValue.NONE;
        }
    }
}

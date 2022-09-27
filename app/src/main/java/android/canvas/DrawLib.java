package android.canvas;
import luaj.LuaView;
import luaj.lib.TwoArgFunction;
import luaj.LuaValue;
import luaj.Globals;
import luaj.LuaTable;
import luaj.lib.VarArgFunction;
import luaj.Varargs;

import luaj.lib.TwoArgFunction;

public class DrawLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue arg1, LuaValue env) {
        Globals globals = env.checkglobals();
        globals.load(new CanvasLib());
        globals.load(new PaintLib());
        globals.load(new BitmapLib());
        LuaTable tab = new LuaTable();
        tab.set("new",new newDrawLib());
        globals.set("DrawView",tab);
        return tab;
    }
    
    class mDrawLib {
        private DrawView drawView;
        
        public LuaTable get()
        {
            drawView = new DrawView();
            LuaTable t = new LuaTable();
            t.set("setDraw",new setDraw());
            t.set("getView",new getView());
            t.set("invalidate",new invalidate());
            return t;
        }
        
        class getView extends VarArgFunction
        {
            @Override
            public Varargs invoke(Varargs args) {
                return LuaView.valueOf(drawView);
            }
        }
        
        class setDraw extends VarArgFunction
        {
            @Override
            public Varargs invoke(Varargs args) {
                drawView.setDraw(args.checkfunction(1));
                return NONE;
            }
        }
        
        class invalidate extends VarArgFunction
        {
            @Override
            public Varargs invoke(Varargs args) {
                drawView.invalidate();
                return NONE;
            }
        }
    }
    
    class newDrawLib extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return new mDrawLib().get();
        }
    }
    
}

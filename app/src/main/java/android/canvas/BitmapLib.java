package android.canvas;

import android.graphics.Bitmap;

import luaj.LuaBitmap;
import luaj.LuaError;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.Varargs;
import luaj.lib.TwoArgFunction;
import luaj.lib.VarArgFunction;

public class BitmapLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        LuaTable b = new LuaTable();
        b.set("new", new newBitmap());
        arg2.set("Bitmap",b);
        return b;
    }
    
    class mBitmap{
        private Bitmap bitmap;
        
        public LuaTable get(Varargs args){
            if(args.narg()==0)
            {
                throw new LuaError("缺少参数：width,height");
            }
            bitmap = Bitmap.createBitmap(args.checkint(1), args.checkint(2), Bitmap.Config.ARGB_8888);
            LuaTable t = new LuaTable();
            t.set("getBitmap",new getBitmap());
            return t;
        }
        
        class getBitmap extends VarArgFunction {
            @Override
            public Varargs invoke(Varargs args) {
                return LuaBitmap.valueOf(bitmap);
            }
        }
    }
    
    class newBitmap extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return new mBitmap().get(args);
        }
    }
}

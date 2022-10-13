package android.canvas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import luaj.LuaBitmap;
import luaj.LuaError;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.LuaView;
import luaj.Varargs;
import luaj.lib.TwoArgFunction;
import luaj.lib.VarArgFunction;

/**
 * @author Thousand-Dust
 */
public class BitmapLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue arg1, LuaValue env) {
        env.set("loadBitmap", new loadBitmap());
        LuaTable table = new LuaTable();
        table.set("setWidth", new setWidth());
        table.set("setHeight", new setHeight());
        table.set("setWH", new setWH());
        table.set("remove", new remove());

        if (LuaBitmap.s_metatable == null) {
            LuaTable mt = LuaValue.tableOf(
                    new LuaValue[] { INDEX, table});
            LuaBitmap.s_metatable = mt;
        }

        return env;
    }

    class loadBitmap extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaBitmap.valueOf(BitmapFactory.decodeFile(args.checkjstring(1)));
        }
    }

    class setWidth extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            Bitmap bitmap = LuaBitmap.checkbitmap(args.arg(1));
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(width/args.tofloat(2), 1);
            return LuaBitmap.valueOf(Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true));
        }
    }

    class setHeight extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            Bitmap bitmap = LuaBitmap.checkbitmap(args.arg(1));
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(1, height/args.tofloat(2));
            return LuaBitmap.valueOf(Bitmap.createBitmap(bitmap, 0,0, width, height, matrix, true));
        }
    }

    class setWH extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            Bitmap bitmap = LuaBitmap.checkbitmap(args.arg(1));
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(width/args.tofloat(2), height/args.tofloat(3));
            return LuaBitmap.valueOf(Bitmap.createBitmap(bitmap, 0,0, width, height, matrix, true));
        }
    }

    class remove extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            Bitmap bitmap = LuaBitmap.checkbitmap(args.arg(1));
            bitmap.recycle();
            return NONE;
        }
    }

}

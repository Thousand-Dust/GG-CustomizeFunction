package android.canvas;
import luaj.LuaPaint;
import luaj.LuaString;
import luaj.lib.TwoArgFunction;
import luaj.LuaValue;
import luaj.lib.VarArgFunction;
import luaj.LuaTable;
import luaj.Varargs;
import android.graphics.Paint;
import luaj.LuaError;
import android.graphics.Color;

/**
 * @author Thousand-Dust
 */
public class PaintLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue arg1, LuaValue env) {

        env.set("newPaint", new newPaint());

        LuaTable paint = new LuaTable();
        //设置画笔笔触宽
        paint.set("setWidth", new setWidth());
        //设置画笔样式
        paint.set("setStyle", new setStyle());
        //设置画笔颜色
        paint.set("setColor", new setColor());
        //设置画笔画出的字符大小
        paint.set("setTextSize", new setTextSize());
        //设置抗锯齿
        paint.set("setAntiAlias", new setAntiAlias());

        if (!env.get("package").isnil()) {
            env.get("package").get("loaded").set("paint", paint);
        }
        if (LuaPaint.s_metatable == null) {
            LuaTable mt = LuaValue.tableOf(
                    new LuaValue[] { INDEX, paint});
            LuaPaint.s_metatable = mt;
        }

        return env;
    }

    class newPaint extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return LuaPaint.valueOf(new Paint());
        }
    }

    class setWidth extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaPaint.checkpaint(args.arg(1)).setStrokeWidth(args.checkint(2));
            return NONE;
        }
    }

    class setStyle extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaValue value = args.checkvalue(2);

            Paint.Style style;
            if (value instanceof LuaString) {
                String styleStr = value.checkjstring();
                switch (styleStr) {
                    case "描边":
                        style = Paint.Style.STROKE;
                        break;
                    case "填充":
                        style = Paint.Style.FILL;
                        break;
                    case "描边并填充":
                        style = Paint.Style.FILL_AND_STROKE;
                        break;

                    default:
                        throw new LuaError("未知的画笔类型");
                }
            } else {
                int styleNum = value.checkint();
                switch (styleNum) {
                    case 0:
                        //描边
                        style = Paint.Style.STROKE;
                        break;
                    case 1:
                        //填充
                        style = Paint.Style.FILL;
                        break;
                    case 2:
                        //描边并填充
                        style = Paint.Style.FILL_AND_STROKE;
                        break;

                    default:
                        throw new LuaError("未知的画笔类型");
                }
            }

            LuaPaint.checkpaint(args.arg(1)).setStyle(style);

            return NONE;
        }
    }

    class setColor extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaPaint.checkpaint(args.arg(1)).setColor(Color.parseColor(args.checkjstring(2)));
            return NONE;
        }
    }

    class setTextSize extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaPaint.checkpaint(args.arg(1)).setTextSize(args.tofloat(2));
            return NONE;
        }
    }

    class setAntiAlias extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaPaint.checkpaint(args.arg(1)).setAntiAlias(args.checkboolean(2));
            return NONE;
        }
    }
}

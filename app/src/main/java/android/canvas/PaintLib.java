package android.canvas;
import luaj.LuaPaint;
import luaj.lib.TwoArgFunction;
import luaj.LuaValue;
import luaj.lib.VarArgFunction;
import luaj.LuaTable;
import luaj.Varargs;
import android.graphics.Paint;
import luaj.LuaError;
import android.graphics.Color;

public class PaintLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        LuaTable paint = new LuaTable();
        paint.set("new",new newPaint());
        //属性
        LuaTable Style = new LuaTable();
        Style.set("FILL",1);
        Style.set("STROKE",2);
        Style.set("FILL_AND_STROKE",3);
        
        paint.set("Style",Style);
        arg2.set("Paint",paint);
        return paint;
    }
    
    class mPaint {
        private Paint paint;
        
        public LuaTable get(){
            paint = new Paint();
            LuaTable t = new LuaTable();
            t.set("getPaint",new getPaint());
            t.set("setAntialias", new setAntialias());//设置画笔是否开启抗锯齿
            t.set("setAlpha", new setAlpha());//设置画笔透明度
            t.set("setColor", new setColor());//设置画笔颜色
            t.set("setStyle", new setStyle());//设置画笔样式
            t.set("setTextSize", new setTextSize());//设置画笔绘制文本时的文本大小
            t.set("setWidth", new setWidth());//设置画笔笔触宽度
            t.set("setPaint",new setPaint());
            
            return t;
        }
        
        class setPaint extends VarArgFunction
        {
            @Override
            public Varargs invoke(Varargs args) {
                if(args.arg(1).typename().equals("table")){
                    paint = LuaPaint.checkpaint(args.checktable(1).get("getPaint"));
                }
                else if(args.arg(1).typename().equals("paint"))
                {
                    paint = LuaPaint.checkpaint(args.arg(1));
                }else
                {
                    throw new LuaError("参数类型错误，应该是paint");
                }
                return NONE;
            }
        }
        
        class getPaint extends VarArgFunction
        {
            @Override
            public Varargs invoke(Varargs args) {
                return LuaPaint.valueOf(paint);
            }
        }
        
        class setAntialias extends VarArgFunction {

            public Varargs invoke(Varargs varargs) {
                paint.setAntiAlias(varargs.checkboolean(1));
                return NONE;
            }
        }

        class setAlpha extends VarArgFunction {

            public Varargs invoke(Varargs varargs) {
                paint.setAlpha(varargs.checkint(1));
                return NONE;
            }
        }

        class setColor extends VarArgFunction {

            public Varargs invoke(Varargs varargs) {
                paint.setColor(Color.parseColor(varargs.checkjstring(1)));
                return NONE;
            }
        }

        class setStyle extends VarArgFunction {

            public Varargs invoke(Varargs varargs) {
                int style = varargs.checkint(1);

                switch (style) {
                    case 1:
                        //填充
                        paint.setStyle(Paint.Style.FILL);
                        break;
                    case 2:
                        //描边
                        paint.setStyle(Paint.Style.STROKE);
                        break;
                    case 3:
                        //填充并描边
                        paint.setStyle(Paint.Style.FILL_AND_STROKE);
                        break;
                    default:
                        throw new LuaError("没有该样式：请选择1,2,3\n1：填充 2：描边 3：填充并描边");
                }
                return NONE;
            }
        }

        class setTextSize extends VarArgFunction {

            public Varargs invoke(Varargs varargs) {
                paint.setTextSize(varargs.tofloat(1));
                return NONE;
            }
        }

        class setWidth extends VarArgFunction {

            public Varargs invoke(Varargs varargs) {
                paint.setStrokeWidth(varargs.tofloat(1));
                return NONE;
            }
        }
    }
    
    class newPaint extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return new mPaint().get();
        }
        
    }
}

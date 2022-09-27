package android.canvas;
import luaj.LuaBitmap;
import luaj.LuaCanvas;
import luaj.LuaPaint;
import luaj.lib.TwoArgFunction;
import luaj.LuaValue;
import luaj.Globals;
import luaj.LuaTable;
import luaj.lib.VarArgFunction;
import android.graphics.Canvas;
import luaj.Varargs;
import android.graphics.RectF;
import android.graphics.Paint;
import luaj.LuaError;
import android.graphics.Rect;
import android.graphics.Color;

public class CanvasLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue arg1, LuaValue env) {
        LuaTable canvas = new LuaTable();
        canvas.set("new", new newCanvas());
        env.set("Canvas", canvas);
        return canvas;
    }

    class mCanvas {
        private Canvas canvas;

        public LuaTable get(Varargs args) {
            if (args.arg(1).typename().equals("bitmap")) {
                canvas = new Canvas(LuaBitmap.checkbitmap(args.arg(1)));
            } else if (args.arg(1).typename().equals("canvas")) {
                canvas = LuaCanvas.checkcanvas(args.arg(1));
            } else if (args.arg(1).typename().equals("table")) {
                canvas = new Canvas(LuaBitmap.checkbitmap(args.checktable(1).rawget("getBitmap").call()));
            } else {
                throw new LuaError("参数错误，应该是：bitmap或canvas");
            }
            LuaTable t = new LuaTable();
            t.set("getCanvas", new getCanvas());
            t.set("clipRect", new clipRect());//绘制内容四边形范围裁切
            t.set("setBitmap", new setBitmap());//设置画布背景图片
            t.set("translate", new translate());//移动画布原点
            t.set("drawArc", new drawArc());//绘制圆弧
            t.set("drawBitmap", new drawBitmap());//绘制Bitmap
            t.set("drawCircle", new drawCircle());//绘制圆
            t.set("drawColor", new drawColor());//绘制画布背景颜色
            t.set("drawLine", new drawLine());//绘制线段
            t.set("drawLines", new drawLines());//绘制连接的多条线段
            // t.set("drawProgress", new drawProgress());//绘制进度条
            t.set("drawRect", new drawRect());//绘制四边形
            t.set("drawText", new drawText());//绘制文字
            t.set("setCanvas", new setCanvas());
            return t;
        }

        public float getFloat(Varargs args,int i)
        {
            return (float)args.checkdouble(i);
        }
        class getCanvas extends VarArgFunction {
            @Override
            public Varargs invoke(Varargs args) {
                return LuaCanvas.valueOf(canvas);
            }
        }

        class setCanvas extends VarArgFunction {
            @Override
            public Varargs invoke(Varargs args) {
                if (args.arg(1).typename().equals("canvas"))
                    canvas = LuaCanvas.checkcanvas(args.arg(1));
                else if (args.arg(1).typename().equals("table")) {
                    canvas = LuaCanvas.checkcanvas(args.checktable(1).rawget("getCanvas"));
                } else {
                    throw new LuaError("参数类型错误");
                }
                return NONE;
            }
        }

        class clipRect extends VarArgFunction {
            public Varargs invoke(Varargs var) {
                RectF rect = new RectF(var.tofloat(1), var.tofloat(2), var.tofloat(3), var.tofloat(4));
                canvas.clipRect(rect);
                return NONE;
            }
        }

        class setBitmap extends VarArgFunction {
            public Varargs invoke(Varargs var) {
                if (var.arg(1).typename().equals("bitmap")) {
                    canvas.setBitmap(LuaBitmap.checkbitmap(var.arg(1)));
                } else if (var.arg(1).typename().equals("table")) {
                    canvas.setBitmap(LuaBitmap.checkbitmap(var.checktable(1).rawget("getBitmap").call()));
                }
                return NONE;
            }
        }

        class translate extends VarArgFunction {
            public Varargs invoke(Varargs var) {
                canvas.translate(var.checkint(1), var.checkint(2));
                return NONE;
            }
        }

        class drawArc extends VarArgFunction {
            public Varargs invoke(Varargs var) {
                if (var.narg() != 5) {
                    throw new LuaError("参数数量错误，应为5个参数");
                }
                LuaTable tab = var.checktable(1);
                RectF rectf = new RectF((float)tab.rawget(1).checkdouble(),
                                        (float)tab.rawget(2).checkdouble(),
                                        (float)tab.rawget(3).checkdouble(),
                                        (float)tab.rawget(4).checkdouble());
                Paint paint = null;
                if (var.arg(5).typename().equals("table")) {
                    paint = LuaPaint.checkpaint(var.checktable(5).rawget("getPaint").call());
                } else if (var.arg(5).typename().equals("paint")) {
                    paint = LuaPaint.checkpaint(var.arg(5));
                } else {
                    throw new LuaError("参数5类型错误，应该是paint");
                }
                canvas.drawArc(rectf, (float)checkdouble(2), (float)checkdouble(3), checkboolean(4), paint);
                return NONE;
            }
        }

        class drawBitmap extends VarArgFunction {
            public Varargs invoke(Varargs var) {
                LuaTable tab = var.checktable(2);
                LuaTable tab2 = var.checktable(3);
                Rect rect1 = new Rect(tab.rawget(1).checkint(),
                                      tab.rawget(2).checkint(),
                                      tab.rawget(3).checkint(),
                                      tab.rawget(4).checkint());
                RectF rectf2 = new RectF((float)tab2.rawget(1).checkdouble(),
                                         (float)tab2.rawget(2).checkdouble(),
                                         (float)tab2.rawget(3).checkdouble(),
                                         (float)tab2.rawget(4).checkdouble());
                Paint paint = null;
                if (var.arg(4).typename().equals("table")) {
                    paint = LuaPaint.checkpaint(var.checktable(4).get("getPaint"));
                } else if (var.arg(4).typename().equals("paint")) {
                    paint = LuaPaint.checkpaint(var.arg(4));
                } else {
                    throw new LuaError("参数4类型错误，应该是paint");
                }
                canvas.drawBitmap(LuaBitmap.checkbitmap(arg(1)), rect1, rectf2, paint);
                return NONE;
            }
        }

        class drawCircle extends VarArgFunction {

            public Varargs invoke(Varargs var) {
                Paint paint = null;
                if (var.arg(4).typename().equals("table")) {
                    paint = LuaPaint.checkpaint(var.checktable(4).get("getPaint"));
                } else if (var.arg(4).typename().equals("paint")) {
                    paint = LuaPaint.checkpaint(var.arg(4));
                } else {
                    throw new LuaError("参数4类型错误，应该是paint");
                }
                canvas.drawCircle((float)var.checkdouble(1), (float)var.checkdouble(2), (float)var.checkdouble(3), paint);
                return NONE;
            }
        }

        class drawColor extends VarArgFunction {
            public Varargs invoke(Varargs var) {
                canvas.drawColor(Color.parseColor(checkjstring(1)));
                return NONE;
            }
        }

        class drawRect extends VarArgFunction {
            public Varargs invoke(Varargs var) {
                LuaTable tab = var.checktable(1);
                RectF rectf = new RectF((float)tab.rawget(1).checkdouble(),
                                        (float)tab.rawget(2).checkdouble(),
                                        (float)tab.rawget(3).checkdouble(),
                                        (float)tab.rawget(4).checkdouble());
                Paint paint = null;
                if (var.arg(2).typename().equals("table")) {
                    paint = LuaPaint.checkpaint(var.checktable(2).get("getPaint"));
                } else if (var.arg(2).typename().equals("paint")) {
                    paint = LuaPaint.checkpaint(var.arg(2));
                } else {
                    throw new LuaError("参数2类型错误，应该是paint");
                }    
                canvas.drawRect(rectf, paint);
                return NONE;
            }
        }

        class drawLine extends VarArgFunction {

            public Varargs invoke(Varargs var) {
                    Paint paint = null;
                    if (var.arg(5).typename().equals("table")) {
                        paint = LuaPaint.checkpaint(var.checktable(5).rawget("getPaint").call());
                    } else if (var.arg(5).typename().equals("paint")) {
                        paint = LuaPaint.checkpaint(var.arg(5));
                    } else {
                        throw new LuaError("参数5类型错误，应该是paint");
                    }
                    
                    canvas.drawLine(getFloat(var,1),
                                getFloat(var,2),
                                getFloat(var,3),
                                getFloat(var,4),
                                    paint);
                                  
                return NONE;
            }
        }

        class drawLines extends VarArgFunction {
            public Varargs invoke(Varargs var) {
                LuaTable table = var.checktable(1);
                int len = table.length();
                float[] floats = new float[len];
                for (int i = 0; i < len - 1; i++) {
                    floats[i] = table.get(i + 1).tofloat();
                }
                Paint paint = null;
                if (var.arg(2).typename().equals("table")) {
                    paint = LuaPaint.checkpaint(var.checktable(2).get("getPaint"));
                } else if (var.arg(2).typename().equals("paint")) {
                    paint = LuaPaint.checkpaint(var.arg(2));
                } else {
                    throw new LuaError("参数2类型错误，应该是paint");
                }
                canvas.drawLines(floats, paint);
                return NONE;
            }
        }

        class drawText extends VarArgFunction {
            public Varargs invoke(Varargs var) {
                Paint paint = null;
                if (var.arg(4).typename().equals("table")) {
                    paint = LuaPaint.checkpaint(var.checktable(4).get("getPaint"));
                } else if (var.arg(4).typename().equals("paint")) {
                    paint = LuaPaint.checkpaint(var.arg(4));
                } else {
                    throw new LuaError("参数4类型错误，应该是paint");
                }
                canvas.drawText(var.checkjstring(1), (float)var.checkdouble(2), (float)var.checkdouble(3), paint);
                return NONE;
            }
        }

    }

    class newCanvas extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            return new mCanvas().get(args);
        }
    }

}

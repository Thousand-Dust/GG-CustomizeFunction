package Thousand_Dust.canvas;

import Thousand_Dust.luaj.LuaBitmap;
import Thousand_Dust.luaj.LuaCanvas;
import luaj.LuaFunction;
import Thousand_Dust.luaj.LuaPaint;
import luaj.lib.TwoArgFunction;
import luaj.LuaValue;
import luaj.LuaTable;
import luaj.lib.VarArgFunction;

import android.graphics.Canvas;

import luaj.Varargs;

import android.graphics.RectF;
import android.graphics.Paint;

import luaj.LuaError;

import android.graphics.Color;

/**
 * @author Thousand-Dust
 */
public class CanvasLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaTable canvas = new LuaTable();
        //绘制圆弧
        canvas.set("drawArc", new drawArc());
        //绘制Bitmap图片
        canvas.set("drawBitmap", new drawBitmap());
        //绘制圆
        canvas.set("drawCircle", new drawCircle());
        //绘制背景颜色
        canvas.set("drawColor", new drawColor());
        //绘制线段
        canvas.set("drawLine", new drawLine());
        //绘制多条线段
        canvas.set("drawLines", new drawLines());
        //绘制四边形
        canvas.set("drawRect", new drawRect());
        //绘制字符串
        canvas.set("drawText", new drawText());
        //裁剪绘制内容（四边形）
        canvas.set("clipRect", new clipRect());
        //旋转画布
        canvas.set("rotate", new rotate());
        //移动画布原点
        canvas.set("translate", new translate());
        //保存画布状态
        canvas.set("save", new Save());
        //恢复上次保存的画布状态
        canvas.set("restore", new Restore());

        if (!env.get("package").isnil()) {
            env.get("package").get("loaded").set("canvas", canvas);
        }
        if (LuaCanvas.s_metatable == null) {
            LuaTable mt = LuaValue.tableOf(
                    new LuaValue[] { INDEX, canvas});
            LuaCanvas.s_metatable = mt;
        }

        return env;
    }

    class MyThread extends VarArgFunction {
        @Override
        public Varargs invoke(final Varargs args) {

            new Thread() {
                @Override
                public void run() {
                    LuaFunction fun = args.checkfunction(1);
                    try {
                        if (args.narg() == 1) {
                            fun.call();
                        } else {
                            fun.call(args.checktable(2));
                        }
                    } catch (Exception e) {
                        throw new LuaError(e.toString());
                    }
                }
            }.start();

            return NONE;
        }

    }

    class drawArc extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable table = args.checktable(2);
            RectF rectF = new RectF(table.tofloat(1), table.tofloat(2), table.tofloat(3), table.tofloat(4));
            LuaCanvas.checkcanvas(args.arg(1))
                    .drawArc(rectF, args.tofloat(3), args.tofloat(4), args.checkboolean(5), LuaPaint.checkpaint(args.arg(6)));
            return NONE;
        }
    }

    class drawBitmap extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaCanvas.checkcanvas(args.arg(1)).drawBitmap(LuaBitmap.checkbitmap(args.arg(2)), args.tofloat(3), args.tofloat(4), LuaPaint.checkpaint(args.arg(5)));
            return NONE;
        }
    }

    class drawCircle extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaCanvas.checkcanvas(args.arg(1))
                    .drawCircle(args.tofloat(2), args.tofloat(3), args.tofloat(4), LuaPaint.checkpaint(args.arg(5)));
            return NONE;
        }
    }

    class drawColor extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaCanvas.checkcanvas(args.arg(1)).drawColor(Color.parseColor(args.tojstring(2)));
            return NONE;
        }
    }

    class drawLine extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaCanvas.checkcanvas(args.arg(1))
                    .drawLine(args.tofloat(2), args.tofloat(3), args.tofloat(4), args.tofloat(5), LuaPaint.checkpaint(args.arg(6)));
            return NONE;
        }
    }

    class drawLines extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable table = args.checktable(2);
            float[] floats = new float[table.length()];
            for (int i = 0; i < floats.length; i++) {
                floats[i] = table.get(i).tofloat();
            }
            LuaCanvas.checkcanvas(args.arg(1)).drawLines(floats, LuaPaint.checkpaint(args.arg(3)));
            return NONE;
        }
    }

    class drawProgress extends VarArgFunction {
        private Paint framePaint;
        private Paint progressPaint;

        public drawProgress(Paint paint) {
            this.framePaint = paint;
            progressPaint = new Paint();
            progressPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        public Varargs invoke(Varargs args) {
            int narg = args.narg();
            if (narg >= 6) {
                framePaint.setColor(Color.parseColor(args.checkjstring(6))); //使用用户自定义背景颜色
            } else {
                framePaint.setColor(Color.parseColor("#FFFFFF"));
                //判断是否使用用户自定义进度颜色
                if (narg == 5) {
                    progressPaint.setColor(Color.parseColor(args.checkjstring(5)));
                } else {
                    progressPaint.setColor(Color.parseColor("#FF0000"));
                }
            }
            Canvas canvas = LuaCanvas.checkcanvas(args.arg(1)); //使用默认画笔
            //获得进度条背景矩形
            float left, top, right, bottom;
            {
                LuaTable table = args.checktable(2);
                left = table.get(1).tofloat();
                top = table.get(2).tofloat();
                right = table.get(3).tofloat();
                bottom = table.get(4).tofloat();
            }
            RectF frameRect = new RectF(left, top, right, bottom);
            //绘制进度条背景
            canvas.drawRect(frameRect, framePaint);
            //计算进度矩形
            float max = args.tofloat(3);
            float progress = args.tofloat(4);
            float proportion = (right - left) / max;
            RectF internel = new RectF(left, top, left + (progress * proportion), bottom);
            //绘制进度
            canvas.drawRect(internel, progressPaint);

            return NONE;
        }
    }

    class drawRect extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable table = args.checktable(2);
            RectF rectF = new RectF(table.get(1).tofloat(), table.get(2).tofloat(), table.get(3).tofloat(), table.get(4).tofloat());
            LuaCanvas.checkcanvas(args.arg(1)).drawRect(rectF, LuaPaint.checkpaint(args.arg(3)));
            return NONE;
        }
    }

    class drawText extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaCanvas.checkcanvas(args.arg(1))
                    .drawText(args.checkjstring(2), args.tofloat(3), args.tofloat(4), LuaPaint.checkpaint(args.arg(5)));
            return NONE;
        }
    }

    class clipRect extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaTable table = args.checktable(2);
            LuaCanvas.checkcanvas(args.arg(1)).clipRect(new RectF(table.tofloat(1), table.tofloat(2), table.tofloat(3), table.tofloat(4)));
            return NONE;
        }
    }

    class rotate extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            Canvas canvas = LuaCanvas.checkcanvas(args.arg(1));
            switch (args.narg()) {
                case 2:
                    canvas.rotate(args.tofloat(2));
                    break;
                case 4:
                    canvas.rotate(args.tofloat(2), args.tofloat(3), args.tofloat(4));
                    break;
                default:
                    throw new LuaError("there is no such method: " + args);
            }
            return NONE;
        }
    }

    class translate extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaCanvas.checkcanvas(args.arg(1)).translate(args.tofloat(2), args.tofloat(3));
            return NONE;
        }
    }

    class Save extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaCanvas.checkcanvas(args.arg(1)).save();
            return NONE;
        }
    }

    class Restore extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {
            LuaCanvas.checkcanvas(args.arg(1)).restore();
            return NONE;
        }
    }

}
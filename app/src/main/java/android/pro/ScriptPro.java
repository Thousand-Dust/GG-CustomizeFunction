package android.pro;

import android.canvas.BitmapLib;
import android.content.Context;
import android.app.AlertDialog;
import java.util.Map;
import android.util.ArrayMap;
import android.os.Build;
import android.view.WindowManager;
import android.content.DialogInterface;
import android.ext.ThreadManager;
import android.widget.Toast;
import android.canvas.DrawLib;

import luaj.Globals;
import luaj.LoadState;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.Varargs;
import luaj.compiler.LuaC;
import luaj.lib.TwoArgFunction;
import luaj.lib.VarArgFunction;

public class ScriptPro extends TwoArgFunction {

    private Globals globals;
    private final String tabName = "pro";

    public ScriptPro(Globals globals) {
        this.globals = globals;
        init();
    }

    @Override
    public LuaValue call(LuaValue arg1, LuaValue env) {
        LuaTable tab = new LuaTable();
        tab.set("VERSION", BaseInfo.getVersion());
        tab.set("UPTIME", BaseInfo.getUpdateTime());
        tab.set("AUTHOR", BaseInfo.getAuthor());
        tab.set("killMTP", new closeMTP());
        tab.set("execCmd", new execCmd());
        tab.set("toast",new toast());
        tab.set("sleep",new sleep());
        env.set(tabName, tab);
        env.set("newThread", new newThread());
        return tab;
    }

    private void init() {
        this.globals.load(this);
        this.globals.load(new DrawLib());
        this.globals.load(new BitmapLib());
        LoadState.install(this.globals);
        LuaC.install(this.globals);
    }

    //新线程
    class newThread extends VarArgFunction {
        public Varargs invoke(final Varargs args) {
            new Thread(new Runnable(){
                    @Override
                    public void run() {
                        args.checkfunction(1).call();
                    }
                }).start();
            return NONE;
        }
    }

    //关闭MTP检测
    class closeMTP extends VarArgFunction {
        public Varargs invoke(final Varargs args) {
            try {
                Context appContext = Tools.getContext().createPackageContext(args.checkjstring(1), Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
                String path = appContext.getFilesDir().getAbsolutePath();
                String rs = args.checkboolean(2) == false ?"sh ": "su -c ";
                Runtime.getRuntime().exec(rs + "chmod 0000 " + path + "/tss_tmp/tssmua.zip");
                Runtime.getRuntime().exec(rs + "chmod 0000 " + path + "/files/tss_tmp/");
            } catch (Exception e) {
                return LuaValue.valueOf(e.toString());
            }
            return LuaValue.valueOf(args.checkjstring(1) + ":MTP搜索检测已关闭");
        }
    }



    //执行系统命令
    class execCmd extends VarArgFunction {
        boolean isClock = false;
        private String result = "";
        private Map<String,String> PermisDescrip = new ArrayMap<String,String>();
        private String[][] p = new String[][]{
            {"rm","此命令会删除您的文件/文件夹，且无法恢复。"},
            {"wget","此命令会从指定链接下载脚本，若您无法确认下载源是否安全，请不要继续执行。"},
            {"curl","此命令会访问网络或下载文件，若您无法确认访问是否安全，请不要继续执行。"},
            {"reboot","此命令会重启手机。"},
            {"shutdown","此命令会在设定时间重启/关闭手机。"},
            {"halt","此命令会直接关机。"},
            {"poweroff","此命令会直接关机。"},
            {":(){:|: &};:","此命令会无终止的条件递归。如果执行此操作，它将快速复制自己，从而消耗完所有的内存和CPU资源，导致冻结您的整个系统，若非特殊需求请勿继续执行！"}
        };
        
        private void initPermisDescrip()
        {
            for(String[] obj:p)
                PermisDescrip.put(obj[0],obj[1]);
        }
        
        @Override
        public Varargs invoke(Varargs args) {
            initPermisDescrip();
            String cmd = args.checkjstring(1);
            for(String[] s :p){
              if(cmd.indexOf(s[0]) != -1){
                  showAlert(getMsg(s[0],cmd),cmd);
                  return LuaValue.valueOf(result);
              }
            }
            try {
                return LuaValue.valueOf(Tools.execCmd(cmd, null));
            } catch (Exception e) {
                return LuaValue.valueOf(e.getMessage());
            }
        }
        
        public String getMsg(String name,String cmd)
        {
            String result = 
            "脚本正在执行高危命令：" + name +
            "\n\n" +
            "执行的代码：" +
            cmd +
            "\n\n" +
            PermisDescrip.get(name) +
            "\n\n" +
            "是否忽略风险并执行？";
            return result;
        }

        public void showAlert(final String content,final String cmd) {
            
            Runnable run = new Runnable(){
                public void run() {
                    final AlertDialog.Builder ab = new AlertDialog.Builder(Tools.getContext());
                    ab.setTitle("注意");
                    ab.setMessage(content);
                    ab.setCancelable(false); 
                    ab.setPositiveButton("取消执行", new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                isClock = true;
                                result = "refuse";
                                ab.create().dismiss();
                            }
                    }); 
                    ab.setNegativeButton("继续执行", new AlertDialog.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                isClock = true;
                                try {
                                    result = Tools.execCmd(cmd, null);
                                } catch (Exception e) {
                                    result = e.getMessage();
                                }
                            }
                        });
                    final AlertDialog dialog = ab.create();
                    if (Build.VERSION.SDK_INT >= 26) {
                        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                    } else {
                        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    }
                    dialog.show();
                }
            };
            ThreadManager.runOnUiThread(run);
            while(isClock == false)Thread.yield();
        }
    }
    
    //toast
    class toast extends VarArgFunction
    {
        private Toast toast = null;
        
        public void showToast(Context context, String str){
            if(toast != null)
                toast.cancel();
            toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
            toast.show();
        }
        
        @Override
        public Varargs invoke(final Varargs args) {
            ThreadManager.runOnUiThread(new Runnable(){
                public void run(){
                    showToast(Tools.getContext(),args.checkjstring(1));
                }
            });
            return NONE;
        }
    }
    
    
    //sleep
    class sleep extends VarArgFunction
    {
        @Override
        public Varargs invoke(Varargs args) {
            try {
                Thread.sleep(args.checkint(1));
            } catch (InterruptedException e)
            {}
            return NONE;
        } 
    }
}

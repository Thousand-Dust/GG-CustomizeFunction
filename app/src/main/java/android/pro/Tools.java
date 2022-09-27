package android.pro;

import android.content.Context;
import android.widget.Toast;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Closeable;
import android.ext.ThreadManager;
import android.graphics.Bitmap;
import android.os.StrictMode;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.PixelFormat;

public class Tools
{
    public static Context getContext(){
        return android.ext.Tools.getContext();
    }
    
    public void toast(final String str)
    {
        Runnable run = new Runnable(){
            @Override
            public void run() {
                Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
            }
        };
        ThreadManager.runOnUiThread(run);
    }
    
    /**
     * 执行系统命令, 返回执行结果
     *
     * @param cmd 需要执行的命令
     * @param dir 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
     */
    public static String execCmd(String cmd, File dir) throws Exception {
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;

        try {
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(cmd, null, dir);

            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }

        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);
            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }

        // 返回执行结果
        return result.toString().substring(0,result.length()-1);
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }
    
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }
    
    public static Bitmap getHttpBitmap(String url) {  
        if (isMainThread()) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        URL myFileURL;  
        Bitmap bitmap=null;  
        try {  
            myFileURL = new URL(url);
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();  
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);  
            conn.setUseCaches(false);
            conn.connect();  
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();  
        } catch (Exception e) {  
            e.printStackTrace();
        }
        return bitmap;
    }
    
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap( drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}

package android.socket;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import luaj.LuaString;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.Varargs;
import luaj.lib.TwoArgFunction;
import luaj.lib.VarArgFunction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LuaServerSocket extends TwoArgFunction {

    private interface SocketEvent {
        void socketEvent(LuaString luaStr);
    }

    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        LuaTable t = new LuaTable();
        t.set("setSocketEvent", new setSocketEvent());
        arg2.set("SeverSocket", t);

        return t;
    }

    class setSocketEvent extends VarArgFunction {

        private ServerSocket ss;
        private Socket socket;
        private SocketEvent event;

        private Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0x001:
                        //向lua发送接收到的Socket消息内容
                        event.socketEvent((LuaString)msg.obj);
                        break;
                }
            }
        };

        @Override
        public Varargs invoke(final Varargs args) {
            new Thread() {
                @Override
                public void run() {
                    InputStream in = null;
                    ByteArrayOutputStream out = null;
                    while (true) {
                        try {
                            ss = new ServerSocket(args.checkint(1));
                            socket = ss.accept();
                            in = socket.getInputStream();
                            out = new ByteArrayOutputStream();
                            byte[] bytes = new byte[1024];
                            int len = -1;
                            while ((len = in.read(bytes)) != -1) {
                                out.write(bytes, 0, len);
                            }

                            //发送消息，让lua线程向lua发送接收到的Socket消息内容
                            Message message = new Message();
                            message.what = 0x001;
                            message.obj = LuaString.valueOf(out.toByteArray());
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            continue;
                        } finally {
                            try {
                                if (in != null)
                                    in.close();
                                if (out != null)
                                    out.close();
                            } catch (IOException e) {
                            }
                        }
                    }

                }
            }.start();

            event = new SocketEvent() {
                @Override
                public void socketEvent(LuaString luaStr) {
                    args.checkfunction(2).call(luaStr);
                }
            };

            return NONE;
        }

    }

}

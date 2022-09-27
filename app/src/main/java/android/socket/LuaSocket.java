package android.socket;

import luaj.LuaError;
import luaj.LuaString;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.Varargs;
import luaj.lib.TwoArgFunction;
import luaj.lib.VarArgFunction;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class LuaSocket extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue arg1, LuaValue arg2) {
        LuaTable t = new LuaTable();
        t.set("new", new newSocket());
        arg2.set("Socket", t);

        return t;
    }

    class mSocket {

        private Socket socket;

        public Varargs get(Varargs arg) {
            try {
                socket = new Socket(arg.checkjstring(1), arg.checkint(2));
            } catch (IOException e) {
                throw new LuaError("来自Socket的错误："+e.toString());
            }

            LuaTable t = new LuaTable();
            t.set("write", new Write());

            return t;
        }

        class Write extends VarArgFunction {
            @Override
            public Varargs invoke(Varargs args) {
                LuaString content = args.checkstring(1);
                OutputStream out = null;
                try {
                    out = socket.getOutputStream();
                    out.write(content.m_bytes, content.m_offset, content.m_length);
                    out.flush();
                } catch (IOException e) {
                    throw new LuaError("来自Socket.write："+e.toString());
                } finally {
                    try {
                        if (out != null)
                            out.close();
                    } catch (IOException e) {
                    }
                }

                return NONE;
            }
        }

    }

    class newSocket extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs arg) {
            return new mSocket().get(arg);
        }
    }

}

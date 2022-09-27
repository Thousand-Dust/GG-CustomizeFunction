package luaj.lib;

import luaj.Varargs;

final class BaseLib$thread extends VarArgFunction {

    final BaseLib baseLib;

    BaseLib$thread(BaseLib baseLib) {
        this.baseLib = baseLib;
    }

    @Override
    public Varargs invoke(Varargs args) {
        new Thread(() -> args.checkfunction(1).call()).start();
        return NONE;
    }
}

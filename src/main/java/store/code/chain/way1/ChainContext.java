package store.code.chain.way1;

import artoria.core.Context;

public interface ChainContext extends Context {

    Object[] getArguments();

    Object getData();

    void setData(Object data);

}

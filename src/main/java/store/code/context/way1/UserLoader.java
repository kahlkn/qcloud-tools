package store.code.context.way1;

import artoria.common.Loader;

public interface UserLoader extends Loader {

    UserInfo load(Object input);

}

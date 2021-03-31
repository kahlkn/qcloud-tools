package store.code.cache.way1;

import artoria.collection.ReferenceMap;

import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.ZERO;

/**
 * Memory cache simple implement by jdk.
 * @author Kahle
 */
public class SimpleCache extends AbstractCache {

    public SimpleCache(String name) {

        this(name, ReferenceMap.Type.WEAK);
    }

    public SimpleCache(String name, ReferenceMap.Type type) {

        this(name, ZERO, ZERO, type);
    }

    public SimpleCache(String name, long timeToLive, long timeToIdle) {

        this(name, timeToLive, timeToIdle, ReferenceMap.Type.WEAK);
    }

    public SimpleCache(String name, long timeToLive, long timeToIdle, ReferenceMap.Type type) {
        super(
                name, ZERO, timeToLive, timeToIdle,
                new ReferenceMap<Object, ValueWrapper>(
                        new ConcurrentHashMap<Object, ReferenceMap.ValueCell<Object, ValueWrapper>>(),
                        type
                )
        );
    }

}

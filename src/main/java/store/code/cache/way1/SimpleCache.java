package store.code.cache.way1;

import artoria.collect.ReferenceMap;
import artoria.lang.ReferenceType;

import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.ZERO;

/**
 * Memory cache simple implement by jdk.
 * @author Kahle
 */
public class SimpleCache extends AbstractCache {

    public SimpleCache(String name) {

        this(name, ReferenceType.WEAK);
    }

    public SimpleCache(String name, ReferenceType type) {

        this(name, ZERO, ZERO, type);
    }

    public SimpleCache(String name, long timeToLive, long timeToIdle) {

        this(name, timeToLive, timeToIdle, ReferenceType.WEAK);
    }

    public SimpleCache(String name, long timeToLive, long timeToIdle, ReferenceType type) {
        super(
                name, ZERO, timeToLive, timeToIdle,
                new ReferenceMap<Object, ValueWrapper>(type,
                    new ConcurrentHashMap<Object, ReferenceMap.ValueCell<Object, ValueWrapper>>())
        );
    }

}

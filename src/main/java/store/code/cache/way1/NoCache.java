package store.code.cache.way1;

import artoria.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;

import static artoria.common.Constants.ZERO;

/**
 * No cache.
 * @author Kahle
 */
public class NoCache implements Cache {
    private final String name;

    public NoCache(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        this.name = name;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Object getNativeCache() {

        return null;
    }

    @Override
    public int size() {

        return ZERO;
    }

    @Override
    public boolean containsKey(Object key) {

        return false;
    }

    @Override
    public Object get(Object key) {

        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {

        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {

        return null;
    }

    @Override
    public Object put(Object key, Object value) {

        return null;
    }

    @Override
    public Object put(Object key, Object value, Long timeToLive, Long timeToIdle) {

        return null;
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {

        return null;
    }

    @Override
    public void putAll(Map<?, ?> map) {

    }

    @Override
    public Object remove(Object key) {

        return null;
    }

    @Override
    public void removeAll(Collection<?> keys) {

    }

    @Override
    public int prune() {

        return ZERO;
    }

    @Override
    public void clear() {

    }

    @Override
    public Collection<Object> keys() {

        return null;
    }

    @Override
    public Map<Object, Object> entries() {

        return null;
    }

    @Override
    public void destroy() throws Exception {

    }

}

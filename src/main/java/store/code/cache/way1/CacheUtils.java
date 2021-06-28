package store.code.cache.way1;

import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.*;
import static artoria.lang.ReferenceType.SOFT;

/**
 * Cache tools.
 * @author Kahle
 */
public class CacheUtils {
    private static final Map<String, Cache> CACHE_MAP = new ConcurrentHashMap<String, Cache>();
    private static Logger log = LoggerFactory.getLogger(CacheUtils.class);

    static {
        long timeToLive = TWO * SIXTY * ONE_THOUSAND;
        SimpleCache cache = new SimpleCache(DEFAULT, timeToLive, ZERO, SOFT);
        cache.setPrintLog(true);
        CacheUtils.register(cache);
    }

    public static void register(Cache cache) {
        Assert.notNull(cache, "Parameter \"cache\" must not null. ");
        String cacheName = cache.getName();
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        String cacheClassName = cache.getClass().getName();
        log.info("Register \"{}\" to \"{}\". ", cacheClassName, cacheName);
        CACHE_MAP.put(cacheName, cache);
    }

    public static Cache unregister(String cacheName) {
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        Cache remove = CACHE_MAP.remove(cacheName);
        if (remove != null) {
            String removeClassName = remove.getClass().getName();
            log.info("Unregister \"{}\" to \"{}\". ", removeClassName, cacheName);
        }
        return remove;
    }

    public static Cache getCache(String cacheName) {
        Assert.notBlank(cacheName, "Parameter \"cacheName\" must not blank. ");
        Cache cache = CACHE_MAP.get(cacheName);
        Assert.notNull(cache, "The cache does not exist. Please register first. ");
        return cache;
    }

    public static int size(String cacheName) {
        Cache cache = getCache(cacheName);
        return cache.size();
    }

    public static boolean containsKey(String cacheName, Object key) {
        Cache cache = getCache(cacheName);
        return cache.containsKey(key);
    }

    public static Object get(String cacheName, Object key) {
        Cache cache = getCache(cacheName);
        return cache.get(key);
    }

    public static <T> T get(String cacheName, Object key, Callable<T> callable) {
        Cache cache = getCache(cacheName);
        return cache.get(key, callable);
    }

    public static <T> T get(String cacheName, Object key, Class<T> type) {
        Cache cache = getCache(cacheName);
        return cache.get(key, type);
    }

    public static Object put(String cacheName, Object key, Object value) {
        Cache cache = getCache(cacheName);
        return cache.put(key, value);
    }

    public static Object put(String cacheName, Object key, Object value, Long timeToLive, Long timeToIdle) {
        Cache cache = getCache(cacheName);
        return cache.put(key, value, timeToLive, timeToIdle);
    }

    public static Object putIfAbsent(String cacheName, Object key, Object value) {
        Cache cache = getCache(cacheName);
        return cache.putIfAbsent(key, value);
    }

    public static void putAll(String cacheName, Map<?, ?> map) {
        Cache cache = getCache(cacheName);
        cache.putAll(map);
    }

    public static Object remove(String cacheName, Object key) {
        Cache cache = getCache(cacheName);
        return cache.remove(key);
    }

    public static void removeAll(String cacheName, Collection<?> keys) {
        Cache cache = getCache(cacheName);
        cache.removeAll(keys);
    }

    public static int prune(String cacheName) {
        Cache cache = getCache(cacheName);
        return cache.prune();
    }

    public static void clear(String cacheName) {
        Cache cache = getCache(cacheName);
        cache.clear();
    }

    public static Collection<Object> keys(String cacheName) {
        Cache cache = getCache(cacheName);
        return cache.keys();
    }

    public static Map<Object, Object> entries(String cacheName) {
        Cache cache = getCache(cacheName);
        return cache.entries();
    }

    public static void destroy(String cacheName) {
        Cache cache = getCache(cacheName);
        CACHE_MAP.remove(cacheName);
        try {
            cache.destroy();
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

    public static void destroyAll() {
        if (MapUtils.isEmpty(CACHE_MAP)) { return; }
        List<String> nameList = new ArrayList<String>(CACHE_MAP.keySet());
        for (String name : nameList) {
            if (StringUtils.isBlank(name)) { continue; }
            Cache cache = CACHE_MAP.get(name);
            CACHE_MAP.remove(name);
            if (cache == null) { continue; }
            try {
                cache.destroy();
            }
            catch (Exception e) {
                throw ExceptionUtils.wrap(e);
            }
        }
    }

}

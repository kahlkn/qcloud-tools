package store.code.cache.way1;

import artoria.convert.type.TypeConvertUtils;
import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.MapUtils;
import artoria.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import static artoria.common.Constants.ZERO;

/**
 * Abstract memory cache implementation.
 * @author Kahle
 */
public abstract class AbstractCache implements Cache {
    /**
     * The log object.
     */
    private static Logger log = LoggerFactory.getLogger(artoria.cache.AbstractCache.class);
    /**
     * The number of times the cache was missed.
     */
    private final AtomicLong missCount = new AtomicLong();
    /**
     * The number of times the cache was hit.
     */
    private final AtomicLong hitCount = new AtomicLong();
    /**
     * Cached storage object.
     */
    private final Map<Object, ValueWrapper> storage;
    /**
     * Cache name.
     */
    private final String name;
    /**
     * Cache capacity. 0 indicates unlimited.
     */
    private final Long capacity;
    /**
     * The amount of time for the element to idle, in millisecond. 0 indicates unlimited.
     */
    private final Long timeToIdle;
    /**
     * The amount of time for the element to live, in millisecond. 0 indicates unlimited.
     */
    private final Long timeToLive;
    /**
     * Print log.
     */
    private Boolean printLog;

    public AbstractCache(String name, long capacity, long timeToLive, long timeToIdle, Map<Object, ValueWrapper> storage) {
        Assert.notNull(storage, "Parameter \"storage\" must not null. ");
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        timeToLive = timeToLive < ZERO ? ZERO : timeToLive;
        timeToIdle = timeToIdle < ZERO ? ZERO : timeToIdle;
        capacity = capacity < ZERO ? ZERO : capacity;
        this.storage = storage;
        this.name = name;
        this.capacity = capacity;
        this.timeToIdle = timeToIdle;
        this.timeToLive = timeToLive;
        this.printLog = false;
    }

    protected Map<Object, ValueWrapper> getStorage() {

        return storage;
    }

    protected boolean isFull() {

        return capacity > ZERO && size() > capacity;
    }

    protected void updateHitStatistic(boolean hit) {
        if (printLog) {
            log.info("The size of the cache named \"{}\" is \"{}\"" +
                    " and its hit counts are \"{}\" and its miss counts are \"{}\". ", name, size(), hitCount, missCount);
        }
        (hit ? hitCount : missCount).incrementAndGet();
    }

    public boolean getPrintLog() {

        return printLog;
    }

    public void setPrintLog(boolean printLog) {

        this.printLog = printLog;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Object getNativeCache() {

        return storage;
    }

    @Override
    public int size() {

        return storage.size();
    }

    @Override
    public boolean containsKey(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        boolean containsKey = storage.containsKey(key);
        updateHitStatistic(containsKey);
        return containsKey;
    }

    @Override
    public Object get(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper valueWrapper = storage.get(key);
        boolean notNull = valueWrapper != null;
        if (valueWrapper != null
                && valueWrapper.isExpired()) {
            storage.remove(key);
            notNull = false;
        }
        updateHitStatistic(notNull);
        return notNull ? valueWrapper.getValue() : null;
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        Assert.notNull(callable, "Parameter \"callable\" must not null. ");
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Object object = get(key);
        if (object != null) {
            return ObjectUtils.cast(object);
        }
        try {
            object = callable.call();
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
        if (object != null) {
            put(key, object);
        }
        return ObjectUtils.cast(object);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Assert.notNull(type, "Parameter \"type\" must not null. ");
        Object object = get(key);
        if (object == null) { return null; }
        object = TypeConvertUtils.convert(object, type);
        return ObjectUtils.cast(object, type);
    }

    @Override
    public Object put(Object key, Object value) {

        return put(key, value, timeToLive, timeToIdle);
    }

    @Override
    public Object put(Object key, Object value, Long timeToLive, Long timeToIdle) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Assert.notNull(value, "Parameter \"value\" must not null. ");
        ValueWrapper valueWrapper = storage.get(key);
        Object preValue = null;
        if (valueWrapper != null
                && valueWrapper.isExpired()) {
            storage.remove(key);
            preValue = valueWrapper.getValue();
            valueWrapper = null;
        }
        if (valueWrapper != null) {
            valueWrapper.setValue(value);
        }
        else {
            if (isFull()) { prune(); }
            if (timeToLive == null) { timeToLive = this.timeToLive; }
            if (timeToIdle == null) { timeToIdle = this.timeToIdle; }
            timeToLive = timeToLive < ZERO ? ZERO : timeToLive;
            timeToIdle = timeToIdle < ZERO ? ZERO : timeToIdle;
            valueWrapper = new ValueWrapper(key, value, timeToLive, timeToIdle);
            storage.put(key, valueWrapper);
        }
        return preValue;
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<?, ?> map) {
        Assert.notNull(map, "Parameter \"map\" must not null. ");
        if (MapUtils.isEmpty(map)) { return; }
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Object remove(Object key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        ValueWrapper remove = storage.remove(key);
        return remove != null ? remove.getValue() : null;
    }

    @Override
    public void removeAll(Collection<?> keys) {
        if (CollectionUtils.isEmpty(keys)) { return; }
        for (Object key : keys) {
            if (key == null) { continue; }
            storage.remove(key);
        }
    }

    @Override
    public int prune() {
        if (MapUtils.isEmpty(storage)) { return ZERO; }
        List<Object> deleteList = new ArrayList<Object>();
        for (Map.Entry<Object, ValueWrapper> entry : storage.entrySet()) {
            ValueWrapper valueWrapper = entry.getValue();
            if (valueWrapper == null) { continue; }
            Object key = entry.getKey();
            if (valueWrapper.isExpired()) {
                deleteList.add(key);
            }
        }
        if (CollectionUtils.isNotEmpty(deleteList)) {
            for (Object key : deleteList) {
                if (key == null) { continue; }
                storage.remove(key);
            }
        }
        return deleteList.size();
    }

    @Override
    public void clear() {
        storage.clear();
        hitCount.set(ZERO);
        missCount.set(ZERO);
    }

    @Override
    public Collection<Object> keys() {

        return storage.keySet();
    }

    @Override
    public Map<Object, Object> entries() {
        Map<Object, Object> result = new HashMap<Object, Object>(storage.size());
        if (MapUtils.isEmpty(storage)) {
            return Collections.unmodifiableMap(result);
        }
        for (Map.Entry<Object, ValueWrapper> entry : storage.entrySet()) {
            ValueWrapper val = entry.getValue();
            Object key = entry.getKey();
            if (key == null || val == null) { continue; }
            result.put(key, val.getValue());
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public void destroy() throws Exception {

        clear();
    }

    protected static class ValueWrapper {
        /**
         * The cache key.
         */
        private final Object key;
        /**
         * The value.
         */
        private Object value;
        /**
         * The amount of time for the element to live, in millisecond. 0 indicates unlimited.
         */
        private final long timeToLive;
        /**
         * The amount of time for the element to idle, in millisecond. 0 indicates unlimited.
         */
        private final long timeToIdle;
        /**
         * The create time.
         */
        private final long createTime;
        /**
         * The last update time.
         */
        private volatile long lastUpdateTime;
        /**
         * The last access time.
         */
        private volatile long lastAccessTime;
        /**
         * The number of times the entry was updated.
         */
        private final AtomicLong updateCount = new AtomicLong();
        /**
         * The number of times the entry was accessed.
         */
        private final AtomicLong accessCount = new AtomicLong();

        public ValueWrapper(Object key, Object value, Long timeToLive, Long timeToIdle) {
            Assert.isTrue(timeToLive >= ZERO, "Parameter \"timeToLive\" must >= 0. ");
            Assert.isTrue(timeToIdle >= ZERO, "Parameter \"timeToIdle\" must >= 0. ");
            Assert.notNull(value, "Parameter \"value\" must not null. ");
            Assert.notNull(key, "Parameter \"key\" must not null. ");
            long currentTimeMillis = System.currentTimeMillis();
            this.lastAccessTime = currentTimeMillis;
            this.lastUpdateTime = currentTimeMillis;
            this.createTime = currentTimeMillis;
            this.timeToLive = timeToLive;
            this.timeToIdle = timeToIdle;
            this.value = value;
            this.key = key;
        }

        public Object getKey() {

            return key;
        }

        public Object getValue() {
            updateAccessStatistic();
            return value;
        }

        public void setValue(Object value) {
            updateUpdateStatistic();
            this.value = value;
        }

        public long getTimeToLive() {

            return timeToLive;
        }

        public long getTimeToIdle() {

            return timeToIdle;
        }

        public long getCreateTime() {

            return createTime;
        }

        public long getLastUpdateTime() {

            return lastUpdateTime;
        }

        public long getLastAccessTime() {

            return lastAccessTime;
        }

        public long getUpdateCount() {

            return updateCount.get();
        }

        public long getAccessCount() {

            return accessCount.get();
        }

        public boolean isExpired() {
            if (timeToLive <= ZERO && timeToIdle <= ZERO) { return false; }
            long currentTimeMillis = System.currentTimeMillis();
            if (timeToLive > ZERO &&
                    (currentTimeMillis - createTime) > timeToLive) {
                return true;
            }
            return timeToIdle > ZERO &&
                    (currentTimeMillis - lastAccessTime) > timeToIdle;
        }

        public void resetAccessStatistics() {
            lastAccessTime = System.currentTimeMillis();
            accessCount.set(ZERO);
        }

        public void updateAccessStatistic() {
            lastAccessTime = System.currentTimeMillis();
            accessCount.incrementAndGet();
        }

        public void updateUpdateStatistic() {
            lastUpdateTime = System.currentTimeMillis();
            updateCount.incrementAndGet();
        }

    }

}

package store.code.fake.way1;

import artoria.exception.ExceptionUtils;
import artoria.reflect.ReflectUtils;
import artoria.util.*;
import store.code.convert.type.way1.TypeConvertUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.*;

public class SimpleFaker implements Faker {
    private static final Integer DEFAULT_BOUND = 8192;
    private static final Integer DEFAULT_SIZE = 8;

    void verifyUnsupportedClass(Class<?> clazz) {
        boolean isUnsupported = clazz.isArray()
                || List.class.isAssignableFrom(clazz)
                || Map.class.isAssignableFrom(clazz);
        if (isUnsupported) {
            throw new UnsupportedOperationException("\"List\", \"Map\" and array is unsupported. ");
        }
    }

    protected Method[] findWriteMethods(Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        PropertyDescriptor[] descriptors = ReflectUtils.getPropertyDescriptors(clazz);
        Map<String, Method> methodMap = new HashMap<String, Method>(descriptors.length);
        for (PropertyDescriptor descriptor : descriptors) {
            Method writeMethod = descriptor.getWriteMethod();
            if (writeMethod != null) { methodMap.put(descriptor.getName(), writeMethod); }
        }
        // 因为之前有个可以单纯获取 setter 方法数组的方法，现在为了偷懒直接将某段方法复制过来改改了
        // toArray：如果数组足够大，则存储列表元素的数组;否则，将为此目的分配相同运行时类型的新数组。
        return methodMap.values().toArray(new Method[]{});
    }

    <T> T fakeBeanByClass(Class<T> clazz) throws Exception {
        Method[] writeMethods = findWriteMethods(clazz);
        Map<String, Method> methodMap = new HashMap<String, Method>(writeMethods.length);
        for (Method writeMethod : writeMethods) {
            String attrName = writeMethod.getName().substring(THREE);
            methodMap.put(StringUtils.uncapitalize(attrName), writeMethod);
        }
        T bean = ReflectUtils.newInstance(clazz);
        for (Map.Entry<String, Method> entry : methodMap.entrySet()) {
            String attrName = entry.getKey();
            Method method = entry.getValue();
            Type type = method.getGenericParameterTypes()[ZERO];
            Object val;
            if (type instanceof ParameterizedType) {
                ParameterizedType realType = (ParameterizedType) type;
                Class<?> rawType = (Class<?>) realType.getRawType();
                if (clazz.isAssignableFrom(rawType)) { continue; }
                //Type[] args = realType.getActualTypeArguments();
                //boolean hasArgs = args != null;
                boolean isMap = Map.class.isAssignableFrom(rawType);
                //isMap = isMap && hasArgs && args.length >= TWO;
                boolean isList = List.class.isAssignableFrom(rawType);
                //isList = isList && hasArgs && args.length >= ONE;
                boolean isArr = rawType.isArray();
                val = isMap || isList || isArr ? null : fake(attrName, rawType);
            }
            else {
                if (clazz.isAssignableFrom((Class<?>) type)) { continue; }
                val = fake(attrName, (Class<?>) type);
            }
            if (val != null) { method.invoke(bean, val); }
        }
        return bean;
    }

    @Override
    public int can(String express, Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        return FIFTY;
    }

    @Override
    public <T> T fake(String express, Class<T> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?> wrapper = ClassUtils.getWrapper(clazz);
        verifyUnsupportedClass(wrapper);
        if (Number.class.isAssignableFrom(wrapper)) {
            double nextDouble = RandomUtils.nextDouble() * DEFAULT_BOUND;
            nextDouble = NumberUtils.round(nextDouble).doubleValue();
            Object convert = TypeConvertUtils.convert(nextDouble, wrapper);
            return ObjectUtils.cast(convert, clazz);
        }
        if (Boolean.class.isAssignableFrom(wrapper)) {
            boolean nextBoolean = RandomUtils.nextBoolean();
            return ObjectUtils.cast(nextBoolean, clazz);
        }
        if (Character.class.isAssignableFrom(wrapper)) {
            Character nextCharacter = RandomUtils.nextCharacter();
            return ObjectUtils.cast(nextCharacter, clazz);
        }
        if (Date.class.isAssignableFrom(wrapper)) {
            Date nowDate = new Date();
            Object convert = TypeConvertUtils.convert(nowDate, wrapper);
            return ObjectUtils.cast(convert, clazz);
        }
        if (String.class.isAssignableFrom(wrapper)) {
            int size = RandomUtils.nextInt(DEFAULT_SIZE);
            String nextString = RandomUtils.nextString(++size);
            return ObjectUtils.cast(nextString, clazz);
        }
        if (Object.class.equals(wrapper)) {
            Object object = new Object();
            return ObjectUtils.cast(object, clazz);
        }
        try {
            return fakeBeanByClass(clazz);
        }
        catch (Exception e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}

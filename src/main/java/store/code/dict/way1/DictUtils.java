package store.code.dict.way1;

import artoria.convert.type.TypeConvertUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.*;

public class DictUtils {
    private static final DictProvider DEFAULT_DICT_PROVIDER = new SimpleDictProvider();
    private static Logger log = LoggerFactory.getLogger(DictUtils.class);
    private static DictProvider dictProvider;

    public static DictProvider getDictProvider() {

        return dictProvider != null ? dictProvider : DEFAULT_DICT_PROVIDER;
    }

    public static void setDictProvider(DictProvider dictProvider) {
        Assert.notNull(dictProvider, "Parameter \"dictProvider\" must not null. ");
        log.info("Set dict provider: {}", dictProvider.getClass().getName());
        DictUtils.dictProvider = dictProvider;
    }



    public static Map<String, Object> getProperties(String group) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        List<Dict> list = findList(new Dict(group, NULL_STR, NULL_OBJ));
        if (CollectionUtils.isEmpty(list)) { return result; }
        for (Dict dict : list) {
            if (dict == null) { continue; }
            Object value = dict.getValue();
            String name = dict.getName();
            result.put(name, value);
        }
        return result;
    }



    public static String getStringValue(String name) {

        return getStringValue(name, NULL_STR);
    }

    public static String getStringValue(String name, String defaultValue) {

        return getStringValue(NULL_STR, name, defaultValue);
    }

    public static String getStringValue(String group, String name, String defaultValue) {

        return getValue(group, name, String.class, defaultValue);
    }

    public static boolean getBooleanValue(String name) {

        return getBooleanValue(name, Boolean.FALSE);
    }

    public static boolean getBooleanValue(String name, boolean defaultValue) {

        return getBooleanValue(NULL_STR, name, defaultValue);
    }

    public static boolean getBooleanValue(String group, String name, boolean defaultValue) {
        Boolean value = getValue(group, name, Boolean.class, defaultValue);
        return value != null ? value : defaultValue;
    }

    public static int getIntegerValue(String name) {

        return getIntegerValue(name, ZERO);
    }

    public static int getIntegerValue(String name, int defaultValue) {

        return getIntegerValue(NULL_STR, name, defaultValue);
    }

    public static int getIntegerValue(String group, String name, int defaultValue) {
        Integer value = getValue(group, name, Integer.class, defaultValue);
        return value != null ? value : defaultValue;
    }

    public static <T> T getRequiredValue(String name, Class<T> targetType) {

        return getRequiredValue(NULL_STR, name, targetType);
    }

    public static <T> T getRequiredValue(String group, String name, Class<T> targetType) {
        T value = getValue(group, name, targetType, (T) null);
        if (value == null) { throw new DictException("The dict value is null. "); }
        return value;
    }

    public static <T> T getValue(String name, Class<T> targetType, T defaultValue) {

        return getValue(NULL_STR, name, targetType, defaultValue);
    }

    public static <T> T getValue(String group, String name, Class<T> targetType, T defaultValue) {
        Object value = getValue(group, name, defaultValue);
        if (value == null) { return null; }
        value = TypeConvertUtils.convert(value, targetType);
        return ObjectUtils.cast(value, targetType);
    }



    public static Object setValue(String name, Object value) {

        return setValue(NULL_STR, name, value);
    }

    public static Object setValue(String group, String name, Object value) {
        Object oldVal = getValue(group, name, NULL_OBJ);
        Dict dict = new Dict(group, name, value);
        if (oldVal != null) { edit(dict); }
        else { add(dict); }
        return oldVal;
    }

    public static Object getValue(String name, Object defaultValue) {

        return getValue(NULL_STR, name, defaultValue);
    }

    public static Object getValue(String group, String name, Object defaultValue) {
        Dict dict = find(new Dict(group, name, NULL_OBJ));
        if (dict == null) { return defaultValue; }
        Object dictValue = dict.getValue();
        return dictValue != null ? dictValue : defaultValue;
    }

    public static void delete(String name) {

        delete(NULL_STR, name);
    }

    public static void delete(String group, String name) {

        delete(new Dict(group, name, NULL_OBJ));
    }

    public static boolean exist(String name) {

        return exist(NULL_STR, name);
    }

    public static boolean exist(String group, String name) {

        return exist(new Dict(group, name, NULL_OBJ));
    }

    public static void add(Dict dict) {

        getDictProvider().add(dict);
    }

    public static void edit(Dict dict) {

        getDictProvider().edit(dict);
    }

    public static void delete(Dict dict) {

        getDictProvider().delete(dict);
    }

    public static boolean exist(Dict dict) {

        return getDictProvider().exist(dict);
    }

    public static Dict find(Dict dict) {

        return getDictProvider().find(dict);
    }

    public static List<Dict> findList(Dict dict) {

        return getDictProvider().findList(dict);
    }

}

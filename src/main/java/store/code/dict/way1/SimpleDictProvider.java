package store.code.dict.way1;

import artoria.util.MapUtils;
import artoria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static artoria.common.Constants.DEFAULT;
import static artoria.common.Constants.TWENTY;

public class SimpleDictProvider implements DictProvider {
    private static final Map<String, Map<String, Object>> DICT_MAP = new ConcurrentHashMap<String, Map<String, Object>>();
    private static Logger log = LoggerFactory.getLogger(SimpleDictProvider.class);

    @Override
    public void add(Dict dict) {
        String group = dict.getGroup();
        if (StringUtils.isBlank(group)) {
            group = DEFAULT;
        }
        String name = dict.getName();
        Object value = dict.getValue();
        Map<String, Object> map = DICT_MAP.get(group);
        if (map == null) {
            map = new ConcurrentHashMap<String, Object>(TWENTY);
            DICT_MAP.put(group, map);
        }
        Object oldVal = map.get(name);
        if (oldVal != null) {
            if (oldVal.equals(value)) { return; }
            throw new DictException("Value exist! ");
        }
        map.put(name, value);
    }

    @Override
    public void edit(Dict dict) {
        String group = dict.getGroup();
        if (StringUtils.isBlank(group)) {
            group = DEFAULT;
        }
        String name = dict.getName();
        Object value = dict.getValue();
        Map<String, Object> map = DICT_MAP.get(group);
        if (map == null) {
            throw new DictException("Dict not exist! ");
        }
        map.put(name, value);
    }

    @Override
    public void delete(Dict dict) {
        String group = dict.getGroup();
        if (StringUtils.isBlank(group)) {
            group = DEFAULT;
        }
        String name = dict.getName();
        Map<String, Object> map = DICT_MAP.get(group);
        if (map == null) { return; }
        map.remove(name);
        if (MapUtils.isEmpty(map)) {
            DICT_MAP.remove(group);
        }
    }

    @Override
    public boolean exist(Dict dict) {
        String group = dict.getGroup();
        if (StringUtils.isBlank(group)) {
            group = DEFAULT;
        }
        String name = dict.getName();
        Map<String, Object> map = DICT_MAP.get(group);
        return map != null && map.containsKey(name);
    }

    @Override
    public Dict find(Dict dict) {
        String group = dict.getGroup();
        if (StringUtils.isBlank(group)) {
            group = DEFAULT;
        }
        String name = dict.getName();
        Object value = dict.getValue();
        Map<String, Object> map = DICT_MAP.get(group);
        if (map == null) {
            return null;
        }
        Object dictValue = map.get(name);
        if (value != null && !value.equals(dictValue)) {
            return null;
        }
        return new Dict(group, name, dictValue);
    }

    @Override
    public List<Dict> findList(Dict dict) {
        String group = dict.getGroup();
        if (StringUtils.isBlank(group)) {
            group = DEFAULT;
        }
        String name = dict.getName();
        Object value = dict.getValue();
        List<Dict> result = new ArrayList<Dict>();
        Map<String, Object> map = DICT_MAP.get(group);
        if (map == null) {
            return result;
        }
        if (StringUtils.isNotBlank(name)) {
            Object dictValue = map.get(name);
            if (value != null && !value.equals(dictValue)) {
                return result;
            }
            result.add(new Dict(group, name, dictValue));
            return result;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object entryVal = entry.getValue();
            String entryKey = entry.getKey();
            result.add(new Dict(group, entryKey, entryVal));
        }
        return result;
    }

}

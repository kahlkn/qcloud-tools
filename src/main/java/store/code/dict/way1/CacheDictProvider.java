package store.code.dict.way1;

import artoria.cache.Cache;

import java.util.List;

public class CacheDictProvider implements DictProvider {
    private DictProvider dictProvider;
    private Cache cache;

    @Override
    public void add(Dict dict) {
//        cache = new SimpleCache();

    }

    @Override
    public void edit(Dict dict) {

    }

    @Override
    public void delete(Dict dict) {

    }

    @Override
    public boolean exist(Dict dict) {
        return false;
    }

    @Override
    public Dict find(Dict dict) {
        return null;
    }

    @Override
    public List<Dict> findList(Dict dict) {
        return null;
    }

}

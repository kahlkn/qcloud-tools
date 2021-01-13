package store.code.dict.way1;

import java.util.List;

public interface DictProvider {

    /**
     * Add dict.
     * @param dict Add parameters
     */
    void add(Dict dict);

    /**
     * Edit dict.
     * @param dict Edit parameters
     */
    void edit(Dict dict);

    /**
     * Delete dict.
     * @param dict Delete parameters
     */
    void delete(Dict dict);

    /**
     * Exist dict.
     * @param dict Query parameters
     */
    boolean exist(Dict dict);

    /**
     * Find dict.
     * @param dict Query parameters
     */
    Dict find(Dict dict);

    /**
     * Find dict list.
     * @param dict Query parameters
     */
    List<Dict> findList(Dict dict);

}

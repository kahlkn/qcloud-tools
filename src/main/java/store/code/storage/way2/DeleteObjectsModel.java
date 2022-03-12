package store.code.storage.way2;

import java.util.List;

public class DeleteObjectsModel extends ObjectModel {
    /**
     * List of keys to delete.
     */
    private List<String> objectKeys;

    public List<String> getObjectKeys() {

        return objectKeys;
    }

    public void setObjectKeys(List<String> objectKeys) {

        this.objectKeys = objectKeys;
    }

}

package store.code.storage.way2;

import java.util.List;

public class DeleteObjectsResult extends ObjectResult {
    /**
     * Successfully deleted objects.
     */
    private List<String> deletedObjectKeys;

    public List<String> getDeletedObjectKeys() {

        return deletedObjectKeys;
    }

    public void setDeletedObjectKeys(List<String> deletedObjectKeys) {

        this.deletedObjectKeys = deletedObjectKeys;
    }

}

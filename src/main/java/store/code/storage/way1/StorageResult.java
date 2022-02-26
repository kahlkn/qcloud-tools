package store.code.storage.way1;

import artoria.data.AbstractExtraData;

import java.io.Serializable;

/**
 * The stored result of the object.
 * @author Kahle
 */
public class StorageResult extends AbstractExtraData implements Serializable {
    /**
     * Business id.
     */
    private String businessId;

    public String getBusinessId() {

        return businessId;
    }

    public void setBusinessId(String businessId) {

        this.businessId = businessId;
    }

}

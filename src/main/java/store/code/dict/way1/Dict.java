package store.code.dict.way1;

import artoria.data.AbstractExtraData;

import java.io.Serializable;

public class Dict extends AbstractExtraData implements Serializable {
    /**
     * The group of entries in the dictionary.
     */
    private String group;
    /**
     * The name of the entry in the dictionary.
     */
    private String name;
    /**
     * The value of the entry in the dictionary.
     */
    private Object value;

    public Dict() {

    }

    public Dict(String group, String name, Object value) {
        this.group = group;
        this.name = name;
        this.value = value;
    }

    public String getGroup() {

        return group;
    }

    public void setGroup(String group) {

        this.group = group;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Object getValue() {

        return value;
    }

    public void setValue(Object value) {

        this.value = value;
    }

}

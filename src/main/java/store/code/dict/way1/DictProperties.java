package store.code.dict.way1;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "artoria.dict")
public class DictProperties {
    private Class<? extends DictProvider> springBeanType;
    private String springBeanName;

    public Class<? extends DictProvider> getSpringBeanType() {
        return springBeanType;
    }

    public void setSpringBeanType(Class<? extends DictProvider> springBeanType) {
        this.springBeanType = springBeanType;
    }

    public String getSpringBeanName() {
        return springBeanName;
    }

    public void setSpringBeanName(String springBeanName) {
        this.springBeanName = springBeanName;
    }

}

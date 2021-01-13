package store.code.dict.way1;

import artoria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties({DictProperties.class})
public class DictAutoConfiguration {
    private static Logger log = LoggerFactory.getLogger(DictAutoConfiguration.class);

    @Autowired
    public DictAutoConfiguration(ApplicationContext appContext, DictProperties dictProperties) {
        Class<? extends DictProvider> springBeanType = dictProperties.getSpringBeanType();
        String springBeanName = dictProperties.getSpringBeanName();
        DictProvider dictProvider = null;
        if (springBeanType != null || StringUtils.isNotBlank(springBeanName)) {
            if (springBeanType != null) {
                dictProvider = appContext.getBean(springBeanType);
            }
            else if (StringUtils.isNotBlank(springBeanName)) {
                dictProvider = (DictProvider) appContext.getBean(springBeanName);
            }
            // else {}
        }
        if (dictProvider != null) {
            Properties properties = new Properties();
//            properties.getProperty()
            DictUtils.setDictProvider(dictProvider);
        }
    }

}

package store.code.storage.way1.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import store.code.storage.way1.ObjectStorageProvider;

/**
 * Alibaba cloud oss auto configuration.
 * @author Kahle
 */
@Configuration
@ConditionalOnProperty(name = "misaka.alibaba.cloud.oss.enabled", havingValue = "true")
@EnableConfigurationProperties({OssProperties.class})
public class OssAutoConfiguration implements DisposableBean {
    private static Logger log = LoggerFactory.getLogger(OssAutoConfiguration.class);
    private final OssProperties ossProperties;
    private ObjectStorageProvider objectStorageProvider;

    @Autowired
    public OssAutoConfiguration(OssProperties ossProperties) {

        this.ossProperties = ossProperties;
    }

    @Bean
    public ObjectStorageProvider objectStorageProvider() {
        String accessKeySecret = ossProperties.getAccessKeySecret();
        String accessKeyId = ossProperties.getAccessKeyId();
        String endpoint = ossProperties.getEndpoint();
        objectStorageProvider = new OssProviderImpl(accessKeyId, accessKeySecret, endpoint);
        log.info("Alibaba cloud object storage (OSS) provider was initialized success. ");
        return objectStorageProvider;
    }

    @Override
    public void destroy() throws Exception {
        if (objectStorageProvider != null) {
            objectStorageProvider.destroy();
        }
    }

}

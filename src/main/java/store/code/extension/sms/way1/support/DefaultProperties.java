package store.code.extension.sms.way1.support;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Alibaba cloud default properties.
 * @author Kahle
 */
@ConfigurationProperties("cloud.alibaba.default")
public class DefaultProperties {
    private String accessKeyId;
    private String accessKeySecret;

    public String getAccessKeyId() {

        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {

        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {

        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {

        this.accessKeySecret = accessKeySecret;
    }

}

package store.code.spring.config.zookeeper;

import artoria.util.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.io.Closeable;

/**
 * Zookeeper auto configuration.
 * @author Kahle
 */
@Configuration
@ConditionalOnClass({CuratorFramework.class})
@ConditionalOnProperty(name = "artoria.lock.zookeeper", havingValue = "true")
public class ZookeeperCuratorAutoConfiguration implements InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(ZookeeperCuratorAutoConfiguration.class);
    private static final String ZK_URL_PREFIX = "zookeeper://";
    private Closeable curator;

    @Value("${zookeeper.url:null}")
    private String zkUrl = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isNotBlank(this.zkUrl)) {
            log.info("Find zookeeper url \"{}\". ", this.zkUrl);
            String url = this.zkUrl;
            if (url.startsWith(ZK_URL_PREFIX)) {
                url = url.substring(ZK_URL_PREFIX.length());
            }
            try {
                this.curator = CuratorFrameworkFactory.builder()
                        .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
                        .sessionTimeoutMs(30000)
                        .connectionTimeoutMs(30000)
                        .connectString(url)
                        .build();
            }
            catch (Exception e) {
                log.error(e.getMessage(), e);
            }
//            LockManager locker = new ZkReentrantLocker((CuratorFramework) this.curator);
//            LockUtils.setLocker(locker);
        }
        else {
            log.info("Can not find zookeeper url by key \"zookeeper.url\". ");
        }
    }

    @Override
    public void destroy() throws Exception {
        if (this.curator != null) {
            this.curator.close();
            log.info("Release curator object \"{}\" success. ", this.curator);
        }
    }

}

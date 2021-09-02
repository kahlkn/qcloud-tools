package store.code.feign.way1;

import feign.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

import static artoria.common.Constants.EMPTY_STRING;
import static artoria.common.Constants.QUESTION_MARK;
import static artoria.util.ObjectUtils.cast;
import static artoria.util.StringUtils.isNotBlank;

/**
 * The configurable feign client.
 * @author Kahle
 */
public class ConfigurableFeignClient extends LoadBalancerFeignClient {
    private static Logger log = LoggerFactory.getLogger(ConfigurableFeignClient.class);
    private final Map<String, String> configInfo;
    private final Client delegate;

    public ConfigurableFeignClient(Client delegate,
                                   CachingSpringLoadBalancerFactory lbClientFactory,
                                   SpringClientFactory clientFactory,
                                   Map<String, String> configInfo) {
        super(delegate, lbClientFactory, clientFactory);
        if (configInfo == null) { configInfo = Collections.emptyMap(); }
        this.configInfo = configInfo;
        this.delegate = delegate;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        // Variable definition.
        URI nowUri = URI.create(request.url());
        String host = nowUri.getHost();
        String path = nowUri.getPath();
        String query = nowUri.getQuery();
        String target;
        query = isNotBlank(query)
                ? QUESTION_MARK + query : EMPTY_STRING;
        // Check whether configuration information exists.
        boolean existConfig = isNotBlank(target = configInfo.get(host + path))
                || isNotBlank(target = configInfo.get(host));
        // Go to the address information in the configuration.
        if (existConfig) {
            String url = target + path + query;
            // Modifying URL Information.
            RequestTemplate requestTemplate = request.requestTemplate();
            requestTemplate.target(target);
            Target<?> feignTarget = requestTemplate.feignTarget();
            Class<Object> targetType = cast(feignTarget.type());
            feignTarget = new Target.HardCodedTarget<Object>(targetType, feignTarget.name(), target);
            requestTemplate.feignTarget(feignTarget);
            // Rebuild the request object.
            request = Request.create(request.httpMethod(), url,
                request.headers(), request.body(), request.charset(), request.requestTemplate());
            log.info("Executing {} {}", request.httpMethod(), request.url());
            return delegate.execute(request, options);
        }
        return super.execute(request, options);
    }

}

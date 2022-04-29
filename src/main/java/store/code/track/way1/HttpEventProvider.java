//package store.code.track.way1;
//
//import artoria.event.SimpleEventProvider;
//import artoria.message.MessageUtils;
//import artoria.servlet.RequestUtils;
//import artoria.spring.RequestContextUtils;
//import artoria.util.Assert;
//import artoria.util.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Arrays;
//import java.util.Map;
//
//import static artoria.common.Constants.COMPUTER_NAME;
//import static artoria.common.Constants.HOST_NAME;
//
//public class HttpEventProvider extends SimpleEventProvider {
//    private static Logger log = LoggerFactory.getLogger(HttpEventProvider.class);
//    private String serverAppId;
//    private String tokenIdName;
//    private String clientAppIdName;
//
//    public HttpEventProvider(String serverAppId, String destination, String tokenIdName, String clientAppIdName) {
//        super(Arrays.asList(
//                "serverId", "serverAppId", "methodName", "interfaceId", "requestMethod",
//                "requestAddress", "requestReferer", "clientUserAgent", "clientNetAddress",
//                "processTime"
//        ));
//        Assert.notBlank(destination, "Parameter \"destination\" must not blank. ");
//        this.serverAppId = serverAppId;
////        this.destination = destination;
//        this.tokenIdName = tokenIdName;
//        this.clientAppIdName = clientAppIdName;
//    }
//
//    @Override
//    protected void process(Event event) {
//        if (event == null) { return; }
//        Map<Object, Object> properties = event.getProperties();
//        properties.put("serverId", StringUtils.isNotBlank(HOST_NAME) ? HOST_NAME : COMPUTER_NAME);
//        if (StringUtils.isNotBlank(serverAppId)) {
//            properties.put("serverAppId", serverAppId);
//        }
//        HttpServletRequest request = RequestContextUtils.getRequest();
//        if (request == null) { return; }
//        if (StringUtils.isNotBlank(tokenIdName)) {
//            properties.put("tokenId", request.getHeader(tokenIdName));
//        }
//        if (StringUtils.isNotBlank(clientAppIdName)) {
//            properties.put("clientAppId", request.getHeader(clientAppIdName));
//        }
//        properties.put("clientUserAgent", RequestUtils.getUserAgent(request));
//        properties.put("clientNetAddress", RequestUtils.getRemoteAddress(request));
//        properties.put("interfaceId", request.getRequestURI());
//        properties.put("requestMethod", request.getMethod());
//        properties.put("requestAddress", String.valueOf(request.getRequestURL()));
//        properties.put("requestReferer", RequestUtils.getReferer(request));
//        // clientName
//        // requestInput
//        // responseOutput
//        // errorMessage
//        // processTime
//        // EventData
//        // Validate parameters.
//        Assert.notBlank(event.getPrincipalId(), "Parameter \"principalId\" must not blank. ");
//    }
//
//    @Override
//    protected void push(Event event) {
//        show(event);
//        MessageUtils.send(event, "event", Boolean.class);
//    }
//
//}

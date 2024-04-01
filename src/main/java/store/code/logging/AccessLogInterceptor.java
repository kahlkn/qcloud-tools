package store.code.logging;

import artoria.servlet.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static artoria.common.Constants.EMPTY_STRING;

/**
 * Access log interceptor.
 * @author Kahle
 */
public class AccessLogInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(AccessLogInterceptor.class);
    private static final ThreadLocal<Long> ACCESS_TIME = new ThreadLocal<Long>();

    private void printAccessLog(HttpServletRequest request) {
        if (ACCESS_TIME.get() == null) { return; }
        Long processTime = (System.nanoTime() - ACCESS_TIME.get()) / 1000000;
        String remoteAddress = RequestUtils.getRemoteAddress(request);
        String requestUrl = String.valueOf(request.getRequestURL());
        String method = request.getMethod();
        method = method != null ? method.toLowerCase() : EMPTY_STRING;
        String format = "client[addr:\"%s\"] %s \"%s\" and time spent %sms. ";
        log.info(String.format(format, remoteAddress, method, requestUrl, processTime));
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        ACCESS_TIME.set(System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) throws Exception {
        printAccessLog(request);
        ACCESS_TIME.remove();
    }

}

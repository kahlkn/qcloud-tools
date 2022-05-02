package store.code.spel.way1;

import artoria.spring.ApplicationContextUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DistributeExceptionAspect {
    private static Logger log = LoggerFactory.getLogger(DistributeExceptionAspect.class);
    private MethodBasedExpressionEvaluator evaluator = new MethodBasedExpressionEvaluator();

    @Pointcut("@annotation(DistributeExceptionHandler)")
    private void exceptionHandleMethod() {

    }

    @AfterThrowing(value = "exceptionHandleMethod()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("error! ", ex);
        String attachmentId = getAttachmentId(joinPoint);
    }

    @Around("exceptionHandleMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String attachmentId = getAttachmentId(joinPoint);
        log.info(">>>>   {} ", attachmentId);
        return joinPoint.proceed();
    }

    private DistributeExceptionHandler getDistributeExceptionHandler(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(DistributeExceptionHandler.class);
    }

    private String getAttachmentId(JoinPoint joinPoint) {
        DistributeExceptionHandler handler = getDistributeExceptionHandler(joinPoint);
        if (joinPoint.getArgs() == null) {
            return null;
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();
        Object target = joinPoint.getTarget();
        Class<?> targetClass = target.getClass();
        EvaluationContext evaluationContext = evaluator.createEvaluationContext(ApplicationContextUtils.getContext(), method, target, args);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        return String.valueOf(evaluator.eval(methodKey, handler.attachmentId(), evaluationContext));
    }

}

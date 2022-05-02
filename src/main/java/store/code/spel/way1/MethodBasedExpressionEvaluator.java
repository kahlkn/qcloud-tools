package store.code.spel.way1;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodBasedExpressionEvaluator extends CachedExpressionEvaluator {
    private final Map<ExpressionKey, Expression> cache = new ConcurrentHashMap<ExpressionKey, Expression>(64);

    public EvaluationContext createEvaluationContext(BeanFactory beanFactory, Method method, Object target, Object[] args) {
        MethodBasedEvaluationContext evaluationContext =
                new MethodBasedEvaluationContext(null, method, args, getParameterNameDiscoverer());

        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return evaluationContext;
    }

    public Expression getExpression(AnnotatedElementKey elementKey, String expression) {

        return getExpression(cache, elementKey, expression);
    }

    public Object eval(AnnotatedElementKey elementKey, String expression, EvaluationContext evalContext) {

        return getExpression(elementKey, expression).getValue(evalContext);
    }

}

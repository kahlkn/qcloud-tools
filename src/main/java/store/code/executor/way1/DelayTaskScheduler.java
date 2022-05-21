package store.code.executor.way1;

import artoria.lifecycle.Destroyable;
import artoria.lifecycle.Initializable;

/**
 * TODO: 2023/6/2 Deletable
 */
@Deprecated
public interface DelayTaskScheduler extends Initializable, Destroyable {

    DelayTaskHandler unregister(String handlerClassName);

    void register(DelayTaskHandler delayTaskHandler);

    DelayTask removeDelayTask(String taskId);

    void addDelayTask(DelayTask delayTask);

}

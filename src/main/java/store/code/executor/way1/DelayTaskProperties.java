package store.code.executor.way1;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * TODO: 2023/6/2 Deletable
 */
@Deprecated
@ConfigurationProperties(prefix = "artoria.schedule.delay")
public class DelayTaskProperties {
    private Boolean enabled;
    private String queueName = "default";
    private Long period = 5L;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

}

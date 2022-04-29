//package store.code.track.way1;
//
//import artoria.event.EventProperties;
//import artoria.event.EventProvider;
//import artoria.event.EventUtils;
//import artoria.event.HttpEventProvider;
//import artoria.event.aspect.EventRecordAspect;
//import artoria.util.Assert;
//import artoria.util.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//
///**
// * The event auto configuration.
// * @author Kahle
// */
//@Configuration
//@Import({EventRecordAspect.class})
//@ConditionalOnProperty(name = "artoria.event.enabled", havingValue = "true")
//@EnableConfigurationProperties({EventProperties.class})
//public class EventAutoConfiguration {
//    private static Logger log = LoggerFactory.getLogger(EventAutoConfiguration.class);
//    @Value("${spring.application.name:unknown}")
//    private String serverAppId;
//
//    @Autowired
//    public EventAutoConfiguration(EventProperties eventProperties) {
//        Assert.notNull(eventProperties, "Parameter \"eventProperties\" must not null. ");
//        //this.eventProperties = eventProperties;
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public EventProvider eventProvider(EventProperties eventProperties) {
//        Assert.notNull(eventProperties, "Parameter \"eventProperties\" must not null. ");
//        String destination = eventProperties.getDestination();
//        String tokenIdName = eventProperties.getTokenIdName();
//        String clientAppIdName = eventProperties.getClientAppIdName();
//        if (StringUtils.isBlank(destination)) {
//            destination = "event_record";
//        }
//        EventProvider eventProvider = new HttpEventProvider(
//                serverAppId, destination, tokenIdName, clientAppIdName);
//        EventUtils.setEventProvider(eventProvider);
//        log.info("Event tools set destination success. ");
//        return eventProvider;
//    }
//
//}

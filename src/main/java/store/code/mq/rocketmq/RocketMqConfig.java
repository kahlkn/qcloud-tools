//package store.code.message.rocketmq;
//
//import artoria.exception.ExceptionUtils;
//import artoria.logging.Logger;
//import artoria.logging.LoggerFactory;
//import com.alibaba.fastjson.JSON;
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.client.producer.DefaultMQProducer;
//import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//import static java.nio.charset.StandardCharsets.UTF_8;
//
//@Configuration
//public class RocketMqConfig {
//    private static Logger log = LoggerFactory.getLogger(RocketMqConfig.class);
//
//    @Resource
//    private EventService eventService;
//
//    private String eventTopic = "test_event";
//
//    @Bean
//    public DefaultMQProducer mqProducer() {
//        try {
//            DefaultMQProducer mqProducer = new DefaultMQProducer("test_event_producer");
//            mqProducer.setNamesrvAddr("172.17.9.168:9876");
//            mqProducer.start();
//            return mqProducer;
//        }
//        catch (Exception e) {
//            throw ExceptionUtils.wrap(e);
//        }
//    }
//
//    @Bean
//    public DefaultMQPushConsumer mqPushConsumer() {
//        try {
//            DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer("test_event_consumer");
//            mqPushConsumer.setNamesrvAddr("172.17.9.168:9876");
//            //mqPushConsumer.setNamespace("event")
//            mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
//            mqPushConsumer.subscribe(eventTopic, "*");
//            mqPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
//                @Override
//                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//                    for (MessageExt messageExt : msgs) {
//                        try {
//                            String jsonData = new String(messageExt.getBody(), UTF_8);
//                            String msgId = messageExt.getMsgId();
//                            EventDTO eventDTO = JSON.parseObject(jsonData, EventDTO.class);
//                            log.info("The event listener: msgId = {}, event = {}", msgId, jsonData);
//                            eventService.handle(eventDTO);
//                        }
//                        catch (Exception e) {
//                            log.error("consume message failed. messageId:{}, topic:{}, reconsumeTimes:{}"
//                                    , messageExt.getMsgId(), messageExt.getTopic(), messageExt.getReconsumeTimes(), e);
//                            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//                        }
//                    }
//                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//                }
//            });
//            mqPushConsumer.start();
//            return mqPushConsumer;
//        }
//        catch (Exception e) {
//            throw ExceptionUtils.wrap(e);
//        }
//    }
//
//}

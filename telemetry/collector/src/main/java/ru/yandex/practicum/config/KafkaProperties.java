package ru.yandex.practicum.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private String bootstrapServers;
    private Topics topics = new Topics();

    @Data
    public static class Topics {
        private String hubs;
        private String sensors;
    }

}

package ru.yandex.practicum.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.mapper.SensorEventMapper;
import ru.yandex.practicum.model.hub.HubEvent;
import ru.yandex.practicum.model.sensor.SensorEvent;

import java.util.Properties;

@Service
public class EventService {
    private final Producer<String, SpecificRecordBase> producer;
    private static final String HUB_TOPIC_NAME = "telemetry.hubs.v1";
    private static final String SENSOR_TOPIC_NAME = "telemetry.sensor.v1";


    public EventService() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "ru.yandex.practicum.serializer.AvroSerializer");
        producer = new KafkaProducer<>(config);
    }

    public void createSensorEvent(SensorEvent event) {
        SensorEventAvro sensorEventAvro = SensorEventMapper.toSensorEventAvro(event);
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                SENSOR_TOPIC_NAME,
                null,
                event.getTimestamp().toEpochMilli(),
                event.getHubId(),
                sensorEventAvro
        );
        producer.send(record);
    }

    public void createHubEvent(HubEvent event) {
        HubEventAvro hubEventAvro = HubEventMapper.toHubEventAvro(event);
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
                HUB_TOPIC_NAME,
                null,
                event.getTimestamp().toEpochMilli(),
                event.getHubId(),
                hubEventAvro
        );
        producer.send(record);
    }


}

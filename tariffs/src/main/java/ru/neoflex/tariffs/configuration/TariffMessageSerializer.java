package ru.neoflex.tariffs.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import ru.neoflex.tariffs.models.responses.TariffMessage;

public class TariffMessageSerializer implements Serializer<TariffMessage> {

    @Override
    public byte[] serialize(String s, TariffMessage emailMessage) {
        if (emailMessage == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsBytes(emailMessage);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error when serializing EmailMessage: " + emailMessage + " to byte[]");
        }
    }
}
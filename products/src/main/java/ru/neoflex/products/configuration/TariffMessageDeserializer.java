package ru.neoflex.products.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import ru.neoflex.products.models.requests.TariffMessage;

import java.nio.charset.StandardCharsets;

public class TariffMessageDeserializer implements Deserializer<TariffMessage> {

    @Override
    public TariffMessage deserialize(String s, byte[] bytes) {
        try {
            if (bytes == null) {
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new String(bytes, StandardCharsets.UTF_8), TariffMessage.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to EmailMessage");
        }
    }
}
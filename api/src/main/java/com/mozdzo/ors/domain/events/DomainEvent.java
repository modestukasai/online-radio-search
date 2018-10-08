package com.mozdzo.ors.domain.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.ApplicationEvent;

import java.io.IOException;

abstract class DomainEvent extends ApplicationEvent {

    DomainEvent(Object source) {
        super(source);
    }

    abstract static class Data {
        public static <T extends Data> T deserialize(String body, Class<T> eventDataClass) {
            try {
                return EventObjectMapper.mapper.readValue(body, eventDataClass);
            } catch (IOException e) {
                throw new EventDeserializationFailedException(body, eventDataClass);
            }
        }
    }

    abstract Event.Type type();

    abstract Data getData();

    String serialize() {
        try {
            return EventObjectMapper.mapper.writeValueAsString(this.getData());
        } catch (JsonProcessingException e) {
            throw new EventSerializationFailedException(this.getClass());
        }
    }
}

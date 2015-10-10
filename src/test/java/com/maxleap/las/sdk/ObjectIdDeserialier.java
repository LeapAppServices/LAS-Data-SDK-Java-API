package com.maxleap.las.sdk;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 *
 * @author Jing Zhao
 * @since 2.0
 */
public class ObjectIdDeserialier extends JsonDeserializer<ObjectId> {
    @Override
    public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String str = jp.getValueAsString();
        if (str != null) {
            return new ObjectId(str);
        } else {
            return null;
        }
    }

}

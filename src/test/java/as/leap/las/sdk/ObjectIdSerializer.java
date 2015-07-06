package as.leap.las.sdk;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.HashMap;

/**
 * Serializer for object ids, serialises strings or byte arrays to an ObjectId
 * class
 * 
 * @author Jing Zhao
 * @since 2.0
 */
public class ObjectIdSerializer extends JsonSerializer {
    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        if (value instanceof Iterable) {
            jgen.writeStartArray();
            for (Object item : (Iterable) value) {
                jgen.writeObject(serialiseObject(item));
            }
            jgen.writeEndArray();
        } else {
            jgen.writeObject(serialiseObject(value));
        }
    }

    private Object serialiseObject(Object value) throws JsonMappingException {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return new ObjectId((String) value);
        } else if (value instanceof byte[]) {
            return new ObjectId((byte[]) value);
        } else if (value instanceof ObjectId) {
            HashMap<Object, Object> map = new HashMap<>();
            map.put("$oid", value.toString());
            return map;
        } else {
            throw new JsonMappingException("Cannot deserialise object of type " + value.getClass() + " to ObjectId");
        }
    }
}

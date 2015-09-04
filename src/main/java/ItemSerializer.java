import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ItemSerializer extends StdSerializer<Item> implements ContextualSerializer {

    public ItemSerializer() {
        super(Item.class);
    }

    @Override
    public void serialize(Item value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        int i = 5;

    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        return this;
    }
}

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ThingSerializer extends BeanSerializerBase {


    public ThingSerializer(BeanSerializerBase source) {
        super(source);
    }

    @Override
    public void serialize(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {

    }

    public ThingSerializer(ThingSerializer source,
                         ObjectIdWriter objectIdWriter) {
        super(source, objectIdWriter);
    }

    public ThingSerializer(ThingSerializer source,
                         String[] toIgnore) {
        super(source, toIgnore);
    }

    public ThingSerializer(ThingSerializer source,
                           ObjectIdWriter objectIdWriter,
                           Object filterId) {
        super(source, objectIdWriter, filterId);
    }

    @Override
    public BeanSerializerBase withObjectIdWriter(
            ObjectIdWriter objectIdWriter) {
        return new ThingSerializer(this, objectIdWriter);
    }

    @Override
    protected BeanSerializerBase withIgnorals(String[] toIgnore) {
        return new ThingSerializer(this, toIgnore);
    }

    @Override
    protected BeanSerializerBase asArraySerializer() {
        /* Can not:
         *
         * - have Object Id (may be allowed in future)
         * - have "any getter"
         * - have per-property filters
         */
        if ((_objectIdWriter == null)
                && (_anyGetterWriter == null)
                && (_propertyFilterId == null)
                ) {
            return new BeanAsArraySerializer(this);
        }
        // already is one, so:
        return this;
    }

    @Override
    public BeanSerializerBase withFilterId(Object filterId) {
        return new ThingSerializer(this, _objectIdWriter, filterId);
    }
}
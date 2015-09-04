import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceSerializer extends BeanSerializerBase {


    public ResourceSerializer(BeanSerializerBase source) {
        super(source);
    }

    @Override
    public void serialize(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
        //This is stolen from https://github.com/FasterXML/jackson-databind/blob/master/src/main/java/com/fasterxml/jackson/databind/ser/BeanSerializer.java
        if (_objectIdWriter != null) {
            gen.setCurrentValue(bean); // [databind#631]
            _serializeWithObjectId(bean, gen, provider, true);
            return;
        }
        gen.writeStartObject();
        // [databind#631]: Assign current value, to be accessible by custom serializers
        gen.setCurrentValue(bean);
        if (_propertyFilterId != null) {
            serializeFieldsFiltered(bean, gen, provider);
        } else {
            serializeFields(bean, gen, provider);
        }

        this.serializeControls(bean, gen, provider);

        gen.writeEndObject();
    }


    public void serializeControls(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeFieldName("_links");
        gen.writeStartArray();

        Stream<BeanPropertyWriter> x = Arrays.stream(_props)
            .filter(p -> Link.class.isAssignableFrom(p.getPropertyType()));

        //TODO: group by rel, serialize each rel.  Keep in mind that some should be {} and others []
        x.forEach(p -> {
            try {
                p.serializeAsElement(bean, gen, provider);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        gen.writeEndArray();

    }

    public ResourceSerializer(ResourceSerializer source,
                              ObjectIdWriter objectIdWriter) {
        super(source, objectIdWriter);
    }

    public ResourceSerializer(ResourceSerializer source,
                              String[] toIgnore) {
        super(source, toIgnore);
    }

    public ResourceSerializer(ResourceSerializer source,
                              ObjectIdWriter objectIdWriter,
                              Object filterId) {
        super(source, objectIdWriter, filterId);
    }

    @Override
    public BeanSerializerBase withObjectIdWriter(
            ObjectIdWriter objectIdWriter) {
        return new ResourceSerializer(this, objectIdWriter);
    }

    @Override
    protected BeanSerializerBase withIgnorals(String[] toIgnore) {
        return new ResourceSerializer(this, toIgnore);
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
        return new ResourceSerializer(this, _objectIdWriter, filterId);
    }
}
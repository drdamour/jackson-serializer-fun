import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import org.junit.Test;

public class SerializeTest {

    @Test
    public void step1NoRealModifications() throws JsonProcessingException {
        Thing t = new Thing();

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("whatevs");
        module.setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                return serializer;
            }
        });

        mapper.registerModule(module);


        System.out.println(mapper.writeValueAsString(t));

    }
}

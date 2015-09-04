import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.junit.Test;

public class SerializeTest {

    @Test
    public void step1NoModifications() throws JsonProcessingException {
        ExampleResource t = new ExampleResource();

        ObjectMapper mapper = new ObjectMapper();


        System.out.println(mapper.writeValueAsString(t));

    }

    @Test
    public void step2HideLinkFields() throws JsonProcessingException {
        ExampleResource t = new ExampleResource();

        ObjectMapper mapper = new ObjectMapper();

        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {


            @Override
            public Object findFilterId(Annotated a) {
                //if it's a thing, give it the thing filter
                if (Resource.class.isAssignableFrom(a.getRawType())) {
                    return "resource";
                }
                return super.findFilterId(a);
            }
        });

        //register the thing filter
        mapper = mapper.setFilterProvider(new SimpleFilterProvider().addFilter("resource",
            new SimpleBeanPropertyFilter() {

                @Override
                protected boolean include(PropertyWriter writer) {
                    //Don't include anything that is an Item
                    if (writer instanceof BeanPropertyWriter) {
                        BeanPropertyWriter beanWriter = (BeanPropertyWriter) writer;
                        if (Link.class.isAssignableFrom(beanWriter.getPropertyType())) {
                            return false;
                        }
                    }
                    return super.include(writer);
                }


            }
        ));
        System.out.println(mapper.writeValueAsString(t));

    }



    @Test
    public void step3HideLinkArrays() throws JsonProcessingException {
        ExampleResource t = new ExampleResource();

        ObjectMapper mapper = new ObjectMapper();

        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {


            @Override
            public Object findFilterId(Annotated a) {
                //if it's a thing, give it the thing filter
                if (Resource.class.isAssignableFrom(a.getRawType())) {
                    return "resource";
                }
                return super.findFilterId(a);
            }
        });

        //register the thing filter
        mapper = mapper.setFilterProvider(new SimpleFilterProvider().addFilter("resource",
            new SimpleBeanPropertyFilter() {

                @Override
                protected boolean include(PropertyWriter writer) {
                    //Don't include any Link properties
                    if (writer instanceof BeanPropertyWriter) {
                        BeanPropertyWriter beanWriter = (BeanPropertyWriter) writer;
                        if (Link.class.isAssignableFrom(beanWriter.getPropertyType())) {
                            return false;
                        }

                        if(Link[].class.isAssignableFrom(beanWriter.getPropertyType())){
                            return false;
                        }
                    }
                    return super.include(writer);
                }

            }
        ));
        System.out.println(mapper.writeValueAsString(t));

    }


    @Test
    public void step4SerializeLinks() throws JsonProcessingException {
        ExampleResource t = new ExampleResource();

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("whatevs");


        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {


            @Override
            public Object findFilterId(Annotated a) {
                //if it's a thing, give it the thing filter
                if (Resource.class.isAssignableFrom(a.getRawType())) {
                    return "resource";
                }
                return super.findFilterId(a);
            }
        });

        //register the thing filter
        mapper = mapper.setFilterProvider(new SimpleFilterProvider().addFilter("resource",
            new SimpleBeanPropertyFilter() {

                @Override
                protected boolean include(PropertyWriter writer) {
                    //Don't include any Link properties
                    if (writer instanceof BeanPropertyWriter) {
                        BeanPropertyWriter beanWriter = (BeanPropertyWriter) writer;
                        if (Link.class.isAssignableFrom(beanWriter.getPropertyType())) {
                            return false;
                        }

                        if(Link[].class.isAssignableFrom(beanWriter.getPropertyType())){
                            return false;
                        }
                    }
                    return super.include(writer);
                }

            }
        ));


        module.setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                if (Resource.class.isAssignableFrom(beanDesc.getBeanClass())) {
                    return new ResourceSerializer((BeanSerializer)serializer);
                }
                return serializer;
            }
        });


        mapper.registerModule(module);

        System.out.println(mapper.writeValueAsString(t));

    }



}

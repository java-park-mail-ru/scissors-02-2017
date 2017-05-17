package game.resourses;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class ResourseFactory {
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourseFactory.class);

    public ResourseFactory() {
    }

    public <T> T get(String path, Class<T> clazz) {
        URL resourceDescriptor;
        try {
            resourceDescriptor = Resources.getResource(path);
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Unable to find resource " + path, ex);
            return null;
        }

        T resource;
        try {
            resource = objectMapper.readValue(resourceDescriptor, clazz);
        } catch (IOException e) {
            LOGGER.error("Failed constructing resource object " + path + " of type " + clazz.getName(), e);
            return null;
        }
        return resource;
    }

}

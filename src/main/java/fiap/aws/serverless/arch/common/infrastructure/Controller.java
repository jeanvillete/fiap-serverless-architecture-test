package fiap.aws.serverless.arch.common.infrastructure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fiap.aws.serverless.arch.common.domain.RequestMapping;

import java.io.IOException;

public abstract class Controller {

    public static class ImpossibleParseJsonException extends Exception {

        public ImpossibleParseJsonException(Throwable cause) {
            super(cause);
        }

    }

    private final ObjectMapper objectMapper;

    public Controller() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    }

    protected <T> T parseBody(RequestMapping requestMapping, Class<T> toType) throws ImpossibleParseJsonException {
        String body = requestMapping.getBody();

        try {
            return objectMapper.readValue(body, toType);
        } catch (IOException e) {
            throw new ImpossibleParseJsonException(e);
        }
    }

}

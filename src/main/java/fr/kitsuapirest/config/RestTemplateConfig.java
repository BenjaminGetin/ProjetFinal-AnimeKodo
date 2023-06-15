package fr.kitsuapirest.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * The RestTemplateConfig class provides the configuration for RestTemplate.
 */
@Component
public class RestTemplateConfig {

    /**
     * Creates and configures a RestTemplate bean.
     *
     * @return the configured RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

        // Configure the message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.parseMediaType("application/vnd.api+json")));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        converter.setObjectMapper(objectMapper);

        restTemplate.getMessageConverters().add(converter);

        return restTemplate;
    }

    /**
     * Creates a SimpleClientHttpRequestFactory bean with custom timeouts.
     *
     * @return the configured SimpleClientHttpRequestFactory.
     */
    private SimpleClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }

}

package br.com.selat.consumer.config;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;


@ApplicationPath("consumer")
public class ApiConfig extends ResourceConfig {

    public ApiConfig(){
        packages("br.com.selat");
        register(JacksonJsonProvider.class);
    }
}

package br.com.selat.appsample.config;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;


@ApplicationPath("appsample")
public class ApiConfig extends ResourceConfig {

    public ApiConfig(){
        packages("br.com.selat.appsample");
        register(JacksonJsonProvider.class);
    }
}

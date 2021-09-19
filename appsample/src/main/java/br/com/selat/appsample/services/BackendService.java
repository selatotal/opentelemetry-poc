package br.com.selat.appsample.services;

import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.Optional;

@Service
public class BackendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendService.class);
    private static final String URL = Optional.ofNullable(System.getenv("BACKEND_URL")).orElse("http://localhost:8081");

    public Optional<String> backendRequest(){

        try {
            Content content = Request.get(URL+"/consumer/hello").execute().returnContent();
            return Optional.of(content.asString());
        } catch (IOException e) {
            LOGGER.error("Error requesting backend", e);
        }
        return Optional.empty();
    }
}

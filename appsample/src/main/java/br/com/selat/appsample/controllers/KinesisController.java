package br.com.selat.appsample.controllers;

import br.com.selat.appsample.services.KinesisService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

@Path("/kinesis")
public class KinesisController {

    private final KinesisService kinesisService;

    @Autowired
    public KinesisController(KinesisService kinesisService) {
        this.kinesisService = kinesisService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(){
        String response = kinesisService.publishMessage("test", "partitionKey", Collections.singletonMap("name", "John Doe"));
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(response).build();
    }
}

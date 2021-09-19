package br.com.selat.consumer.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(){
        return Response.ok().type(MediaType.APPLICATION_JSON).entity("Hello").build();
    }

}

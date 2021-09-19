package br.com.selat.appsample.controllers;

import br.com.selat.appsample.services.BackendService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/backend")
public class BackendController {

    private final BackendService backendService;

    @Autowired
    public BackendController(BackendService backendService) {
        this.backendService = backendService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(){
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(backendService.backendRequest()).build();
    }
}

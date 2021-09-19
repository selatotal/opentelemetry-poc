package br.com.selat.appsample.controllers;

import br.com.selat.appsample.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/database")
public class DatabaseController {

    private final DatabaseService databaseService;

    @Autowired
    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(){
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(databaseService.health()).build();
    }
}

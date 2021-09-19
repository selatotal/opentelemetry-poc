package br.com.selat.appsample.controllers;

import br.com.selat.appsample.services.IotCoreService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/iot")
public class IotCoreController {

    private final IotCoreService iotCoreService;

    @Autowired
    public IotCoreController(IotCoreService iotCoreService) {
        this.iotCoreService = iotCoreService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello(){
        iotCoreService.health();
        return Response.ok().type(MediaType.APPLICATION_JSON).entity("OK").build();
    }
}

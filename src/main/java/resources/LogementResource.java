package resources;

import entities.Logement;
import filtres.Secured;
import metiers.LogementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/logements")
public class LogementResource {
    private static LogementBusiness logementBusiness = new LogementBusiness();

    @Secured
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogements(@QueryParam("reference") Integer reference,
                                 @QueryParam("delegation") String delegation) {
        if (reference != null) {
            Logement logement = logementBusiness.getLogementsByReference(reference);
            if (logement == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No logement found with reference: " + reference)
                        .build();
            }
            return Response.ok(logement).build();
        }

        if (delegation != null && !delegation.isEmpty()) {
            List<Logement> logements = logementBusiness.getLogementsByDeleguation(delegation);
            if (logements.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No logements found for delegation: " + delegation)
                        .build();
            }
            return Response.ok(logements).build();
        }

        return Response.ok(logementBusiness.getLogements()).build();
    }

    @GET
    @Path("/reference")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogementByReference(@QueryParam("reference") Integer reference) {
        if (reference == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Reference parameter is required")
                    .build();
        }

        Logement logement = logementBusiness.getLogementsByReference(reference);
        if (logement == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No logement found with reference: " + reference)
                    .build();
        }
        return Response.ok(logement).build();
    }

    @GET
    @Path("/delegation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogementsByDelegation(@QueryParam("delegation") String delegation) {
        if (delegation == null || delegation.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Delegation parameter is required")
                    .build();
        }

        List<Logement> logements = logementBusiness.getLogementsByDeleguation(delegation);
        if (logements.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No logements found for delegation: " + delegation)
                    .build();
        }
        return Response.ok(logements).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addLogement(Logement logement) {
        boolean added = logementBusiness.addLogement(logement);
        if (added) {
            return Response.status(Response.Status.CREATED).entity("Logement added").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Error adding logement").build();
    }

    @PUT
    @Path("/{reference}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateLogement(@PathParam("reference") int reference, Logement logement) {
        boolean updated = logementBusiness.updateLogement(reference, logement);
        if (updated) {
            return Response.ok("Logement updated").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Logement not found").build();
    }

    @DELETE
    @Path("/{reference}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteLogement(@PathParam("reference") int reference) {
        boolean deleted = logementBusiness.deleteLogement(reference);
        if (deleted) {
            return Response.ok("Logement deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Logement not found").build();
    }
}

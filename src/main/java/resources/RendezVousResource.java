package resources;

import entities.RendezVous;
import metiers.RendezVousBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/rendezvous")
public class RendezVousResource {
    private static RendezVousBusiness rendezVousBusiness = new RendezVousBusiness();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRendezVous(RendezVous rendezVous) {
        boolean added = rendezVousBusiness.addRendezVous(rendezVous);
        if (added) {
            return Response.status(Response.Status.CREATED).entity("RendezVous added").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Error adding RendezVous").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRendezVous(@QueryParam("refLogement") Integer refLogement) {
        if (refLogement != null) {
            List<RendezVous> rendezVousList = rendezVousBusiness.getListeRendezVousByLogementReference(refLogement);
            if (rendezVousList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("No RendezVous found for Logement reference: " + refLogement).build();
            }
            return Response.ok(rendezVousList).build();
        }
        return Response.ok(rendezVousBusiness.getListeRendezVous()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRendezVousById(@PathParam("id") int id) {
        RendezVous rendezVous = rendezVousBusiness.getRendezVousById(id);
        if (rendezVous == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No RendezVous found with ID: " + id).build();
        }
        return Response.ok(rendezVous).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteRendezVous(@PathParam("id") int id) {
        boolean deleted = rendezVousBusiness.deleteRendezVous(id);
        if (deleted) {
            return Response.ok("RendezVous deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("RendezVous not found").build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateRendezVous(@PathParam("id") int id, RendezVous rendezVous) {
        boolean updated = rendezVousBusiness.updateRendezVous(id, rendezVous);
        if (updated) {
            return Response.ok("RendezVous updated").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("RendezVous not found").build();
    }
}

package edu.kpi.rest;

import edu.kpi.model.MovieSession;
import edu.kpi.service.MovieSessionService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieSessionResource {

  @EJB
  private MovieSessionService sessionService;

  @GET
  public Response getSessions(
      @QueryParam("title") String title,
      @QueryParam("page") @DefaultValue("1") int page,
      @QueryParam("size") @DefaultValue("10") int size) {

    List<MovieSession> result = sessionService.getFilteredSessions(title, page, size);
    return Response.ok(result).build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteSession(@PathParam("id") long id) {
    if (sessionService.getSession(id) == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    sessionService.deleteSession(id);
    return Response.noContent().build();
  }
}

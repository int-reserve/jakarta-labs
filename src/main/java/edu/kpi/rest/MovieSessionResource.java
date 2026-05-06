package edu.kpi.rest;

import edu.kpi.model.MovieSession;
import edu.kpi.repository.DataStore;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieSessionResource {

    private final Validator validator;

    public MovieSessionResource() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @GET
    public Response getSessions(
            @QueryParam("title") String title,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        
        List<MovieSession> allSessions = DataStore.getAllSessions();
        
        // Filtering
        if (title != null && !title.trim().isEmpty()) {
            allSessions = allSessions.stream()
                .filter(s -> s.getMovieTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
        }

        // Pagination
        int total = allSessions.size();
        int offset = (page - 1) * size;
        
        if (offset >= total) {
            return Response.ok(List.of()).header("X-Total-Count", total).build();
        }

        int toIndex = Math.min(offset + size, total);
        List<MovieSession> result = allSessions.subList(offset, toIndex);

        return Response.ok(result)
                .header("X-Total-Count", total)
                .header("X-Page", page)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getSession(@PathParam("id") String id) {
        MovieSession session = DataStore.getSession(id);
        if (session == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(session).build();
    }

    @POST
    public Response createSession(@Valid MovieSession session) {
        Set<ConstraintViolation<MovieSession>> violations = validator.validate(session);
        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toList());
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }

        session.setId(UUID.randomUUID().toString());
        DataStore.addSession(session);
        return Response.status(Response.Status.CREATED).entity(session).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateSession(@PathParam("id") String id, @Valid MovieSession updatedSession) {
        Set<ConstraintViolation<MovieSession>> violations = validator.validate(updatedSession);
        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toList());
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }

        MovieSession existing = DataStore.getSession(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        existing.setMovieTitle(updatedSession.getMovieTitle());
        existing.setStartTime(updatedSession.getStartTime());
        existing.setPrice(updatedSession.getPrice());
        
        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSession(@PathParam("id") String id) {
        MovieSession existing = DataStore.getSession(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        DataStore.deleteSession(id);
        return Response.noContent().build();
    }
}

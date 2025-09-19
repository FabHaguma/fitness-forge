package com.gymmis.resource;

import com.gymmis.entity.ClassSession;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/class-sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClassSessionResource {

    @PersistenceContext
    EntityManager entityManager;

    @GET
    public List<ClassSession> getAllClassSessions() {
        return entityManager.createQuery("SELECT cs FROM ClassSession cs", ClassSession.class).getResultList();
    }

    @GET
    @Path("/{id}")
    public ClassSession getClassSession(@PathParam("id") Long id) {
        ClassSession classSession = entityManager.find(ClassSession.class, id);
        if (classSession == null) {
            throw new WebApplicationException("Class session not found", 404);
        }
        return classSession;
    }

    @POST
    @Transactional
    public Response createClassSession(ClassSession classSession) {
        entityManager.persist(classSession);
        return Response.status(Response.Status.CREATED).entity(classSession).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateClassSession(@PathParam("id") Long id, ClassSession updatedClassSession) {
        ClassSession classSession = entityManager.find(ClassSession.class, id);
        if (classSession == null) {
            throw new WebApplicationException("Class session not found", 404);
        }
        classSession.setName(updatedClassSession.getName());
        classSession.setType(updatedClassSession.getType());
        classSession.setSchedule(updatedClassSession.getSchedule());
        classSession.setTrainer(updatedClassSession.getTrainer());
        entityManager.merge(classSession);
        return Response.ok(classSession).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteClassSession(@PathParam("id") Long id) {
        ClassSession classSession = entityManager.find(ClassSession.class, id);
        if (classSession == null) {
            throw new WebApplicationException("Class session not found", 404);
        }
        entityManager.remove(classSession);
        return Response.noContent().build();
    }
}
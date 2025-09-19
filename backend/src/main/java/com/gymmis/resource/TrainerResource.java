package com.gymmis.resource;

import com.gymmis.entity.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/trainers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrainerResource {

    @PersistenceContext
    EntityManager entityManager;

    @GET
    public List<Trainer> getAllTrainers() {
        return entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class).getResultList();
    }

    @GET
    @Path("/{id}")
    public Trainer getTrainer(@PathParam("id") Long id) {
        Trainer trainer = entityManager.find(Trainer.class, id);
        if (trainer == null) {
            throw new WebApplicationException("Trainer not found", 404);
        }
        return trainer;
    }

    @POST
    @Transactional
    public Response createTrainer(Trainer trainer) {
        entityManager.persist(trainer);
        return Response.status(Response.Status.CREATED).entity(trainer).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateTrainer(@PathParam("id") Long id, Trainer updatedTrainer) {
        Trainer trainer = entityManager.find(Trainer.class, id);
        if (trainer == null) {
            throw new WebApplicationException("Trainer not found", 404);
        }
        trainer.setName(updatedTrainer.getName());
        trainer.setSpecialty(updatedTrainer.getSpecialty());
        entityManager.merge(trainer);
        return Response.ok(trainer).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteTrainer(@PathParam("id") Long id) {
        Trainer trainer = entityManager.find(Trainer.class, id);
        if (trainer == null) {
            throw new WebApplicationException("Trainer not found", 404);
        }
        entityManager.remove(trainer);
        return Response.noContent().build();
    }
}
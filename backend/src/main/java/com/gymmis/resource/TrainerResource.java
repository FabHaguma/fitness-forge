package com.gymmis.resource;

import com.gymmis.dao.TrainerDao;
import com.gymmis.entity.Trainer;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/trainers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrainerResource {

    @Inject
    TrainerDao trainerDao;

    @GET
    public List<Trainer> getAllTrainers() {
        try {
            return trainerDao.findAll();
        } catch (SQLException e) {
            throw new WebApplicationException("Database error", 500);
        }
    }

    @GET
    @Path("/{id}")
    public Trainer getTrainer(@PathParam("id") Long id) {
        try {
            Trainer trainer = trainerDao.findById(id);
            if (trainer == null) {
                throw new WebApplicationException("Trainer not found", 404);
            }
            return trainer;
        } catch (SQLException e) {
            throw new WebApplicationException("Database error", 500);
        }
    }

    @POST
    public Response createTrainer(Trainer trainer) {
        try {
            trainerDao.save(trainer);
            return Response.status(Response.Status.CREATED).entity(trainer).build();
        } catch (SQLException e) {
            throw new WebApplicationException("Database error", 500);
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateTrainer(@PathParam("id") Long id, Trainer updatedTrainer) {
        try {
            Trainer trainer = trainerDao.findById(id);
            if (trainer == null) {
                throw new WebApplicationException("Trainer not found", 404);
            }
            updatedTrainer.setId(id);
            trainerDao.save(updatedTrainer);
            return Response.ok(updatedTrainer).build();
        } catch (SQLException e) {
            throw new WebApplicationException("Database error", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTrainer(@PathParam("id") Long id) {
        try {
            trainerDao.delete(id);
            return Response.noContent().build();
        } catch (SQLException e) {
            throw new WebApplicationException("Database error", 500);
        }
    }
}
package com.gymmis.resource;

import com.gymmis.entity.MembershipPlan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/membership-plans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MembershipPlanResource {

    @PersistenceContext
    EntityManager entityManager;

    @GET
    public List<MembershipPlan> getAllMembershipPlans() {
        return entityManager.createQuery("SELECT mp FROM MembershipPlan mp", MembershipPlan.class).getResultList();
    }

    @GET
    @Path("/{id}")
    public MembershipPlan getMembershipPlan(@PathParam("id") Long id) {
        MembershipPlan plan = entityManager.find(MembershipPlan.class, id);
        if (plan == null) {
            throw new WebApplicationException("Membership plan not found", 404);
        }
        return plan;
    }

    @POST
    @Transactional
    public Response createMembershipPlan(MembershipPlan plan) {
        entityManager.persist(plan);
        return Response.status(Response.Status.CREATED).entity(plan).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateMembershipPlan(@PathParam("id") Long id, MembershipPlan updatedPlan) {
        MembershipPlan plan = entityManager.find(MembershipPlan.class, id);
        if (plan == null) {
            throw new WebApplicationException("Membership plan not found", 404);
        }
        plan.setName(updatedPlan.getName());
        plan.setType(updatedPlan.getType());
        plan.setAmount(updatedPlan.getAmount());
        entityManager.merge(plan);
        return Response.ok(plan).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteMembershipPlan(@PathParam("id") Long id) {
        MembershipPlan plan = entityManager.find(MembershipPlan.class, id);
        if (plan == null) {
            throw new WebApplicationException("Membership plan not found", 404);
        }
        entityManager.remove(plan);
        return Response.noContent().build();
    }
}
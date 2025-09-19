package com.gymmis.resource;

import com.gymmis.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/members")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MemberResource {

    @PersistenceContext
    EntityManager entityManager;

    @GET
    public List<Member> getAllMembers() {
        return entityManager.createQuery("SELECT m FROM Member m", Member.class).getResultList();
    }

    @GET
    @Path("/{id}")
    public Member getMember(@PathParam("id") Long id) {
        Member member = entityManager.find(Member.class, id);
        if (member == null) {
            throw new WebApplicationException("Member not found", 404);
        }
        return member;
    }

    @POST
    @Transactional
    public Response createMember(Member member) {
        entityManager.persist(member);
        return Response.status(Response.Status.CREATED).entity(member).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateMember(@PathParam("id") Long id, Member updatedMember) {
        Member member = entityManager.find(Member.class, id);
        if (member == null) {
            throw new WebApplicationException("Member not found", 404);
        }
        member.setName(updatedMember.getName());
        member.setAge(updatedMember.getAge());
        member.setGender(updatedMember.getGender());
        member.setContact(updatedMember.getContact());
        entityManager.merge(member);
        return Response.ok(member).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteMember(@PathParam("id") Long id) {
        Member member = entityManager.find(Member.class, id);
        if (member == null) {
            throw new WebApplicationException("Member not found", 404);
        }
        entityManager.remove(member);
        return Response.noContent().build();
    }
}
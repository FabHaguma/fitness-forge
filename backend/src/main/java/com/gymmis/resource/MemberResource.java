package com.gymmis.resource;

import com.gymmis.entity.Member;
import com.gymmis.service.MemberService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/members")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MemberResource {

    @Inject
    MemberService memberService;

    @GET
    public List<Member> getAllMembers() {
        try {
            return memberService.findAll();
        } catch (SQLException e) {
            throw new WebApplicationException("Database error", 500);
        }
    }

    @GET
    @Path("/{id}")
    public Member getMember(@PathParam("id") Long id) {
        try {
            Member member = memberService.findById(id);
            if (member == null) {
                throw new WebApplicationException("Member not found", 404);
            }
            return member;
        } catch (SQLException e) {
            throw new WebApplicationException("Database error", 500);
        }
    }

    @POST
    public Response createMember(Member member) {
        try {
            memberService.save(member);
            return Response.status(Response.Status.CREATED).entity(member).build();
        } catch (SQLException e) {
            throw new WebApplicationException("Database error", 500);
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateMember(@PathParam("id") Long id, Member updatedMember) {
        try {
            Member member = memberService.findById(id);
            if (member == null) {
                throw new WebApplicationException("Member not found", 404);
            }
            updatedMember.setId(id);
            memberService.save(updatedMember);
            return Response.ok(updatedMember).build();
        } catch (SQLException e) {
            throw new WebApplicationException("Database error", 500);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMember(@PathParam("id") Long id) {
        try {
            memberService.delete(id);
            return Response.noContent().build();
        } catch (SQLException e) {
            throw new WebApplicationException("Database error", 500);
        }
    }
}
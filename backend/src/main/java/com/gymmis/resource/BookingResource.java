package com.gymmis.resource;

import com.gymmis.entity.Booking;
import com.gymmis.entity.Member;
import com.gymmis.entity.ClassSession;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    @PersistenceContext
    EntityManager entityManager;

    @GET
    public List<Booking> getAllBookings() {
        return entityManager.createQuery("SELECT b FROM Booking b", Booking.class).getResultList();
    }

    @GET
    @Path("/{id}")
    public Booking getBooking(@PathParam("id") Long id) {
        Booking booking = entityManager.find(Booking.class, id);
        if (booking == null) {
            throw new WebApplicationException("Booking not found", 404);
        }
        return booking;
    }

    @POST
    @Transactional
    public Response createBooking(Booking booking) {
        entityManager.persist(booking);
        return Response.status(Response.Status.CREATED).entity(booking).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateBooking(@PathParam("id") Long id, Booking updatedBooking) {
        Booking booking = entityManager.find(Booking.class, id);
        if (booking == null) {
            throw new WebApplicationException("Booking not found", 404);
        }
        booking.setMember(updatedBooking.getMember());
        booking.setClassSession(updatedBooking.getClassSession());
        booking.setStatus(updatedBooking.getStatus());
        entityManager.merge(booking);
        return Response.ok(booking).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response cancelBooking(@PathParam("id") Long id) {
        Booking booking = entityManager.find(Booking.class, id);
        if (booking == null) {
            throw new WebApplicationException("Booking not found", 404);
        }
        entityManager.remove(booking);
        return Response.noContent().build();
    }

    // Additional endpoint for members to book a class
    @POST
    @Path("/book")
    @Transactional
    public Response bookClass(@QueryParam("memberId") Long memberId, @QueryParam("classSessionId") Long classSessionId) {
        Member member = entityManager.find(Member.class, memberId);
        ClassSession classSession = entityManager.find(ClassSession.class, classSessionId);
        if (member == null || classSession == null) {
            throw new WebApplicationException("Member or Class Session not found", 404);
        }

        Booking booking = new Booking();
        booking.setMember(member);
        booking.setClassSession(classSession);
        booking.setStatus("booked");
        entityManager.persist(booking);
        return Response.status(Response.Status.CREATED).entity(booking).build();
    }

    // Additional endpoint for members to cancel a booking
    @PUT
    @Path("/cancel/{id}")
    @Transactional
    public Response cancelBookingById(@PathParam("id") Long id) {
        Booking booking = entityManager.find(Booking.class, id);
        if (booking == null) {
            throw new WebApplicationException("Booking not found", 404);
        }
        booking.setStatus("cancelled");
        entityManager.merge(booking);
        return Response.ok(booking).build();
    }
}
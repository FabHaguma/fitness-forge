package com.gymmis.resource;

import com.gymmis.entity.Invoice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/invoices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InvoiceResource {

    @PersistenceContext
    EntityManager entityManager;

    @GET
    public List<Invoice> getAllInvoices() {
        return entityManager.createQuery("SELECT i FROM Invoice i", Invoice.class).getResultList();
    }

    @GET
    @Path("/{id}")
    public Invoice getInvoice(@PathParam("id") Long id) {
        Invoice invoice = entityManager.find(Invoice.class, id);
        if (invoice == null) {
            throw new WebApplicationException("Invoice not found", 404);
        }
        return invoice;
    }

    @POST
    @Transactional
    public Response createInvoice(Invoice invoice) {
        entityManager.persist(invoice);
        return Response.status(Response.Status.CREATED).entity(invoice).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateInvoice(@PathParam("id") Long id, Invoice updatedInvoice) {
        Invoice invoice = entityManager.find(Invoice.class, id);
        if (invoice == null) {
            throw new WebApplicationException("Invoice not found", 404);
        }
        invoice.setMember(updatedInvoice.getMember());
        invoice.setMembershipPlan(updatedInvoice.getMembershipPlan());
        invoice.setAmount(updatedInvoice.getAmount());
        invoice.setInvoiceDate(updatedInvoice.getInvoiceDate());
        entityManager.merge(invoice);
        return Response.ok(invoice).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteInvoice(@PathParam("id") Long id) {
        Invoice invoice = entityManager.find(Invoice.class, id);
        if (invoice == null) {
            throw new WebApplicationException("Invoice not found", 404);
        }
        entityManager.remove(invoice);
        return Response.noContent().build();
    }
}
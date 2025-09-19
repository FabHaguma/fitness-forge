package com.gymmis.resource;

import com.gymmis.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @PersistenceContext
    EntityManager entityManager;

    @POST
    @Path("/login")
    public Response login(Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Username and password required"))
                    .build();
        }

        // Simple authentication - in production, use proper security
        User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (user == null || !password.equals(user.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", "Invalid credentials"))
                    .build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("role", user.getRole());
        response.put("userId", user.getId());
        if (user.getMember() != null) {
            response.put("memberId", user.getMember().getId());
        }

        return Response.ok(response).build();
    }

    @POST
    @Path("/register")
    public Response register(Map<String, String> userData) {
        String username = userData.get("username");
        String password = userData.get("password");
        String role = userData.get("role"); // "admin" or "member"

        if (username == null || password == null || role == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Username, password, and role required"))
                    .build();
        }

        // Check if user already exists
        Long existingCount = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();

        if (existingCount > 0) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Username already exists"))
                    .build();
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // In production, hash the password
        user.setRole(role);

        entityManager.persist(user);

        return Response.status(Response.Status.CREATED)
                .entity(Map.of("message", "User registered successfully", "userId", user.getId()))
                .build();
    }
}
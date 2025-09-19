package com.gymmis.resource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
public class ReportingResource {

    @PersistenceContext
    EntityManager entityManager;

    @GET
    @Path("/dashboard")
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboard = new HashMap<>();

        // Total active members
        Query memberQuery = entityManager.createQuery("SELECT COUNT(m) FROM Member m");
        Long totalMembers = (Long) memberQuery.getSingleResult();
        dashboard.put("totalMembers", totalMembers);

        // Total revenue from invoices
        Query revenueQuery = entityManager.createQuery("SELECT SUM(i.amount) FROM Invoice i");
        Double totalRevenue = (Double) revenueQuery.getSingleResult();
        dashboard.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);

        // Attendance per class (bookings count)
        Query attendanceQuery = entityManager.createQuery(
            "SELECT cs.name, COUNT(b) FROM Booking b JOIN b.classSession cs WHERE b.status = 'booked' GROUP BY cs.name"
        );
        List<Object[]> attendanceResults = attendanceQuery.getResultList();
        Map<String, Long> attendanceMap = new HashMap<>();
        for (Object[] result : attendanceResults) {
            attendanceMap.put((String) result[0], (Long) result[1]);
        }
        dashboard.put("attendancePerClass", attendanceMap);

        // Active vs inactive members (simplified - all members are considered active for now)
        dashboard.put("activeMembers", totalMembers);
        dashboard.put("inactiveMembers", 0L);

        return dashboard;
    }

    @GET
    @Path("/revenue-trends")
    public List<Object[]> getRevenueTrends() {
        // Simple revenue by month (assuming invoiceDate is in YYYY-MM-DD format)
        Query query = entityManager.createQuery(
            "SELECT SUBSTRING(i.invoiceDate, 1, 7), SUM(i.amount) FROM Invoice i GROUP BY SUBSTRING(i.invoiceDate, 1, 7) ORDER BY SUBSTRING(i.invoiceDate, 1, 7)"
        );
        return query.getResultList();
    }

    @GET
    @Path("/member-stats")
    public Map<String, Object> getMemberStats() {
        Map<String, Object> stats = new HashMap<>();

        // Members by gender
        Query genderQuery = entityManager.createQuery(
            "SELECT m.gender, COUNT(m) FROM Member m GROUP BY m.gender"
        );
        List<Object[]> genderResults = genderQuery.getResultList();
        Map<String, Long> genderMap = new HashMap<>();
        for (Object[] result : genderResults) {
            genderMap.put((String) result[0], (Long) result[1]);
        }
        stats.put("byGender", genderMap);

        // Members by age group
        Query ageQuery = entityManager.createQuery(
            "SELECT CASE WHEN m.age < 25 THEN 'Under 25' WHEN m.age BETWEEN 25 AND 34 THEN '25-34' WHEN m.age BETWEEN 35 AND 44 THEN '35-44' ELSE '45+' END, COUNT(m) FROM Member m GROUP BY CASE WHEN m.age < 25 THEN 'Under 25' WHEN m.age BETWEEN 25 AND 34 THEN '25-34' WHEN m.age BETWEEN 35 AND 44 THEN '35-44' ELSE '45+' END"
        );
        List<Object[]> ageResults = ageQuery.getResultList();
        Map<String, Long> ageMap = new HashMap<>();
        for (Object[] result : ageResults) {
            ageMap.put((String) result[0], (Long) result[1]);
        }
        stats.put("byAgeGroup", ageMap);

        return stats;
    }

    @GET
    @Path("/class-popularity")
    public List<Object[]> getClassPopularity() {
        // Most popular classes by booking count
        Query query = entityManager.createQuery(
            "SELECT cs.name, cs.type, COUNT(b) FROM Booking b JOIN b.classSession cs WHERE b.status = 'booked' GROUP BY cs.name, cs.type ORDER BY COUNT(b) DESC"
        );
        return query.getResultList();
    }

    @GET
    @Path("/export/csv")
    @Produces("text/csv")
    public String exportReportAsCSV() {
        StringBuilder csv = new StringBuilder();
        csv.append("Report Type,Value\n");

        // Add some sample data
        Query memberQuery = entityManager.createQuery("SELECT COUNT(m) FROM Member m");
        Long totalMembers = (Long) memberQuery.getSingleResult();
        csv.append("Total Members,").append(totalMembers).append("\n");

        Query revenueQuery = entityManager.createQuery("SELECT SUM(i.amount) FROM Invoice i");
        Double totalRevenue = (Double) revenueQuery.getSingleResult();
        csv.append("Total Revenue,").append(totalRevenue != null ? totalRevenue : 0.0).append("\n");

        return csv.toString();
    }
}
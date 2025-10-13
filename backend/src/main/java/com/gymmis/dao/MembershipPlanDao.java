package com.gymmis.dao;

import com.gymmis.entity.MembershipPlan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MembershipPlanDao {

    @Inject
    DataSource dataSource;

    public List<MembershipPlan> findAll() throws SQLException {
        List<MembershipPlan> membershipPlans = new ArrayList<>();
        String sql = "SELECT id, name, type, amount FROM membership_plans";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MembershipPlan membershipPlan = new MembershipPlan();
                membershipPlan.setId(rs.getLong("id"));
                membershipPlan.setName(rs.getString("name"));
                membershipPlan.setType(rs.getString("type"));
                membershipPlan.setAmount(rs.getDouble("amount"));
                membershipPlans.add(membershipPlan);
            }
        }
        return membershipPlans;
    }

    public MembershipPlan findById(Long id) throws SQLException {
        String sql = "SELECT id, name, type, amount FROM membership_plans WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MembershipPlan membershipPlan = new MembershipPlan();
                    membershipPlan.setId(rs.getLong("id"));
                    membershipPlan.setName(rs.getString("name"));
                    membershipPlan.setType(rs.getString("type"));
                    membershipPlan.setAmount(rs.getDouble("amount"));
                    return membershipPlan;
                }
            }
        }
        return null;
    }

    public void save(MembershipPlan membershipPlan) throws SQLException {
        if (membershipPlan.getId() == null) {
            insert(membershipPlan);
        } else {
            update(membershipPlan);
        }
    }

    private void insert(MembershipPlan membershipPlan) throws SQLException {
        String sql = "INSERT INTO membership_plans (name, type, amount) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, membershipPlan.getName());
            stmt.setString(2, membershipPlan.getType());
            stmt.setDouble(3, membershipPlan.getAmount());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    membershipPlan.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    private void update(MembershipPlan membershipPlan) throws SQLException {
        String sql = "UPDATE membership_plans SET name = ?, type = ?, amount = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, membershipPlan.getName());
            stmt.setString(2, membershipPlan.getType());
            stmt.setDouble(3, membershipPlan.getAmount());
            stmt.setLong(4, membershipPlan.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM membership_plans WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
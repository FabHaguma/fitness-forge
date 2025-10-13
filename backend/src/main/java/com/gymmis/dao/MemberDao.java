package com.gymmis.dao;

import com.gymmis.entity.Member;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MemberDao {

    @Inject
    DataSource dataSource;

    public List<Member> findAll() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT id, name, age, gender, contact FROM members";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                member.setAge(rs.getInt("age"));
                member.setGender(rs.getString("gender"));
                member.setContact(rs.getString("contact"));
                members.add(member);
            }
        }
        return members;
    }

    public Member findById(Long id) throws SQLException {
        String sql = "SELECT id, name, age, gender, contact FROM members WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Member member = new Member();
                    member.setId(rs.getLong("id"));
                    member.setName(rs.getString("name"));
                    member.setAge(rs.getInt("age"));
                    member.setGender(rs.getString("gender"));
                    member.setContact(rs.getString("contact"));
                    return member;
                }
            }
        }
        return null;
    }

    public void save(Member member) throws SQLException {
        if (member.getId() == null) {
            insert(member);
        } else {
            update(member);
        }
    }

    private void insert(Member member) throws SQLException {
        String sql = "INSERT INTO members (name, age, gender, contact) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, member.getName());
            stmt.setInt(2, member.getAge());
            stmt.setString(3, member.getGender());
            stmt.setString(4, member.getContact());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    member.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    private void update(Member member) throws SQLException {
        String sql = "UPDATE members SET name = ?, age = ?, gender = ?, contact = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getName());
            stmt.setInt(2, member.getAge());
            stmt.setString(3, member.getGender());
            stmt.setString(4, member.getContact());
            stmt.setLong(5, member.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM members WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
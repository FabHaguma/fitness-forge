package com.gymmis.dao;

import com.gymmis.entity.Member;
import com.gymmis.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserDao {

    @Inject
    DataSource dataSource;

    @Inject
    MemberDao memberDao;

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password, role, member_id FROM users";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));

                Long memberId = rs.getLong("member_id");
                if (memberId != null) {
                    Member member = memberDao.findById(memberId);
                    user.setMember(member);
                }

                users.add(user);
            }
        }
        return users;
    }

    public User findById(Long id) throws SQLException {
        String sql = "SELECT id, username, password, role, member_id FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));

                    Long memberId = rs.getLong("member_id");
                    if (memberId != null) {
                        Member member = memberDao.findById(memberId);
                        user.setMember(member);
                    }

                    return user;
                }
            }
        }
        return null;
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password, role, member_id FROM users WHERE username = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));

                    Long memberId = rs.getLong("member_id");
                    if (memberId != null) {
                        Member member = memberDao.findById(memberId);
                        user.setMember(member);
                    }

                    return user;
                }
            }
        }
        return null;
    }

    public void save(User user) throws SQLException {
        if (user.getId() == null) {
            insert(user);
        } else {
            update(user);
        }
    }

    private void insert(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role, member_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            if (user.getMember() != null) {
                stmt.setLong(4, user.getMember().getId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    private void update(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, member_id = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            if (user.getMember() != null) {
                stmt.setLong(4, user.getMember().getId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }
            stmt.setLong(5, user.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
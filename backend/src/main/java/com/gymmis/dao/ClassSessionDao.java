package com.gymmis.dao;

import com.gymmis.entity.ClassSession;
import com.gymmis.entity.Trainer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ClassSessionDao {

    @Inject
    DataSource dataSource;

    @Inject
    TrainerDao trainerDao;

    public List<ClassSession> findAll() throws SQLException {
        List<ClassSession> classSessions = new ArrayList<>();
        String sql = "SELECT id, name, type, schedule, trainer_id FROM class_sessions";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ClassSession classSession = new ClassSession();
                classSession.setId(rs.getLong("id"));
                classSession.setName(rs.getString("name"));
                classSession.setType(rs.getString("type"));
                classSession.setSchedule(rs.getString("schedule"));

                Long trainerId = rs.getLong("trainer_id");
                if (trainerId != null) {
                    Trainer trainer = trainerDao.findById(trainerId);
                    classSession.setTrainer(trainer);
                }

                classSessions.add(classSession);
            }
        }
        return classSessions;
    }

    public ClassSession findById(Long id) throws SQLException {
        String sql = "SELECT id, name, type, schedule, trainer_id FROM class_sessions WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ClassSession classSession = new ClassSession();
                    classSession.setId(rs.getLong("id"));
                    classSession.setName(rs.getString("name"));
                    classSession.setType(rs.getString("type"));
                    classSession.setSchedule(rs.getString("schedule"));

                    Long trainerId = rs.getLong("trainer_id");
                    if (trainerId != null) {
                        Trainer trainer = trainerDao.findById(trainerId);
                        classSession.setTrainer(trainer);
                    }

                    return classSession;
                }
            }
        }
        return null;
    }

    public void save(ClassSession classSession) throws SQLException {
        if (classSession.getId() == null) {
            insert(classSession);
        } else {
            update(classSession);
        }
    }

    private void insert(ClassSession classSession) throws SQLException {
        String sql = "INSERT INTO class_sessions (name, type, schedule, trainer_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, classSession.getName());
            stmt.setString(2, classSession.getType());
            stmt.setString(3, classSession.getSchedule());
            if (classSession.getTrainer() != null) {
                stmt.setLong(4, classSession.getTrainer().getId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    classSession.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    private void update(ClassSession classSession) throws SQLException {
        String sql = "UPDATE class_sessions SET name = ?, type = ?, schedule = ?, trainer_id = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, classSession.getName());
            stmt.setString(2, classSession.getType());
            stmt.setString(3, classSession.getSchedule());
            if (classSession.getTrainer() != null) {
                stmt.setLong(4, classSession.getTrainer().getId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }
            stmt.setLong(5, classSession.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM class_sessions WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
package com.gymmis.dao;

import com.gymmis.entity.Trainer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TrainerDao {

    @Inject
    DataSource dataSource;

    public List<Trainer> findAll() throws SQLException {
        List<Trainer> trainers = new ArrayList<>();
        String sql = "SELECT id, name, specialty FROM trainers";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Trainer trainer = new Trainer();
                trainer.setId(rs.getLong("id"));
                trainer.setName(rs.getString("name"));
                trainer.setSpecialty(rs.getString("specialty"));
                trainers.add(trainer);
            }
        }
        return trainers;
    }

    public Trainer findById(Long id) throws SQLException {
        String sql = "SELECT id, name, specialty FROM trainers WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Trainer trainer = new Trainer();
                    trainer.setId(rs.getLong("id"));
                    trainer.setName(rs.getString("name"));
                    trainer.setSpecialty(rs.getString("specialty"));
                    return trainer;
                }
            }
        }
        return null;
    }

    public void save(Trainer trainer) throws SQLException {
        if (trainer.getId() == null) {
            insert(trainer);
        } else {
            update(trainer);
        }
    }

    private void insert(Trainer trainer) throws SQLException {
        String sql = "INSERT INTO trainers (name, specialty) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, trainer.getName());
            stmt.setString(2, trainer.getSpecialty());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trainer.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    private void update(Trainer trainer) throws SQLException {
        String sql = "UPDATE trainers SET name = ?, specialty = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trainer.getName());
            stmt.setString(2, trainer.getSpecialty());
            stmt.setLong(3, trainer.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM trainers WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
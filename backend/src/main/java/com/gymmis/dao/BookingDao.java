package com.gymmis.dao;

import com.gymmis.entity.Booking;
import com.gymmis.entity.ClassSession;
import com.gymmis.entity.Member;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BookingDao {

    @Inject
    DataSource dataSource;

    @Inject
    MemberDao memberDao;

    @Inject
    ClassSessionDao classSessionDao;

    public List<Booking> findAll() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT id, member_id, class_session_id, status FROM bookings";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getLong("id"));

                Long memberId = rs.getLong("member_id");
                if (memberId != null) {
                    Member member = memberDao.findById(memberId);
                    booking.setMember(member);
                }

                Long classSessionId = rs.getLong("class_session_id");
                if (classSessionId != null) {
                    ClassSession classSession = classSessionDao.findById(classSessionId);
                    booking.setClassSession(classSession);
                }

                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }
        }
        return bookings;
    }

    public Booking findById(Long id) throws SQLException {
        String sql = "SELECT id, member_id, class_session_id, status FROM bookings WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Booking booking = new Booking();
                    booking.setId(rs.getLong("id"));

                    Long memberId = rs.getLong("member_id");
                    if (memberId != null) {
                        Member member = memberDao.findById(memberId);
                        booking.setMember(member);
                    }

                    Long classSessionId = rs.getLong("class_session_id");
                    if (classSessionId != null) {
                        ClassSession classSession = classSessionDao.findById(classSessionId);
                        booking.setClassSession(classSession);
                    }

                    booking.setStatus(rs.getString("status"));
                    return booking;
                }
            }
        }
        return null;
    }

    public void save(Booking booking) throws SQLException {
        if (booking.getId() == null) {
            insert(booking);
        } else {
            update(booking);
        }
    }

    private void insert(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (member_id, class_session_id, status) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (booking.getMember() != null) {
                stmt.setLong(1, booking.getMember().getId());
            } else {
                stmt.setNull(1, Types.BIGINT);
            }

            if (booking.getClassSession() != null) {
                stmt.setLong(2, booking.getClassSession().getId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }

            stmt.setString(3, booking.getStatus());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    private void update(Booking booking) throws SQLException {
        String sql = "UPDATE bookings SET member_id = ?, class_session_id = ?, status = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (booking.getMember() != null) {
                stmt.setLong(1, booking.getMember().getId());
            } else {
                stmt.setNull(1, Types.BIGINT);
            }

            if (booking.getClassSession() != null) {
                stmt.setLong(2, booking.getClassSession().getId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }

            stmt.setString(3, booking.getStatus());
            stmt.setLong(4, booking.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM bookings WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
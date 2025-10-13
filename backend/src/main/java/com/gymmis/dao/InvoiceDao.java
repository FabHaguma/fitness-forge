package com.gymmis.dao;

import com.gymmis.entity.Invoice;
import com.gymmis.entity.Member;
import com.gymmis.entity.MembershipPlan;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class InvoiceDao {

    @Inject
    DataSource dataSource;

    @Inject
    MemberDao memberDao;

    @Inject
    MembershipPlanDao membershipPlanDao;

    public List<Invoice> findAll() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT id, member_id, membership_plan_id, amount, invoice_date FROM invoices";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getLong("id"));

                Long memberId = rs.getLong("member_id");
                if (memberId != null) {
                    Member member = memberDao.findById(memberId);
                    invoice.setMember(member);
                }

                Long membershipPlanId = rs.getLong("membership_plan_id");
                if (membershipPlanId != null) {
                    MembershipPlan membershipPlan = membershipPlanDao.findById(membershipPlanId);
                    invoice.setMembershipPlan(membershipPlan);
                }

                invoice.setAmount(rs.getDouble("amount"));
                invoice.setInvoiceDate(rs.getString("invoice_date"));
                invoices.add(invoice);
            }
        }
        return invoices;
    }

    public Invoice findById(Long id) throws SQLException {
        String sql = "SELECT id, member_id, membership_plan_id, amount, invoice_date FROM invoices WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId(rs.getLong("id"));

                    Long memberId = rs.getLong("member_id");
                    if (memberId != null) {
                        Member member = memberDao.findById(memberId);
                        invoice.setMember(member);
                    }

                    Long membershipPlanId = rs.getLong("membership_plan_id");
                    if (membershipPlanId != null) {
                        MembershipPlan membershipPlan = membershipPlanDao.findById(membershipPlanId);
                        invoice.setMembershipPlan(membershipPlan);
                    }

                    invoice.setAmount(rs.getDouble("amount"));
                    invoice.setInvoiceDate(rs.getString("invoice_date"));
                    return invoice;
                }
            }
        }
        return null;
    }

    public void save(Invoice invoice) throws SQLException {
        if (invoice.getId() == null) {
            insert(invoice);
        } else {
            update(invoice);
        }
    }

    private void insert(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO invoices (member_id, membership_plan_id, amount, invoice_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (invoice.getMember() != null) {
                stmt.setLong(1, invoice.getMember().getId());
            } else {
                stmt.setNull(1, Types.BIGINT);
            }

            if (invoice.getMembershipPlan() != null) {
                stmt.setLong(2, invoice.getMembershipPlan().getId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }

            stmt.setDouble(3, invoice.getAmount());
            stmt.setString(4, invoice.getInvoiceDate());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    private void update(Invoice invoice) throws SQLException {
        String sql = "UPDATE invoices SET member_id = ?, membership_plan_id = ?, amount = ?, invoice_date = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (invoice.getMember() != null) {
                stmt.setLong(1, invoice.getMember().getId());
            } else {
                stmt.setNull(1, Types.BIGINT);
            }

            if (invoice.getMembershipPlan() != null) {
                stmt.setLong(2, invoice.getMembershipPlan().getId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }

            stmt.setDouble(3, invoice.getAmount());
            stmt.setString(4, invoice.getInvoiceDate());
            stmt.setLong(5, invoice.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM invoices WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}
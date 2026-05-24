package com.project.dao;

import com.project.database.DBConnection;
import com.project.models.Bill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    public List<Bill> getAllBills() {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT * FROM bills";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapBill(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean saveBill(Bill b) {
        String sql = "INSERT INTO bills (build_id, final_cpu, final_gpu, final_price, final_score) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, b.getBuildId());
            ps.setString(2, b.getFinalCpu());
            ps.setString(3, b.getFinalGpu());
            ps.setInt(4, b.getFinalPrice());
            ps.setInt(5, b.getFinalScore());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getTotalBills() {
        String sql = "SELECT COUNT(*) FROM bills";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(final_price),0) FROM bills";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Bill mapBill(ResultSet rs) throws SQLException {
        java.sql.Timestamp ts = rs.getTimestamp("purchase_date");
        LocalDateTime date = ts != null ? ts.toLocalDateTime() : null;
        return new Bill(
            rs.getInt("bill_id"),
            rs.getInt("build_id"),
            rs.getString("final_cpu"),
            rs.getString("final_gpu"),
            rs.getInt("final_price"),
            rs.getInt("final_score"),
            date
        );
    }
}

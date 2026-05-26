package com.project.dao;

import com.project.database.DBConnection;
import com.project.models.GPUOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GPUOptionDAO {

    public List<GPUOption> getAllGPUs() {
        List<GPUOption> list = new ArrayList<>();
        String sql = "SELECT * FROM gpu_options";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapGPU(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<GPUOption> getGPUsByBrand(String brand) {
        List<GPUOption> list = new ArrayList<>();
        String sql = "SELECT * FROM gpu_options WHERE brand = ?";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, brand);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapGPU(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addGPU(GPUOption g) {
        String sql = "INSERT INTO gpu_options (brand, name, for_budget, price_increase, performance_increase) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, g.getBrand());
            ps.setString(2, g.getName());
            ps.setInt(3, g.getForBudget());
            ps.setInt(4, g.getPriceIncrease());
            ps.setInt(5, g.getPerformanceIncrease());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteGPU(int id) {
        String sql = "DELETE FROM gpu_options WHERE gpu_id = ?";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getGPUCount() {
        String sql = "SELECT COUNT(*) FROM gpu_options";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private GPUOption mapGPU(ResultSet rs) throws SQLException {
        return new GPUOption(
            rs.getInt("gpu_id"),
            rs.getString("brand"),
            rs.getString("name"),
            rs.getInt("for_budget"),
            rs.getInt("price_increase"),
            rs.getInt("performance_increase")
        );
    }
}

package com.project.dao;

import com.project.database.DBConnection;
import com.project.models.Build;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuildDAO {

    public List<Build> getAllBuilds() {
        List<Build> list = new ArrayList<>();
        String sql = "SELECT * FROM builds";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapBuild(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Build getBuildById(int id) {
        String sql = "SELECT * FROM builds WHERE build_id = ?";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapBuild(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Build> getBuildsByBudget(int budget) {
        List<Build> list = new ArrayList<>();
        String sql = "SELECT * FROM builds WHERE total_price = ?";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, budget);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapBuild(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addBuild(Build b) {
        String sql = "INSERT INTO builds (cpu_model, cpu_type, gpu_model, gpu_type, ram, storage, psu, total_price, performance_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getCpuModel());
            ps.setString(2, b.getCpuType());
            ps.setString(3, b.getGpuModel());
            ps.setString(4, b.getGpuType());
            ps.setString(5, b.getRam());
            ps.setString(6, b.getStorage());
            ps.setString(7, b.getPsu());
            ps.setInt(8, b.getTotalPrice());
            ps.setInt(9, b.getPerformanceScore());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateBuild(Build b) {
        String sql = "UPDATE builds SET cpu_model=?, cpu_type=?, gpu_model=?, gpu_type=?, ram=?, storage=?, psu=?, total_price=?, performance_score=? WHERE build_id=?";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getCpuModel());
            ps.setString(2, b.getCpuType());
            ps.setString(3, b.getGpuModel());
            ps.setString(4, b.getGpuType());
            ps.setString(5, b.getRam());
            ps.setString(6, b.getStorage());
            ps.setString(7, b.getPsu());
            ps.setInt(8, b.getTotalPrice());
            ps.setInt(9, b.getPerformanceScore());
            ps.setInt(10, b.getBuildId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteBuild(int id) {
        String sql = "DELETE FROM builds WHERE build_id = ?";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getTotalBuilds() {
        String sql = "SELECT COUNT(*) FROM builds";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getAverageScore() {
        String sql = "SELECT COALESCE(AVG(performance_score),0) FROM builds";
        try (Connection conn = DBConnection.get().connection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Build mapBuild(ResultSet rs) throws SQLException {
        return new Build(
            rs.getInt("build_id"),
            rs.getString("cpu_model"),
            rs.getString("cpu_type"),
            rs.getString("gpu_model"),
            rs.getString("gpu_type"),
            rs.getString("ram"),
            rs.getString("storage"),
            rs.getString("psu"),
            rs.getInt("total_price"),
            rs.getInt("performance_score")
        );
    }
}

package com.tenco.dao;

import com.tenco.dto.Admin;
import com.tenco.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    // 관리자 ID로 관리자 조회
    // 비밀번호는 SQL에서 비교하지 않고 DB 저장값을 그대로 가져옴
    // 비밀번호 일치 여부는 업무 규칙에 해당하므로 Service에서 판단
    // >> 비밀번호 암호화(해시처리) 시 수정 용이
    public Admin findByAdminId(String adminId) {
        String sql = """
                SELECT * FROM admins
                WHERE admin_id = ?
                """;
        try (Connection connect = DatabaseUtil.getConnection()) {
            try (PreparedStatement pstmt = connect.prepareStatement(sql)) {
                pstmt.setString(1, adminId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return Admin.builder()
                                .id(rs.getInt("id"))
                                .adminId(rs.getString("admin_id"))
                                .password(rs.getString("password"))
                                .name(rs.getString("name"))
                                .build();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // 관리자 등록 기능 추가
    public void addAdmin(Admin admin) {
        String sql = """
                INSERT INTO admins(admin_id, password, name)
                VALUES (?, ?, ?)
                """;

        try (Connection connect = DatabaseUtil.getConnection()) {
            try (PreparedStatement pstmt = connect.prepareStatement(sql)) {
                pstmt.setString(1, admin.getAdminId());
                pstmt.setString(2, admin.getPassword());
                pstmt.setString(3, admin.getName());

                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO - 테스트
    public static void main(String[] args) {
     AdminDAO adminDAO = new AdminDAO();
     Admin admin = Admin.builder()
             .adminId("admintest")
             .name("관리자 테스트")
             .password("123")
             .build();
     adminDAO.addAdmin(admin);
    }

}

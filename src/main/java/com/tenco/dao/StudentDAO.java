package com.tenco.dao;

import com.tenco.dto.Student;
import com.tenco.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentDAO {

    // 학생 등록 기능
    public void addStudent(Student student) {
        String sql = """
                INSERT INTO student(name, student_id)
                VALUES (?, ?)
                """;
        try (Connection connect = DatabaseUtil.getConnection()) {
            try (PreparedStatement pstmt = connect.prepareStatement(sql)) {
                pstmt.setString(1, student.getName());
                pstmt.setString(2, student.getStudentId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 학생 전체 조회 기능


    // 학생 학번 조회 기능
}

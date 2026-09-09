package com.tenco.dao;

import com.tenco.dto.Student;
import com.tenco.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // 학생 등록 기능
    // TODO - 추후 사용하는 측 확인해서 리턴 타입 변경
    public int addStudent(Student student) {
        int rows = 0;

        String sql = """
                INSERT INTO students(name, student_id)
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
        return rows;
    }

    // 학생 전체 조회 기능
    public List<Student> getAllStudent() {
        List<Student> studentList = new ArrayList<>();
        String sql = """
                SELECT * FROM students
                """;

        try (Connection connect = DatabaseUtil.getConnection()) {
            try (PreparedStatement pstmt = connect.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setStudentId(rs.getString("student_id"));

                    studentList.add(student);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*
        for (int i = 0; i < studentList.size(); i++) {
            System.out.println(studentList.get(i));
        }
        */
        return studentList;
    }

    // 학생 학번 조회 기능
    public Student getStudentById(String studentId) {
        String sql = """
                SELECT * FROM students
                WHERE student_id = ?
                """;

        try (Connection connect = DatabaseUtil.getConnection()) {
            try (PreparedStatement pstmt = connect.prepareStatement(sql)) {
                pstmt.setString(1, studentId);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setStudentId(rs.getString("student_id"));

                    return student;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}

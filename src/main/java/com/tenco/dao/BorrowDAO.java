package com.tenco.dao;

import com.tenco.dto.Borrow;
import com.tenco.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class BorrowDAO {

    // 대출 현황 조회
    // 1.1 현재 대출 중인 도서 목록 조회
    public List<Borrow> getBorrowedBook() {
        List<Borrow> borrowList = new ArrayList<>();

        // 1.2 JOIN 해서 도서 이름 출력
        String sql = """
                SELECT b.id, b.book_id, bk.title, b.student_id, s.name, b.borrow_date, b.return_date
                FROM borrows b
                INNER JOIN books bk ON b.book_id = bk.id
                INNER JOIN students s ON b.student_id = s.id
                WHERE b.return_date IS NULL
                ORDER BY b.borrow_date ASC
                """;

        try (Connection connect = DatabaseUtil.getConnection()) {
            try (PreparedStatement pstmt = connect.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                     borrowList.add(Borrow.builder()
                                     .id(rs.getInt("id"))
                                     .bookId(rs.getInt("book_id"))
                                     .bookTitle(rs.getString("title"))
                                     .studentId(rs.getInt("student_id"))
                                     .studentName(rs.getString("name"))
                                     .borrowDate(rs.getDate("borrow_date").toLocalDate())
                                     // !!!
                                     //.returnDate(rs.getDate("return_date").toLocalDate())
                             .build());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return borrowList;
    }


    // 2. 도서 대출 기능
    public void borrowBook(int bookId, int studentId) throws SQLException {
        Connection connect = null;
        // try-with-resources 사용하지 않는 이유
        // >> catch 절에서 rollback을 호출하려면 connect 변수가 catch 절에서 보여야 함
        // >> 따라서 try 바깥에 선언하고 finally에서 직접 연결 닫음
        try {
            connect = DatabaseUtil.getConnection();
            // setAutoCommit 기본값 = true >> SQL 한 줄마다 즉시 반영
            // false로 변경하면 commit 호출 전까지 변경 사항이 임시 상태로 유지
            connect.setAutoCommit(false);

            // 2.1 대상 도서 대출 가능 여부 - SELECT
            String checksql = """
                    SELECT available FROM books
                    WHERE id = ?
                    """;
            try (PreparedStatement checkPstmt = connect.prepareStatement(checksql)) {
                checkPstmt.setInt(1, bookId);
                try (ResultSet chekcRs = checkPstmt.executeQuery()) {
                    if (!chekcRs.next()) {
                        throw new SQLException("존재하지 않는 도서입니다 ID : " + bookId);
                    }
                    if (!chekcRs.getBoolean("available")) {
                        throw new SQLException("현재 대출 중인 도서입니다. 반납 후 대출 가능합니다.");
                    }
                }
            }

            // 2.2 도서 대출 기록 - INSERT
            String insertsql = """
                    INSERT INTO borrows (book_id, student_id, borrow_date)
                    VALUES (?, ?, ?)
                    """;
            int rows;
            try (PreparedStatement borrowPstmt = connect.prepareStatement(insertsql)) {
                borrowPstmt.setInt(1, bookId);
                borrowPstmt.setInt(2, studentId);
                borrowPstmt.setDate(3, Date.valueOf(LocalDate.now()));
                rows = borrowPstmt.executeUpdate();
            }
            if (rows < 0) {
                throw new SQLException("적용된 기록이 없습니다");
            }
            // 2-3. 도서 상태 변경 (대출 불가로 해당 도서 처리)
            String updateSql = """
                    UPDATE books SET available = FALSE 
                    WHERE id = ? 
                    """;
            try (PreparedStatement updatePstmt = connect.prepareStatement(updateSql)) {
                updatePstmt.setInt(1, bookId);
                updatePstmt.executeUpdate();
            }

            connect.commit();

        } catch (SQLException e) {
            // 롤백 작성 위치
            if (connect != null) {
                connect.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (connect != null) {
                connect.setAutoCommit(true);
                connect.close();
            }
        }


    }

    // 3. 도서 반납 기능
    public void returnBook (int bookId, int studentId) throws SQLException {
        Connection connect = null;
        try {
            connect = DatabaseUtil.getConnection();
            connect.setAutoCommit(false);
            // 3.1 대출 기록 확인 - SELECT
            String checksql = """
                    SELECT * FROM borrows
                    WHERE book_id = ?
                    AND student_id = ?
                    AND return_date IS NULL
                    """;
            int borrowId;
            try (PreparedStatement listPstmt = connect.prepareStatement(checksql)) {
                listPstmt.setInt(1, bookId);
                listPstmt.setInt(2, studentId);
                try (ResultSet rs = listPstmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("반납할 도서가 없습니다");
                    }
                    borrowId = rs.getInt("id");
                }
            }
            // 2.2 반납일을 오늘 날짜로 설정
            String returnsql = """
                UPDATE borrows SET return_date = ?
                WHERE id = ?
                """;
            try (PreparedStatement returnPstmt = connect.prepareStatement(returnsql)) {
                returnPstmt.setDate(1, Date.valueOf(LocalDate.now()));
                returnPstmt.setInt(2, borrowId);
                returnPstmt.executeUpdate();
            }
            // 3.3. 대출 가능 상태
            String availablesql = """
                UPDATE books
                SET available = True
                WHERE id = ?
                """;
            try (PreparedStatement returnPstmt = connect.prepareStatement(availablesql)) {
                returnPstmt.setInt(1, bookId);
                returnPstmt.executeUpdate();
            }

            connect.commit();

        } catch (SQLException e) {
            if (connect != null) {
                connect.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (connect != null) {
                connect.setAutoCommit(true);
                connect.close();
            }
        }
    }


    public static void main(String[] args) throws SQLException {
        BorrowDAO borrowDAO = new BorrowDAO();
//        // 대출 현황 조회
        List<Borrow> borrowList = borrowDAO.getBorrowedBook();
//
//        for (int i = 0; i < borrowList.size(); i++) {
//            System.out.println(borrowList.get(i));
//        }

        borrowDAO.returnBook(3,1);

    }
}

package com.tenco.service;

import com.tenco.dao.BookDAO;
import com.tenco.dao.BorrowDAO;
import com.tenco.dao.StudentDAO;
import com.tenco.dto.Book;
import com.tenco.dto.Borrow;
import com.tenco.dto.Student;

import java.sql.SQLException;
import java.util.List;

public class LibraryService {

    private final BookDAO bookDAO = new BookDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final BorrowDAO borrowDAO = new BorrowDAO();

    // 1. 도서 추가 기능
    // 제목과 저자 중 하나라도 비어있으면 중단
    public void addBook(Book book) throws SQLException {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty() ||
                book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new SQLException("도서 제목과 저자는 필수 입력 항목입니다.");
        }
        bookDAO.addBook(book);
    }

    // 2. 전체 도서 조회
    public List<Book> getAllBook() throws SQLException {
        return bookDAO.getAllBook();
    }

    // 3. 도서 제목 검색
    public List<Book> getBookByTitle(String title) throws SQLException {
        if (title == null || title.trim().isEmpty()) {
            throw new SQLException("검색어를 입력해주세요");
        }
        return bookDAO.getBookByTitle(title);
    }

    // 4. 도서 대출
    public void borrowBook(int bookId, int studentId) throws SQLException {
        if (bookId <= 0 || studentId <= 0) {
            throw new SQLException("유효한 도서 ID와 유효한 학생 ID를 입력해주세요");
        }
        borrowDAO.borrowBook(bookId, studentId);
    }

    // 5. 대출 도서 확인
    public List<Borrow> getBorrowedBook() throws SQLException {
        return borrowDAO.getBorrowedBook();
    }

    // 6. 도서 반납
    public void returnBook(int bookId, int studentId) throws SQLException {
        if (bookId <= 0 || studentId <= 0) {
            throw new SQLException("유효한 도서 ID와 유효한 학생 ID를 입력해주세요");
        }
        borrowDAO.returnBook(bookId, studentId);
    }

    // 7. 학생 등록
    public void addStudent(Student student) throws SQLException {
        if (student.getName() == null || student.getName().trim().isEmpty() ||
                student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            throw new SQLException("학생 이름과 id는 필수 입력 항목입니다.");
        }
        studentDAO.addStudent(student);
    }

    // 8. 전체 학생 조회
    public List<Student> getAllStudent() throws SQLException{
        return studentDAO.getAllStudent();
    }

    // 9. 로그인
    public Student getStudentById(String studentId) throws SQLException {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new SQLException("학번을 입력해주세요");
        }
        return studentDAO.getStudentById(studentId);
    }

}

package com.tenco;

import com.tenco.dao.BookDAO;
import com.tenco.dao.StudentDAO;
import com.tenco.dto.Book;
import com.tenco.dto.Student;

import java.util.List;

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    public static void main(String[] args) {
        StudentDAO studentDAO = new StudentDAO();
        // 학생 전체 조회 테스트
        List<Student> studentList = studentDAO.getAllStudent();
        // 학생 학번 조회 테스트
        Student student = studentDAO.getStudentById("20230003");
        System.out.println(student);

        // =======================================

        BookDAO bookDAO = new BookDAO();
        // 도서 전체 조회 테스트
        List<Book> bookList = bookDAO.getAllBook();
        // 도서 제목 검색 테스트
        List<Book> bookList2 = bookDAO.getBookByTitle("입문");
        for (int i = 0; i < bookList2.size(); i++){
            System.out.println(bookList2.get(i));
        }
        // 도서 등록 테스트
        Book book = new Book("테스트책", "저자", "한빛미디어", 2026, "9788968481239", true);
        bookDAO.addBook(book);

        Book book2 = Book.builder()
                .author("저자2")
                .title("테스트2")
                .publisher("출판사2")
                .publicationYear(2026)
                .isbn("12341234123")
                .build();
        bookDAO.addBook(book2);
    }
}
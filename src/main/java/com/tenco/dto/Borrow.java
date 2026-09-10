package com.tenco.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
// 도서 대출 기록을 담는 DTO
// DTO는 반드시 테이블과 1:1이어야 할 필요는 없다.
public class Borrow {
    private int id;
    private int bookId;
    private String bookTitle;
    private int studentId;
    private String studentName;
    private LocalDate borrowDate;
    private LocalDate returnDate;


    public Borrow(int bookId, int studentId, LocalDate borrowDate, LocalDate returnDate) {
        this.bookId = bookId;
        this.studentId = studentId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }
}

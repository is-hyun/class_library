package com.tenco.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Borrow {
    private int id;
    private int book_id;
    private int student_id;
    private LocalDate borrow_date;
    private LocalDate return_date;
}

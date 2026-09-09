package com.tenco.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int publicationYear;
    private String isbn;
    private boolean available;

    public Book(String title, String author, String publisher, int publicationYear, String isbn, boolean available) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.available = available;
    }
}

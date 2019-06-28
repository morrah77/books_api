package com.morrah77.books.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookModel {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private String assess;
    BookModel(Long id) {
        this.id = id;
    }
}

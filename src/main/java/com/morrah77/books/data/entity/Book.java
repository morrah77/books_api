package com.morrah77.books.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne
    private Author author;

    @OneToOne
    private Genre genre;

    @OneToMany(mappedBy = "book")
    private List<Assess> assess;

    public Book(Long id) {
        this.id = id;
    }
}

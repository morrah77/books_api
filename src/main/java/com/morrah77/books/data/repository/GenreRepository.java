package com.morrah77.books.data.repository;

import com.morrah77.books.data.entity.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, Long> {
}

package com.morrah77.books.data.repository;

import com.morrah77.books.data.entity.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}

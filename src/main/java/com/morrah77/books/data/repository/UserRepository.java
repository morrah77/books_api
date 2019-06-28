package com.morrah77.books.data.repository;

import com.morrah77.books.data.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}

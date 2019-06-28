package com.morrah77.books.data.repository;

import com.morrah77.books.data.entity.Assess;
import com.morrah77.books.data.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AssessRepository extends CrudRepository<Assess, Long> {
    List<Assess> findByUserAndValueNotOrderByValueAscIdAsc(User user, com.morrah77.books.data.Assess value);

    List<Assess> findByUser_IdAndValueNotOrderByValueAscIdAsc(Long userId, com.morrah77.books.data.Assess value);

    Assess findByBook_IdAndUser_Id(Long bookId, Long userId);

    void deleteByBook_IdAndUser_Id(long bookId, long userId);
}

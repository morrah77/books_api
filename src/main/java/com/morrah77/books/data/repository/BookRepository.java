package com.morrah77.books.data.repository;

import com.morrah77.books.data.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    @Query(value = "SELECT book FROM Book book LEFT JOIN book.assess assess where assess.user.id=:userId and assess.value<>:assess")
    List<Book> findByUserAndAssessNot(@Param("userId") Long userId, @Param("assess") com.morrah77.books.data.Assess assess);

    Slice<Book> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = "select book.id, book.title, book.author_id, book.genre_id, avg(assess.value) as assess from book left join assess on assess.book_id=book.id where assess.user_id<>:userId group by book.id, book.title, book.author_id, book.genre_id order by assess asc limit 20 offset 0")
    List<Book> findMostAssessedByOthers(@Param("userId") Long userId);

    /*
    for user 1 expect one from author_id=999 and genre_id=7
    528994972

    not any from author_id=673 and genre_id=5
    NOT 375428666
    NOT 375764895
    NOT  375766316

    not any from author_id=688 and genre_id=21
    379112337


    for user 2 expect not any from author_id=999 and genre_id=7
    NOT 528857800

    one from author_id=673 and genre_id=5
    375766316

    two from author_id=688 and genre_id=21
    379111918
    379112310

    with likes as (select count(book.id) as count, book.author_id as author_id, book.genre_id as genre_id, avg(assess.value) as assess, count(book.id)-sum(assess.value) as likes from book left join assess on assess.book_id=book.id where assess.user_id=1 group by book.author_id, book.genre_id having avg(assess.value)<=0.5 order by assess), read as (select book_id from assess where user_id=1) select book.id, book.title, book.author_id, book.genre_id, genre.title as genre, author.title as author, likes.assess, likes.likes from book left join author on book.author_id=author.id left join genre on book.genre_id=genre.id left join likes on book.genre_id=likes.genre_id and book.author_id=likes.author_id where book.id not in (select * from read) order by likes.assess asc, likes.likes desc limit 20 offset 0

    TODO: consider to calculate recommendation weight separately by genre and author. Consider to calculate weight by other users assessments.

     */
    @Query(nativeQuery = true, value = "with likes as (select count(book.id) as count, book.author_id as author_id, book.genre_id as genre_id, avg(assess.value) as assess, count(book.id)-sum(assess.value) as likes from book left join assess on assess.book_id=book.id where assess.user_id=:userId group by book.author_id, book.genre_id having avg(assess.value)<=0.5 order by assess), read as (select book_id from assess where user_id=:userId) select book.id, book.title, book.author_id, book.genre_id, genre.title as genre, author.title as author, likes.assess, likes.likes from book left join author on book.author_id=author.id left join genre on book.genre_id=genre.id left join likes on book.genre_id=likes.genre_id and book.author_id=likes.author_id where book.id not in (select * from read) order by likes.assess asc, likes.likes desc limit 20 offset 0")
    List<Book> findMostAssessedByGenreAndAuthorExceptAlreadyRead(@Param("userId") Long userId);
}

package com.morrah77.books.service;

import com.morrah77.books.data.Assess;
import com.morrah77.books.data.entity.Book;
import com.morrah77.books.data.repository.BookRepository;
import com.morrah77.books.model.BookModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<BookModel> getRecommendedBooks(Long userId) {
        List<BookModel> result = new ArrayList<>();
        List<Book> list = bookRepository.findMostAssessedByGenreAndAuthorExceptAlreadyRead(userId);
        list.forEach(
                (Object item) -> {
                    Book book = (Book)item;
                    List<com.morrah77.books.data.entity.Assess> assesses = book.getAssess();
                    result.add(new BookModel(
                            book.getId(),
                            book.getTitle(),
                            book.getAuthor().getTitle(),
                            book.getGenre().getTitle(),
                            ""
                    ));
                }
        );
        return result;
    }

    public List<BookModel> getBooksRecommendedByOthers(Long userId) {
        List<BookModel> result = new ArrayList<>();
        List<Book> list = bookRepository.findMostAssessedByOthers(userId);
        list.forEach(
                (Object item) -> {
                    Book book = (Book)item;
                    result.add(new BookModel(
                            book.getId(),
                            book.getTitle(),
                            book.getAuthor().getTitle(),
                            book.getGenre().getTitle(),
                            book.getAssess().get(0).getValue().toString()
                    ));
                }
        );
        return result;
    }

    public List<BookModel> getNotDislikeedBooks(Long userId) {
        List<BookModel> result = new ArrayList<>();
        List<Book> list = bookRepository.findByUserAndAssessNot(userId, Assess.dislike);
        list.forEach(
                (Object item) -> {
                    Book book = (Book)item;
                    result.add(new BookModel(
                            book.getId(),
                            book.getTitle(),
                            book.getAuthor().getTitle(),
                            book.getGenre().getTitle(),
                            book.getAssess().get(0).getValue().toString()
                    ));
                }
                );
        return result;
    }



    public List<BookModel> getBooks(Long userId, int pageNumber, int pageLength, String orderBy, String orderDirection, String filter) {
        List<BookModel> result = new ArrayList<>();
        orderBy = orderBy == null ? "id" : orderDirection;
        orderDirection = orderDirection == null ? "asc" : orderDirection;
        Slice<Book> books = bookRepository.findAll(PageRequest.of(pageNumber, pageLength, Sort.by(Sort.Direction.fromString(orderDirection), orderBy)));
        books.forEach(
                (Object item) -> {
                    Book book = (Book)item;
                    BookModel model = convertBookToBookModel(book);
                    result.add(model);
                }
                );
        return result;
    }

    public BookModel getBook(Long bookId) {
        Optional<Book> book =  bookRepository.findById(bookId);
        if (book.isPresent()) {
            return convertBookToBookModel(book.get());
        }
        return null;
    }

    private BookModel convertBookToBookModel(Book book) {
        BookModel model = new BookModel(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getTitle(),
                book.getGenre().getTitle(),
                null
        );
        List<com.morrah77.books.data.entity.Assess> assesses = book.getAssess();
        if (assesses != null && !assesses.isEmpty()) {
            String assess = book.getAssess().get(0).getValue().toString();
            model.setAssess(assess);
        }
        return model;
    }
}

package com.morrah77.books.controller;

import com.morrah77.books.data.entity.Assess;
import com.morrah77.books.data.entity.Book;
import com.morrah77.books.data.entity.User;
import com.morrah77.books.exception.BookException;
import com.morrah77.books.model.BookModel;
import com.morrah77.books.service.AssessService;
import com.morrah77.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    AssessService assessService;

    @Autowired
    BookService bookService;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public BookModel getBook(
            @RequestHeader(name = "user", required = false, defaultValue = "1") Long userId,
            @RequestParam(name = "id") Long bookId
    ) throws BookException {
        return bookService.getBook(bookId);
    }

    @RequestMapping(path = "/like", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public BookModel likeBook(@RequestHeader(name = "user", required = false, defaultValue = "1") Long userId, @RequestBody BookModel bookModel) throws BookException {
        System.out.println(bookModel.toString());
        Assess assess = assessService.getAssess(bookModel.getId(), userId);
        if (assess == null) {
            User user = new User(userId, null);
            Book book = new Book(bookModel.getId());
            assess = new Assess(null, user, book, com.morrah77.books.data.Assess.like);
        }
        assess = assessService.saveAssess(assess);
        bookModel.setAssess(assess.getValue().toString());
        if (bookModel.getId().equals("123")) {
            throw new BookException("Foo");
        }
        bookModel.setAssess("like");
        return bookModel;
    }

    @RequestMapping(path = "/like", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    public BookModel unlikeBook(@RequestHeader(name = "user", required = false, defaultValue = "1") Long userId, @RequestBody BookModel bookModel) throws BookException {
        System.out.println(bookModel.toString());
        Assess assess = assessService.getAssess(bookModel.getId(), userId);
        if (assess != null) {
            assessService.deleteAssess(assess);
        }
        if (bookModel.getId().equals("123")) {
            throw new BookException("Foo");
        }
        BookModel book = bookService.getBook(bookModel.getId());
        return bookModel;
    }

    @RequestMapping(path = "/dislike", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public BookModel dislikeBook(@RequestBody BookModel book) throws BookException {
        System.out.println(book.toString());
        if (book.getId().equals("123")) {
            throw new BookException("Foo");
        }
        book.setAssess("dislike");
        return book;
    }

    @RequestMapping(path = "/dislike", method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    public BookModel undislikeBook(@RequestBody BookModel book) throws BookException {
        System.out.println(book.toString());
        if (book.getId().equals("123")) {
            throw new BookException("Foo");
        }
        book.setAssess("neutral");
        return book;
    }
}

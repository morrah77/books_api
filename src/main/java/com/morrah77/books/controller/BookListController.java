package com.morrah77.books.controller;

import com.morrah77.books.model.BookModel;
import com.morrah77.books.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookListController {

    @Value("${books.default-page-length:20}")
    private String defaultPageLength;

    @Autowired
    private BookService bookService;

    @RequestMapping(path = "/recommended", method = RequestMethod.GET)
    public List<BookModel> getBooksSelfRecommended(@RequestHeader(name = "user", required = false, defaultValue = "1") Long userId) {
        return bookService.getRecommendedBooks(userId);
    }

    @RequestMapping(path = "/likedorneutral", method = RequestMethod.GET)
    public List<BookModel> getBooksNotDisliked(@RequestHeader(name = "user", required = false, defaultValue = "1") Long userId) {
        return bookService.getNotDislikeedBooks(userId);
    }

    @RequestMapping(path = "/popular", method = RequestMethod.GET)
    public List<BookModel> getBooksRecommendedByOthers(@RequestHeader(name = "user", required = false, defaultValue = "1") Long userId) {
        return bookService.getBooksRecommendedByOthers(userId);
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public List<BookModel> getBooks(
            @RequestHeader(name = "user", required = false, defaultValue = "1") Long userId,
            @RequestParam(name = "p", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "l", required = false, defaultValue = "20") Integer pageLength,
            @RequestParam(name = "by", required = false) String orderBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "filter", required = false) String filter
    ) {
        System.out.println("p is " + String.valueOf(pageNumber));
        System.out.println("dpl is " + defaultPageLength);
        pageNumber = pageNumber == null ? 0: pageNumber;
        return bookService.getBooks(userId, pageNumber, pageLength, orderBy, order, filter);
    }
}

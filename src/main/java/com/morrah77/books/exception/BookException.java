package com.morrah77.books.exception;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookException extends Exception {
    private String message;
}

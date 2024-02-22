package com.miniproject.library.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserBookResponse {

    private String author;
    private String title;
    private String publisher;
    private Date publicationDate;
}

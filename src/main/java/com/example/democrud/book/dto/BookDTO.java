package com.example.democrud.book.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private AuthorDTO author;
    private List<String> tags;
    private LocalDateTime publicationDate;
    private String isbn;
}

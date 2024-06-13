package com.example.democrud.book.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorDTO {
    private Long id;
    private String name;
    private LocalDate birthdate;
    private String nationality;
}

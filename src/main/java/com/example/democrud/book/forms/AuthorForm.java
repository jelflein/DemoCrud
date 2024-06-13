package com.example.democrud.book.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorForm {

    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String name;

    private LocalDate birthdate;

    @Size(max = 255)
    private String nationality;
}

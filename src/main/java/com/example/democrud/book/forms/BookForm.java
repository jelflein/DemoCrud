package com.example.democrud.book.forms;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class BookForm {

    @NotNull
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotNull
    private AuthorForm author;

    private List<String> tags;

    private LocalDateTime publicationDate;

    @Size(max = 255)
    private String isbn;

    private boolean legacy;

    @AssertTrue(message = "Legacy ISBN is required")
    public boolean isLegacyValid() {
        if (legacy) return isbn != null;
        return true;
    }
}

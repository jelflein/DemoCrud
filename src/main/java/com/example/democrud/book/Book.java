package com.example.democrud.book;

import com.example.democrud.book.author.Author;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String title;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull
    private Author author;

    @ElementCollection
    private List<String> tags;

    private LocalDateTime publicationDate;

    @Column(unique = true)
    @Size(max = 255)
    private String isbn;
}
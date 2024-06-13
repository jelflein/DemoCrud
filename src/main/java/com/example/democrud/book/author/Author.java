package com.example.democrud.book.author;

import com.example.democrud.book.Book;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "authors")
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private LocalDate birthdate;

    private String nationality;

    @OneToMany(mappedBy = "author")
    private List<Book> books;
}

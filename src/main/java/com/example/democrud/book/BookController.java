package com.example.democrud.book;

import com.example.democrud.book.dto.BookDTO;
import com.example.democrud.book.forms.BookForm;
import com.example.democrud.book.mapper.BookMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(
            BookService bookService,
            BookMapper bookMapper
    ) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookService.getAll();

        return books.stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getById(id);

        return book.map(value -> ResponseEntity.ok(bookMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public BookDTO createBook(
            @RequestBody
            @Valid
            BookForm bookForm
    ) {
        Book book = bookMapper.toEntity(bookForm);

        book.setId(null);
        return bookMapper.toDTO(bookService.createBook(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long id,
            @RequestBody BookForm bookForm
    ) {
        Book book = bookMapper.toEntity(bookForm);
        book.setId(id);

        return bookService.update(book)
                .map(value -> ResponseEntity.ok(bookMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!bookService.delete(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().build();
    }
}

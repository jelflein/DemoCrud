package com.example.democrud.book;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book createBook(Book book) {
        isISBNUnique(book);
        isAuthorAndTitleUnique(book);

        return bookRepository.save(book);
    }

    public Optional<Book> update(Book book) {
        if (!bookRepository.existsById(book.getId())) return Optional.empty();

        isISBNUnique(book);
        isAuthorAndTitleUnique(book);

        return Optional.of(bookRepository.save(book));
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> getById(Long id) {
        return bookRepository.findById(id);
    }

    public boolean delete(Long id) {
        if (!bookRepository.existsById(id)) return false;

        bookRepository.deleteById(id);
        return true;
    }

    private void isISBNUnique(Book book) {
        if (bookRepository.isISBNUnique(book.getIsbn(), book.getId())) {
            throw new ConstraintViolationException("ISBN is not unique", null);
        }
    }

    private void isAuthorAndTitleUnique(Book book) {
        if (bookRepository.existsBookTitleAndAuthorName(
                book.getTitle(),
                book.getAuthor().getName(),
                book.getId()
        )) {
            throw new ConstraintViolationException("Book is already in library", null);
        }
    }
}

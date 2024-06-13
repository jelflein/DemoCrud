package com.example.democrud;

import com.example.democrud.book.Book;
import com.example.democrud.book.BookRepository;
import com.example.democrud.book.BookService;
import com.example.democrud.book.author.Author;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setIsbn("1234567890");
        book.setTags(List.of("1234567890"));

        Author author = new Author();
        author.setName("Test Author");
        book.setAuthor(author);
    }

    @Test
    void testCreateBookWithNonUniqueISBN() {
        when(bookRepository.isISBNUnique(any(), any())).thenReturn(true);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            bookService.createBook(book);
        });

        assertEquals("ISBN is not unique", exception.getMessage());

        verify(bookRepository, times(1)).isISBNUnique(book.getIsbn(), book.getId());
        verify(bookRepository, never()).existsBookTitleAndAuthorName(anyString(), anyString(), anyLong());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testCreateBook() {
        when(bookRepository.isISBNUnique(anyString(), anyLong())).thenReturn(false);
        when(bookRepository.existsBookTitleAndAuthorName(anyString(), anyString(), anyLong())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book createdBook = bookService.createBook(book);

        verify(bookRepository, times(1)).isISBNUnique(book.getIsbn(), book.getId());
        verify(bookRepository, times(1)).existsBookTitleAndAuthorName(
                book.getTitle(),
                book.getAuthor().getName(),
                book.getId()
        );

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1)).save(bookCaptor.capture());

        assertNotNull(createdBook);
        assertEquals(book, createdBook);
        assertEquals(book, bookCaptor.getValue());
    }

    @Test
    void testCreateBookWithNonUniqueAuthorAndTitle() {
        when(bookRepository.isISBNUnique(anyString(), anyLong())).thenReturn(false);
        when(bookRepository.existsBookTitleAndAuthorName(anyString(), anyString(), anyLong())).thenReturn(true);

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            bookService.createBook(book);
        });

        assertEquals("Book is already in library", exception.getMessage());

        verify(bookRepository, times(1)).isISBNUnique(book.getIsbn(), book.getId());
        verify(bookRepository, times(1)).existsBookTitleAndAuthorName(
                book.getTitle(),
                book.getAuthor().getName(),
                book.getId()
        );
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testUpdateBook() {
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        when(bookRepository.isISBNUnique(anyString(), anyLong())).thenReturn(false);
        when(bookRepository.existsBookTitleAndAuthorName(anyString(), anyString(), anyLong())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Optional<Book> updatedBook = bookService.update(book);

        assertTrue(updatedBook.isPresent());
        assertEquals(book, updatedBook.get());

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1)).save(bookCaptor.capture());
        assertEquals(book, bookCaptor.getValue());

        verify(bookRepository, times(1)).existsById(book.getId());
        verify(bookRepository, times(1)).isISBNUnique(book.getIsbn(), book.getId());
        verify(bookRepository, times(1)).existsBookTitleAndAuthorName(
                book.getTitle(),
                book.getAuthor().getName(),
                book.getId()
        );
    }

    @Test
    void testUpdateNonExistingBook() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        Optional<Book> updatedBook = bookService.update(book);

        assertFalse(updatedBook.isPresent());

        verify(bookRepository, times(1)).existsById(book.getId());
        verify(bookRepository, never()).isISBNUnique(anyString(), anyLong());
        verify(bookRepository, never()).existsBookTitleAndAuthorName(anyString(), anyString(), anyLong());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAll();

        assertEquals(1, result.size());
        assertEquals(book, result.get(0));

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.getById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteBook() {
        when(bookRepository.existsById(anyLong())).thenReturn(true);

        boolean result = bookService.delete(1L);

        assertTrue(result);

        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNonExistingBook() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        boolean result = bookService.delete(1L);

        assertFalse(result);

        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, never()).deleteById(1L);
    }
}


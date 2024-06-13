package com.example.democrud;

import com.example.democrud.book.forms.AuthorForm;
import com.example.democrud.book.forms.BookForm;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookFormValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidBookForm() {
        BookForm bookForm = new BookForm();
        bookForm.setTitle("Valid Title");
        AuthorForm authorForm = new AuthorForm();
        authorForm.setName("Valid Author");
        bookForm.setAuthor(authorForm);
        bookForm.setIsbn("1234567890");
        bookForm.setLegacy(true);

        Set<ConstraintViolation<BookForm>> violations = validator.validate(bookForm);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testBookFormWithNullTitle() {
        BookForm bookForm = new BookForm();
        bookForm.setTitle(null);
        AuthorForm authorForm = new AuthorForm();
        authorForm.setName("Valid Author");
        bookForm.setAuthor(authorForm);

        Set<ConstraintViolation<BookForm>> violations = validator.validate(bookForm);

        assertEquals(2, violations.size());

        Iterator<ConstraintViolation<BookForm>> constraintViolationIterator = violations.iterator();
        assertEquals(
                Set.of(
                        "must not be blank",
                        "must not be null"
                ),
                Set.of(
                        constraintViolationIterator.next().getMessage(),
                        constraintViolationIterator.next().getMessage()
                )
        );
    }

    @Test
    void testBookFormWithBlankTitle() {
        BookForm bookForm = new BookForm();
        bookForm.setTitle("");
        AuthorForm authorForm = new AuthorForm();
        authorForm.setName("Valid Author");
        bookForm.setAuthor(authorForm);

        Set<ConstraintViolation<BookForm>> violations = validator.validate(bookForm);
        assertEquals(1, violations.size());
        assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testBookFormWithLongTitle() {
        BookForm bookForm = new BookForm();
        bookForm.setTitle("A".repeat(256)); // Title with 256 characters
        AuthorForm authorForm = new AuthorForm();
        authorForm.setName("Valid Author");
        bookForm.setAuthor(authorForm);

        Set<ConstraintViolation<BookForm>> violations = validator.validate(bookForm);
        assertEquals(1, violations.size());
        assertEquals("size must be between 0 and 255", violations.iterator().next().getMessage());
    }

    @Test
    void testBookFormWithNullAuthor() {
        BookForm bookForm = new BookForm();
        bookForm.setTitle("Valid Title");
        bookForm.setAuthor(null);

        Set<ConstraintViolation<BookForm>> violations = validator.validate(bookForm);
        assertEquals(1, violations.size());
        assertEquals("must not be null", violations.iterator().next().getMessage());
    }

    @Test
    void testBookFormWithLegacyButNoIsbn() {
        BookForm bookForm = new BookForm();
        bookForm.setTitle("Valid Title");
        AuthorForm authorForm = new AuthorForm();
        authorForm.setName("Valid Author");
        bookForm.setAuthor(authorForm);
        bookForm.setLegacy(true);
        bookForm.setIsbn(null);

        Set<ConstraintViolation<BookForm>> violations = validator.validate(bookForm);
        assertEquals(1, violations.size());
        assertEquals("Legacy ISBN is required", violations.iterator().next().getMessage());
    }
}

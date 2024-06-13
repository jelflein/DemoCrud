package com.example.democrud.book.mapper;

import com.example.democrud.book.Book;
import com.example.democrud.book.dto.BookDTO;
import com.example.democrud.book.forms.BookForm;
import org.mapstruct.Mapper;

@Mapper(uses = {AuthorMapper.class}, componentModel = "spring")
public interface BookMapper {

    Book toEntity(BookForm bookForm);

    BookDTO toDTO(Book book);
}

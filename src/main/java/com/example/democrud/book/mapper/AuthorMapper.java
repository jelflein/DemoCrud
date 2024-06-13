package com.example.democrud.book.mapper;

import com.example.democrud.book.author.Author;
import com.example.democrud.book.dto.AuthorDTO;
import com.example.democrud.book.forms.AuthorForm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorForm authorForm);

    AuthorDTO toDTO(Author author);
}

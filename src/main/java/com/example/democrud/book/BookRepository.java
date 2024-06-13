package com.example.democrud.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
                select case when count(b) > 0 then true else false end
                from Book b, Author a
                where (:id is null or b.id != :id) and b.title = :title and a.name = :author
            """)
    boolean existsBookTitleAndAuthorName(
            @Param("title") String title,
            @Param("author") String author,
            @Param("id") @Nullable Long id
    );


    @Query("""
                select case when count(b) > 0 then false else true end
                from Book b
                where (:id is null or b.id = :id) and (:isbn is null or b.isbn = :isbn)
            """)
    boolean isISBNUnique(
            @Param("isbn") String isbn,
            @Param("id") @Nullable Long id
    );

    List<Book> findAll();
}

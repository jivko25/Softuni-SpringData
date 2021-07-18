package com.example.springintro.repository;

import com.example.springintro.model.entity.Author;
import com.example.springintro.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a ORDER BY a.books.size DESC")
    List<Author> findAllByBooksSizeDESC();

    @Query(value = "Select a From Author a WHERE a.firstName LIKE %:patern")
    List<Author> findAllByPattern(@Param(value = "patern") String patern);

    @Query(value = "Select a From Author a")
    List<Author> findAllAuthors();
}

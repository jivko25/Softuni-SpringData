package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> findAllBookNameByAgeRestriction(AgeRestriction ageRestriction);

    List<String> findGoldenBooks(Integer copies);

    List<String> findBooksByPrice(BigDecimal min, BigDecimal max);

    List<String> notReleasedBooks(int year);

    List<String> findAllBooksBeforeDate(int year, int month, int day);

    List<String> findBooksByTitlePattern(String pattern);

}

package com.example.springintro.repository;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDateAfter);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String author_firstName, String author_lastName);

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, Integer copies);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal min, BigDecimal max);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate startDate, LocalDate endDate);

    @Query(value = "Select b FROM Book b WHERE b.title LIKE %:pattern%")
    List<Book> findBookByPattern(@Param(value = "pattern") String pattern);

    @Query(value = "Select b FROM Book b WHERE b.author.lastName LIKE :pattern%")
    List<Book> findBookByAuthorPattern(@Param(value = "pattern") String pattern);

    @Query(value = "Select count (b) FROM Book b WHERE length(b.title) > :target")
    int findBooksByLength(@Param(value = "target") int target);

    @Query(value = "Select sum(b.copies) From Book b WHERE b.author.firstName = :firstName AND b.author.lastName = :lastName")
    int findBookCopiesByAuthor(@Param(value = "firstName") String firstName,
                                      @Param(value = "lastName") String lastName);

    List<Book> findAllByTitle(String title);

//    @Query(value = "select b FROM Book b WHERE b.title = :target")
//    List<Book> findAllByTitle(@Param(value = "target") String title);

}

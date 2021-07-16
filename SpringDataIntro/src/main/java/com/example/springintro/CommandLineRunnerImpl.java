package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.hibernate.query.criteria.internal.expression.function.UpperFunction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final Scanner scanner;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, Scanner scanner) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.scanner = scanner;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

        System.out.println("Enter number of task:");
        String task = scanner.nextLine();
        switch (task){
            case "1":
                pringAllBooksByAgeRestriction();
                break;
            case "2":
                printGoldenBooks(5000);
                break;
            case "3":
                printBooksByPrice(BigDecimal.valueOf(5L), BigDecimal.valueOf(40L));
                break;
            case "4":
                printNotReleasedBooks();
                break;
        }

    }


    private void pritnALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void pringAllBooksByAgeRestriction(){
        System.out.println("Enter age restriction:");
        AgeRestriction ageRestriction = AgeRestriction.valueOf(scanner.nextLine().toUpperCase(Locale.ROOT));
        bookService
                .findAllBookNameByAgeRestriction(ageRestriction)
                .forEach(System.out::println);
    }

    private void printGoldenBooks(Integer copies){
        bookService
                .findGoldenBooks(copies)
                .forEach(System.out::println);
    }

    private void printBooksByPrice(BigDecimal min, BigDecimal max){
        bookService
                .findBooksByPrice(min, max)
                .forEach(System.out::println);
    }

    private void printNotReleasedBooks() {
        System.out.println("Enter release date:");
        int year = Integer.parseInt(scanner.nextLine());
        bookService
                .notReleasedBooks(year)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}

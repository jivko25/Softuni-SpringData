package com.example.json;

import com.example.json.repository.ProductRepo;
import com.example.json.service.CategoryService;
import com.example.json.service.ProductService;
import com.example.json.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final ProductRepo productRepo;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;

    public CommandLineRunnerImpl(ProductRepo productRepo, CategoryService categoryService, ProductService productService, UserService userService) {
        this.productRepo = productRepo;
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
        productService.firstQuery();
    }

    public void seedData() throws IOException {
        categoryService.seedCategory();
        userService.seedUser();
        productService.seedProduct();
    }
}

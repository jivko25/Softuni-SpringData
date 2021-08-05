package com.example.json.service.impl;

import com.example.json.constants.GlobalConstants;
import com.example.json.model.dto.FirstQueryDto;
import com.example.json.model.dto.ProductSeedDto;
import com.example.json.model.entity.Product;
import com.example.json.repository.ProductRepo;
import com.example.json.repository.UserRepo;
import com.example.json.service.CategoryService;
import com.example.json.service.ProductService;
import com.example.json.service.UserService;
import com.example.json.util.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final UserRepo userRepo;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ModelMapper ModelMapper;
    private final ProductRepo productRepo;
    private final BufferedReader bufferedReader;


    public ProductServiceImpl(ValidationUtil validationUtil, Gson gson, UserRepo userRepo, UserService userService, CategoryService categoryService, org.modelmapper.ModelMapper modelMapper, ProductRepo productRepo, BufferedReader bufferedReader) {
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.userRepo = userRepo;
        this.userService = userService;
        this.categoryService = categoryService;
        ModelMapper = modelMapper;
        this.productRepo = productRepo;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void seedProduct() throws IOException {
        if(productRepo.count() != 0){
            return;
        }
        Random random = new Random();
        String productContent = Files
                .readString(Path.of(GlobalConstants.productsPath));

        ProductSeedDto[] productSeedDtos = gson
                .fromJson(productContent, ProductSeedDto[].class);

        Arrays.stream(productSeedDtos)
                .filter(validationUtil::isValid)
                .forEach(productSeedDto -> {
                    boolean isProductBought = random.nextBoolean();
                    Product product = ModelMapper.map(productSeedDto, Product.class);
                    if(isProductBought) {
                        try {
                            product.setBuyer(userService.getRandomUser());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        product.setSeller(userService.getRandomUser());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    product.setCategory(categoryService.getRandomCategory());
                    productRepo.save(product);
                });

    }

    @Override
    public void firstQuery() {
        ObjectMapper mapper = new ObjectMapper();
        Set<Product> products = productRepo.findProductBySellerNotNull();
        products
                .stream()
                .filter(validationUtil::isValid)
                .forEach(product -> {
                    FirstQueryDto firstQueryDto = ModelMapper.map(product, FirstQueryDto.class);
                    try {
                        mapper.writeValue(new File("src/main/resources/out/out"), firstQueryDto);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

    }
}

package com.example.json.service.impl;

import com.example.json.model.dto.CategorySeedDto;
import com.example.json.model.entity.Category;
import com.example.json.repository.CategoryRepo;
import com.example.json.service.CategoryService;
import com.example.json.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.json.constants.GlobalConstants.categoriesPath;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(Gson gson, ValidationUtil validationUtil, CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedCategory() throws IOException {
        if(categoryRepo.count() > 0){
            return;
        }
        String fileContent = Files
                .readString(Path.of(categoriesPath));

        CategorySeedDto[] categorySeedDtos = gson
                .fromJson(fileContent, CategorySeedDto[].class);

        Arrays.stream(categorySeedDtos)
                .filter(validationUtil::isValid)
                .map(categorySeedDto -> modelMapper.map(categorySeedDto, Category.class))
                .forEach(categoryRepo::save);
    }

    @Override
    public Set<Category> getRandomCategory() {
        Set<Category> categories = new HashSet<Category>();
        int randomNumberOfCategories = ThreadLocalRandom.current().nextInt(1, 3);
        for (int i = 0; i <= randomNumberOfCategories; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(0, categoryRepo.count() + 1);
            Category category = categoryRepo.findCategoryById(randomId);
            if(categories.contains(category) == false){
                continue;
            }
            else {
                categories.add(category);
            }
        }
        return categories;
    }
}

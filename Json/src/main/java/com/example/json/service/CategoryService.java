package com.example.json.service;

import com.example.json.model.entity.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {
    void seedCategory() throws IOException;
    Set<Category> getRandomCategory();
}

package com.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategory(Long categoryId);

    List<Category> getAllCategories();

    Page<Category> getCategoryList(Integer pageNum, Integer pageSize);
}

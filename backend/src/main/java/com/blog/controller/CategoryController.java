package com.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.Result;
import com.blog.entity.Category;
import com.blog.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public Result<Category> createCategory(@RequestBody @Valid CategoryRequest request) {
        Category category = new Category();
        category.setCategoryName(request.getCategoryName());
        category.setCategoryAlias(request.getCategoryAlias());
        category.setDescription(request.getDescription());
        Category created = categoryService.createCategory(category);
        return Result.success("创建成功", created);
    }

    @PutMapping("/update")
    public Result<Category> updateCategory(@RequestBody Category category) {
        Category updated = categoryService.updateCategory(category);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }

    @GetMapping("/all")
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }

    @GetMapping("/list")
    public Result<Page<Category>> getCategoryList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Category> page = categoryService.getCategoryList(pageNum, pageSize);
        return Result.success(page);
    }
}

@Data
class CategoryRequest {
    @NotBlank(message = "分类名称不能为空")
    private String categoryName;
    private String categoryAlias;
    private String description;
}

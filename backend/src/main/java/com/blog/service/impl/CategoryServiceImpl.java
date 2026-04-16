package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.BizException;
import com.blog.common.ResponseCode;
import com.blog.entity.Category;
import com.blog.mapper.CategoryMapper;
import com.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category createCategory(Category category) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getCategoryName, category.getCategoryName());
        if (categoryMapper.selectCount(wrapper) > 0) {
            throw new BizException(ResponseCode.PARAM_ERROR.getCode(), "分类名称已存在");
        }
        category.setIsDeleted(0);
        categoryMapper.insert(category);
        return category;
    }

    @Override
    public Category updateCategory(Category category) {
        Category existingCategory = categoryMapper.selectById(category.getId());
        if (existingCategory == null) {
            throw new BizException(ResponseCode.NOT_FOUND.getCode(), "分类不存在");
        }
        categoryMapper.updateById(category);
        return category;
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryMapper.deleteById(categoryId);
    }

    @Override
    public List<Category> getAllCategories() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getIsDeleted, 0);
        wrapper.orderByAsc(Category::getCreateTime);
        return categoryMapper.selectList(wrapper);
    }

    @Override
    public Page<Category> getCategoryList(Integer pageNum, Integer pageSize) {
        Page<Category> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getIsDeleted, 0);
        wrapper.orderByDesc(Category::getCreateTime);
        return categoryMapper.selectPage(page, wrapper);
    }
}

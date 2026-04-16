package com.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.Result;
import com.blog.entity.Tag;
import com.blog.service.TagService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping("/create")
    public Result<Tag> createTag(@RequestBody @Valid TagRequest request) {
        Tag tag = new Tag();
        tag.setTagName(request.getTagName());
        tag.setTagAlias(request.getTagAlias());
        tag.setSort(request.getSort() != null ? request.getSort() : 0);
        Tag created = tagService.createTag(tag);
        return Result.success("创建成功", created);
    }

    @PutMapping("/update")
    public Result<Tag> updateTag(@RequestBody Tag tag) {
        Tag updated = tagService.updateTag(tag);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success();
    }

    @GetMapping("/all")
    public Result<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return Result.success(tags);
    }

    @GetMapping("/list")
    public Result<Page<Tag>> getTagList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Tag> page = tagService.getTagList(pageNum, pageSize);
        return Result.success(page);
    }
}

@Data
class TagRequest {
    @NotBlank(message = "标签名称不能为空")
    private String tagName;
    private String tagAlias;
    private Integer sort;
}

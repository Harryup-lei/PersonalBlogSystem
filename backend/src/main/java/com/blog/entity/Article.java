package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("blog_article")
public class Article {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String articleTitle;
    
    private String articleCover;
    
    private Long categoryId;
    
    private String articleSummary;
    
    private String mdFilePath;
    
    private Integer isTop;
    
    private Integer status;
    
    private Long viewCount;
    
    private Long likeCount;
    
    @TableLogic
    private Integer isDeleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

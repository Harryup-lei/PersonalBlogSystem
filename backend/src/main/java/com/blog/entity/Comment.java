package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("blog_comment")
public class Comment {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long articleId;
    
    private String commentator;
    
    private String commentEmail;
    
    private String commentContent;
    
    private Long likeCount;
    
    private Integer status;
    
    @TableLogic
    private Integer isDeleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

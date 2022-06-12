package com.nec.article.model;

import com.nec.article.util.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class ArticleVO {
    private Integer id;
    private Integer articleId;
    private String title;
    private String content;
    private String author;
    private Status status;
    private LocalDateTime createdDt;
    private LocalDateTime updatedDt;
}

package com.nec.article.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String author;

    @CreationTimestamp
    private LocalDateTime createdDt;

    @UpdateTimestamp
    private LocalDateTime updatedDt;

    @OneToMany(mappedBy="articleId")
    private List<ArticleDetail> articleDetail;


}

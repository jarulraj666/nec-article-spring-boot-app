package com.nec.article.repository;

import com.nec.article.entity.ArticleDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleDetailRepository extends CrudRepository<ArticleDetail, Integer> {
}

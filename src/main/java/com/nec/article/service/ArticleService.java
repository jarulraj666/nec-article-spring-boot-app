package com.nec.article.service;

import com.nec.article.entity.Article;
import com.nec.article.entity.ArticleDetail;
import com.nec.article.model.ArticleVO;
import com.nec.article.repository.ArticleDetailRepository;
import com.nec.article.repository.ArticleRepository;
import com.nec.article.util.Status;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final ArticleDetailRepository articleDetailRepository;

    public ArticleService(ArticleRepository articleRepository, ArticleDetailRepository articleDetailRepository) {
        this.articleRepository = articleRepository;
        this.articleDetailRepository = articleDetailRepository;
    }

    private static final Function<Article, ArticleVO> activeArticle = articles -> {
        Optional<ArticleDetail> historyOp = articles.getArticleDetail().stream().filter(articleDetail -> Status.APPROVED.equals(articleDetail.getStatus())).findFirst();
        if (historyOp.isPresent()) {
            ArticleDetail articleDetail = historyOp.get();
            return ArticleVO.builder().articleId(articles.getId()).author(articles.getAuthor()).title(articleDetail.getTitle())
                    .content(articleDetail.getContent()).createdDt(articleDetail.getCreatedDt())
                    .updatedDt(articleDetail.getUpdatedDt()).build();
        }
        return null;
    };

    private static final Function<Article, ArticleVO> pendingArticle = articles -> {
        Optional<ArticleDetail> historyOp = articles.getArticleDetail().stream().filter(articleDetail -> Status.CREATED.equals(articleDetail.getStatus())).findFirst();
        if (historyOp.isPresent()) {
            ArticleDetail articleDetail = historyOp.get();
            return ArticleVO.builder().articleId(articles.getId()).author(articles.getAuthor()).title(articleDetail.getTitle())
                    .content(articleDetail.getContent()).createdDt(articleDetail.getCreatedDt())
                    .updatedDt(articleDetail.getUpdatedDt()).build();
        }
        return null;
    };

    public List<ArticleVO> getArticles() {

        return Streamable.of(this.articleRepository.findAll()).stream().map(activeArticle).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public ArticleVO getArticleById(Integer articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(RuntimeException::new);
        return activeArticle.apply(article);
    }

    public ArticleVO getPendingArticleById(Integer articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(RuntimeException::new);
        return pendingArticle.apply(article);
    }

    public ArticleVO postArticles(ArticleVO articleVO) {
        Article article = Article.builder().author(articleVO.getAuthor()).build();
        Article articleResult = this.articleRepository.save(article);
        ArticleDetail articleDetail = ArticleDetail.builder().articleId(articleResult.getId()).title(articleVO.getTitle()).content(articleVO.getContent()).status(Status.CREATED).build();
        this.articleDetailRepository.save(articleDetail);

        return ArticleVO.builder().articleId(articleResult.getId()).build();
    }

    public ArticleVO updateArticles(ArticleVO articleVO) {
        Integer articleId = articleVO.getArticleId();
        if(null != articleId) {
            Article article = this.articleRepository.findById(articleId).orElseThrow(RuntimeException::new);
            if(null != article) {
                article.getArticleDetail().stream().filter(articleDetail -> Status.APPROVED.equals(articleDetail.getStatus())).peek(articleDetail -> articleDetail.setStatus(Status.EXPIRED)).forEach(articleDetail -> articleDetailRepository.save(articleDetail)
                );

                ArticleDetail articleDetail = ArticleDetail.builder().articleId(articleId).title(articleVO.getTitle()).content(articleVO.getContent()).status(Status.CREATED).build();
                articleDetailRepository.save(articleDetail);
            }
        }
        return articleVO;
    }

    public ArticleVO approveArticles(ArticleVO articleVO) {
        Integer articleId = articleVO.getArticleId();
        if(null != articleId) {
            Article article = this.articleRepository.findById(articleId).orElseThrow(RuntimeException::new);
            if(null != article) {
                article.getArticleDetail().stream().filter(articleDetail -> Status.CREATED.equals(articleDetail.getStatus())).peek(articleDetail -> articleDetail.setStatus(articleVO.getStatus())).forEach(articleDetail -> articleDetailRepository.save(articleDetail)
                );
            }
        }
        return articleVO;
    }

    public List<ArticleVO> getPendingArticles() {
        return Streamable.of(this.articleRepository.findAll()).stream().map(pendingArticle).filter(Objects::nonNull).collect(Collectors.toList());
    }
}

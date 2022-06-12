package com.nec.article.controller;

import com.nec.article.entity.Article;
import com.nec.article.entity.ArticleDetail;
import com.nec.article.model.ArticleVO;
import com.nec.article.service.ArticleService;
import com.nec.article.util.Status;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@SecurityRequirement(name = "article")
@RequestMapping("/article")
@CrossOrigin(origins = "*")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping("/getAll")
    public List<ArticleVO> getAllArticles() {
        return service.getArticles();
    }

    @GetMapping("/{id}")
    public ArticleVO getArticleById(@PathVariable("id") Integer id) {
        return service.getArticleById(id);
    }



    @GetMapping("/getPending")
    public List<ArticleVO> getPendingReviewArticles() {
        return service.getPendingArticles();
    }

    @GetMapping("/getPending/{id}")
    public ArticleVO getPendingReviewArticles(@PathVariable("id") Integer id) {
        return service.getPendingArticleById(id);
    }

    @PostMapping("/create")
    public ArticleVO postArticle(@RequestBody ArticleVO article) {
        return service.postArticles(article);
    }

    @PutMapping("/update")
    public ArticleVO updateArticle(@RequestBody ArticleVO article) {
        return service.updateArticles(article);
    }

    @PutMapping("/approve")
    public ArticleVO approve(@RequestBody ArticleVO article) {
        return service.approveArticles(article);
    }

}

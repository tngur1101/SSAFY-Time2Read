package org.ssafy.bibibig.articles.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssafy.bibibig.articles.application.ArticleService;
import org.ssafy.bibibig.articles.dto.ArticleWithQuiz;
import org.ssafy.bibibig.articles.dto.response.GameResponse;
import org.ssafy.bibibig.common.dto.Response;
import org.ssafy.bibibig.common.exception.CommonException;
import org.ssafy.bibibig.member.utils.SessionInfo;
import org.ssafy.bibibig.quiz.dto.Quiz;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/game")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    @GetMapping("/{year}")
    public Response<List<ArticleWithQuiz>> getArticleWithQuiz(HttpServletRequest request, @PathVariable int year) {
        try {
            log.info("request : {}", request);
            Long memberId = SessionInfo.getSessionMemberId(request);
            log.info("member Id : {}", memberId);
            if(memberId <= 5 && year == 2022)
                return Response.success(articleService.getArticleWithQuizForAdmin());
            else
                return Response.success(articleService.getArticleWithQuizzes(year));
        } catch (CommonException e) {
            e.printStackTrace();
        }
        return Response.success(articleService.getArticleWithQuizzes(year));
    }

    @GetMapping("/{year}/first")
    public Response<List<ArticleWithQuiz>> getFirstArticleWithQuiz(@PathVariable int year) {
        return Response.success(articleService.getQuizzes(year));
    }

    @GetMapping("/{year}/second")
    public Response<List<ArticleWithQuiz>> getSecondArticleWithQuiz(@PathVariable int year) {
        return Response.success(articleService.getQuizzesWithOX(year));
    }

}

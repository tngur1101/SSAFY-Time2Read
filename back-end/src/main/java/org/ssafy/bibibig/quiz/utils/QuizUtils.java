package org.ssafy.bibibig.quiz.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.ssafy.bibibig.articles.dto.Article;
import org.ssafy.bibibig.quiz.dto.Clue;
import org.ssafy.bibibig.quiz.dto.Quiz;
import org.ssafy.bibibig.quiz.dto.QuizType;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class QuizUtils {

    private final WordDefine wordDefineRequest;

    // 키워드 리스트 받아서 정의 찾기
    public Quiz makeQuiz(Article article) {
        Clue clue = findKeyword(article);
        String blurContent = makeBlur(article.content(), clue.word());
        String blurSummary = makeBlur(article.summary(), clue.word());
        return new Quiz(QuizType.KEYWORD, blurContent, blurSummary, clue.word(), clue);
    }

    private Clue findKeyword(Article article) {

        List<String> keywords = article.keywords().stream().filter(this::determineKeyword).toList();
        if (keywords.isEmpty()) {
            keywords = Objects.requireNonNull(
                            WiseNerKeywords.findNerWords(article.content()))
                    .stream()
                    .map(WiseNerKeywords.NameEntity::getText)
                    .toList();
        }
        for (String keyword : keywords) {
            try {
                String description = wordDefineRequest.getWordDefine(keyword);
                if (description != null) return new Clue(keyword, description);
            } catch (NoSuchElementException e) {
            }
        }
        throw new NoSuchElementException("No suitable keyword");
    }

    // 문제에 키워드 빈칸 처리하기
    private String makeBlur(String content, String keyword) {
        Pattern pattern = Pattern.compile(keyword);
        int keywordLen = keyword.length();
        StringBuilder replacement = new StringBuilder();
        for (int i = 0; i < keywordLen; i++)
            replacement.append("O");

        return pattern.matcher(content).replaceAll(replacement.toString());
    }

    //TODO: 불용어 있으면 제거하기
    //1. 숫자가 포함되어 있을 경우 숫자 키워드는 제거
    private boolean determineKeyword(String keyword) {
        Pattern pattern = Pattern.compile("\\d");
        return !pattern.matcher(keyword).find();
    }

}
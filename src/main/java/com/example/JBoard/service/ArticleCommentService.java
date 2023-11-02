package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleCommentDtoC;
import com.example.JBoard.Dto.ReplyDto;
import com.example.JBoard.Dto.Request.ArticleCommentRequest;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.ArticleCommentRepository;
import com.example.JBoard.Repository.ArticleRepository;
import com.example.JBoard.Repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    public void saveArticleComment(ArticleCommentDtoC dto) {
        Article article = articleRepository.getReferenceById(dto.getArticleId());
        Optional<UserAccount> userAccount = userAccountRepository.findByUid(dto.getUserAccountDto().uid());

        articleCommentRepository.save(dto.toEntity(article, userAccount.get()));
    }

    public void saveReply(ReplyDto replyDto) {
        System.out.println("replyDto = " + replyDto);
        Article article = articleRepository.getReferenceById(replyDto.articleId());
        Optional<UserAccount> userAccount = userAccountRepository.findByUid(replyDto.userAccountDto().uid());

        Optional<ArticleComment> parent = articleCommentRepository.findById(replyDto.commentId());

        ArticleComment saveReply = articleCommentRepository.save(replyDto.toEntity(article, userAccount.get(), parent.get()));
        parent.get().getChildren().add(saveReply);
    }

    @Transactional(readOnly = true)
    public List<ArticleCommentDtoC> getArticleComments(Long articleId) {
        return articleCommentRepository.findAllByArticleArticleIdAndParentIsNull(articleId)
                .stream().map(ArticleCommentDtoC::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Long getArticleId(Long commentId) {
        ArticleComment comment = articleCommentRepository.getReferenceById(commentId);
        Long articleId = comment.getArticle().getArticleId();
        return articleId;
    }

    public void updateArticleComment(Long commentId, ArticleCommentRequest articleCommentRequest, UserAccountDto userAccountDto) {
        try{
            ArticleComment comment = articleCommentRepository.getReferenceById(commentId);
            UserAccount userAccount = userAccountRepository.findByUid(userAccountDto.uid()).get();

            if (userAccount.getUid().equals(comment.getUserAccount().getUid())){
                articleCommentRepository.updateComment(articleCommentRequest.content(), commentId);
            } else {
                log.warn("다른 사용자가 댓글 수정을 시도했습니다.");
            }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 업데이트 실패. 댓글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }
    public void deleteArticleComment(Long commentId, UserAccountDto userAccountDto) {
        try{
            ArticleComment comment = articleCommentRepository.getReferenceById(commentId);
            Optional<UserAccount> userAccount = userAccountRepository.findByUid(userAccountDto.uid());

            if (userAccount.get().getUid().equals(comment.getUserAccount().getUid())){
                articleCommentRepository.deleteById(commentId);
            } else{
                log.warn("다른 사용자가 댓글 삭제를 시도했습니다.");
            }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 삭제 실패. 댓글 삭제하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }

    }
}

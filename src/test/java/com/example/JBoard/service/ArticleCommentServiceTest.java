package com.example.JBoard.service;

import com.example.JBoard.Dto.ArticleCommentDtoC;
import com.example.JBoard.Dto.ArticleDtoC;
import com.example.JBoard.Dto.Request.ArticleCommentRequest;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.Article;
import com.example.JBoard.Entity.ArticleComment;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Entity.constant.MemberRole;
import com.example.JBoard.Repository.ArticleCommentRepository;
import com.example.JBoard.Repository.ArticleRepository;
import com.example.JBoard.Repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @Mock
    ArticleCommentRepository commentRepository;
    @Mock
    ArticleRepository articleRepository;
    @Mock
    UserAccountRepository userAccountRepository;

    @InjectMocks
    ArticleCommentService commentService;
    @InjectMocks
    ArticleService articleService;

    @DisplayName("댓글 생성 서비스 테스트")
    @Test
    void saveArticleCommentTest() {
        //given
        Article article = createArticle();
        ArticleComment articleComment = createArticleComment();
        ArticleCommentDtoC commentDto = createCommentDto();
        UserAccount userAccount = createUserAccount();
        UserAccountDto userAccountDto = createUserAccountDto();
        ArticleDtoC articleDto = createArticleDto();

        given(userAccountRepository.findByUid(commentDto.getUserAccountDto().uid())).willReturn(Optional.of(userAccount));
        given(articleRepository.save(any(Article.class))).willReturn(article);
        given(commentRepository.save(any(ArticleComment.class))).willReturn(articleComment);

        //when
        articleService.createArticle(articleDto, userAccountDto.toEntity());
        commentService.saveArticleComment(commentDto);

        //then
        then(articleRepository).should().save(any(Article.class));
        then(commentRepository).should().save(any(ArticleComment.class));
    }

    @DisplayName("댓글 삭제 테스트")
    @Test
    public void test() throws Exception{
        //given
        Long commentId = 1L;
        ArticleComment articleComment = createArticleComment();
        UserAccountDto userAccountDto = createUserAccountDto();
        UserAccount userAccount = createUserAccount();

        given(commentRepository.getReferenceById(commentId)).willReturn(articleComment);
        given(userAccountRepository.findByUid(userAccountDto.uid())).willReturn(Optional.of(userAccount));

        //when
        commentService.deleteArticleComment(commentId, userAccountDto);

        //then
        then(commentRepository).should().deleteById(1L);
    }

    @DisplayName("댓글 수정 테스트")
    @Test
    public void updateCommentsTest() throws Exception{
        //given
        ArticleComment articleComment = createArticleComment();
        UserAccountDto userAccountDto = createUserAccountDto();
        ArticleCommentRequest request = createRequest();
        ArticleCommentDtoC commentDto = createCommentDto();
        UserAccount userAccount = createUserAccount();

        given(commentRepository.getReferenceById(commentDto.getId())).willReturn(articleComment);
        given(userAccountRepository.findByUid(userAccountDto.uid())).willReturn(Optional.of(userAccount));

        //when
        commentService.updateArticleComment(articleComment.getCommentId(), request, userAccountDto);

        //then
        then(articleComment.getContent()).equals("바뀐 내용");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "wofud", "password", "김재령", "wofud0321@naver.com", "wofud", String.valueOf(MemberRole.USER)
        );
    }
    private UserAccount createUserAccount() {
        return UserAccount.of("wofud", "1234", "김재령", "wofud0321@naver.com", "재령");
    }

    private ArticleDtoC createArticleDto() {
        return ArticleDtoC.of(createUserAccountDto(),"제목", "내용", LocalDateTime.now());
    }
    private Article createArticle() {
        return Article.of(createUserAccountDto().toEntity(), "제목", "content", 0L);
    }

    private ArticleCommentDtoC createCommentDto() {
        return ArticleCommentDtoC.of(1L, "댓글", createUserAccountDto());
    }

    private ArticleCommentRequest createRequest() {
        return ArticleCommentRequest.of(1L, "바뀐 댓글");
    }
    private ArticleComment createArticleComment() {
        return ArticleComment.of(createUserAccountDto().toEntity(), createArticle(), "댓글");
    }
}
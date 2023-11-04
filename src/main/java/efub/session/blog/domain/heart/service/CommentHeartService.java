package efub.session.blog.domain.heart.service;

import efub.session.blog.domain.account.domain.Account;
import efub.session.blog.domain.account.dto.request.AccountInfoRequestDto;
import efub.session.blog.domain.account.service.AccountService;
import efub.session.blog.domain.comment.domain.Comment;
import efub.session.blog.domain.comment.repository.CommentRepository;
import efub.session.blog.domain.comment.service.CommentService;
import efub.session.blog.domain.heart.domain.CommentHeart;
import efub.session.blog.domain.heart.repository.CommentHeartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentHeartService {
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final CommentHeartRepository commentHeartRepository;
    private final AccountService accountService;

    public void create(Long commentId, AccountInfoRequestDto requestDto) {
        Account account = accountService.findAccountById(requestDto.getAccountId());
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다. id=" + commentId));

        if(isExistsByWriterAndComment(account, comment)){
               throw new RuntimeException("이미 좋아요를 눌렀습니다.");
        }

        CommentHeart commentHeart = CommentHeart.builder()
                .comment(comment)
                .account(account)
                .build();
    }

    public void delete(Long commentId, Long accountId) {
        Account account = accountService.findAccountById(accountId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다. id=" + commentId));

        CommentHeart commentHeart = commentHeartRepository.findByWriterAndComment(account, comment)
                .orElseThrow(() -> new IllegalArgumentException("해당 좋아요가 있습니다."));

        commentHeartRepository.delete(commentHeart);
    }

    @Transactional(readOnly = true)
    public boolean isExistsByWriterAndComment(Account account, Comment comment){
        return commentHeartRepository.existsByWriterAndComment(account, comment);
    }

    @Transactional(readOnly = true)
    public Integer countCommentHeart(Comment comment){
        Integer count = commentHeartRepository.countByComment(comment);
        return count;
    }
}

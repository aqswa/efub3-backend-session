package efub.session.blog.domain.comment.service;

import efub.session.blog.domain.account.domain.Account;
import efub.session.blog.domain.account.service.AccountService;
import efub.session.blog.domain.comment.domain.Comment;
import efub.session.blog.domain.comment.dto.CommentRequestDto;
import efub.session.blog.domain.comment.repository.CommentRepository;
import efub.session.blog.domain.post.domain.Post;
import efub.session.blog.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    // 의존관계 : CommentService -> CommentRepository
    private final CommentRepository commentRepository;

    // 의존관계 : CommentService -> PostService
    private final PostService postService;
    // 의존관계 : CommentService -> AccountService
    private final AccountService accountService;

    // 댓글 작성
    public Comment createComment(Long postId, CommentRequestDto requestDto){
        Post post = postService.findPost(postId);
        Account writer = accountService.findAccountById(requestDto.getAccountId());
        return commentRepository.save(requestDto.toEntity(post, writer));
    }

    // 댓글 조회 - ID별
    @Transactional(readOnly=true) //조회할 때는 이게 메모리 측면에서 효율적
    public Comment findCommentById(Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다. id=" + commentId));
    }

    // 댓글 목록 조회 - 작성자별
    @Transactional(readOnly=true) //조회할 때는 이게 메모리 측면에서 효율적
    public List<Comment> findCommentListByWriter(Account writer){
        return commentRepository.findAllByWriter(writer);
    }

    // 댓글 목록 조회 - 게시글별
    @Transactional(readOnly = true)
    public List<Comment> findCommentListByPost(Long postId){
        Post post = postService.findPost(postId);
        return commentRepository.findAllByPost(post);
    }

    public Comment updateComment(CommentRequestDto requestDto, Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다. id=" + commentId));;
        return comment.updateComment(requestDto.getContent());
    }

    public void deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다. id=" + commentId));;
        commentRepository.delete(comment);
    }
}

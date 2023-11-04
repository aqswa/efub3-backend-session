package efub.session.blog.domain.comment.service;

import efub.session.blog.domain.account.domain.Account;
import efub.session.blog.domain.account.service.AccountService;
import efub.session.blog.domain.comment.domain.Comment;
import efub.session.blog.domain.comment.dto.CommentRequestDto;
import efub.session.blog.domain.comment.repository.CommentRepository;
import efub.session.blog.domain.post.domain.Post;
import efub.session.blog.domain.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    CommentRepository commentRepository;
    @Mock
    PostService postService;
    @Mock
    AccountService accountService;
    @InjectMocks
    CommentService commentService;

    @Test
    public void createCommentTest_성공(){
        Account writer = new Account(1L, "email@email.com", "pw", "nick", "bio");
        Post post = new Post(1L, "title", "content", writer);
        Comment comment = new Comment("content", post, writer);
        CommentRequestDto commentRequestDto = mock(CommentRequestDto.class);

        when(postService.findPost(any())).thenReturn(post);
        when(accountService.findAccountById(any())).thenReturn(writer);
        when(commentRequestDto.toEntity(any(), any())).thenReturn(comment);
        when(commentRepository.save(any())).thenReturn(comment);

        Comment returnedComment = commentService.createComment(1L, commentRequestDto);
        assertEquals(1L, returnedComment.getPost().getPostId());
        assertEquals("content", returnedComment.getContent());
    }


    @Test
    public void createCommentTest_실패(){
        Account writer = new Account(1L, "email@email.com", "pw", "nick", "bio");
        Post post = new Post(1L, "title", "content", writer);
        Comment comment = new Comment("content", post, writer);
        CommentRequestDto commentRequestDto = mock(CommentRequestDto.class);

        when(postService.findPost(any())).thenReturn(post);
        when(accountService.findAccountById(any())).thenReturn(writer);
        when(commentRequestDto.toEntity(any(), any())).thenReturn(comment);
        when(commentRepository.save(any())).thenReturn(comment);

        Comment returnedComment = commentService.createComment(1L, commentRequestDto);
        assertEquals(2L, returnedComment.getPost().getPostId());
        assertEquals("content", returnedComment.getContent());
    }

    @Test
    public void updateCommentTest_성공(){
        Account writer = new Account(1L, "email@email.com", "pw", "nick", "bio");
        Post post = new Post(1L, "title", "content", writer);
        Comment originalComment = new Comment("content", post, writer);

        CommentRequestDto commentRequestDto = mock(CommentRequestDto.class);

        when(commentRequestDto.getContent()).thenReturn("modified");
        when(commentRepository.findById(any())).thenReturn(Optional.of(originalComment));

        Comment returnedComment = commentService.updateComment(commentRequestDto, 1L);
        assertEquals("modified", returnedComment.getContent());
    }

    @Test
    public void updateCommentTest_존재하지않는댓글_예외(){
        CommentRequestDto commentRequestDto = mock(CommentRequestDto.class);

        when(commentRepository.findById(any())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> {
            commentService.updateComment(commentRequestDto, any());
        });
    }

    @Test
    public void deleteCommentTest_성공(){
        Account writer = new Account(1L, "email@email.com", "pw", "nick", "bio");
        Post post = new Post(1L, "title", "content", writer);
        Comment comment = new Comment("content", post, writer);

        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(any());

        commentService.deleteComment(1L);
    }

    @Test
    public void deleteCommentTest_존재하지않는댓글_예외(){
        when(commentRepository.findById(any())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> commentService.deleteComment(any()));
    }
}
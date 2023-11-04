package efub.session.blog.domain.post.service;

import efub.session.blog.domain.account.domain.Account;
import efub.session.blog.domain.account.repository.AccountRepository;
import efub.session.blog.domain.post.domain.Post;
import efub.session.blog.domain.post.dto.request.PostModifyRequestDto;
import efub.session.blog.domain.post.dto.request.PostRequestDto;
import efub.session.blog.domain.post.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;
    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    PostService postService;

    @Test
    public void addPostTest_성공(){
        Account writer = new Account(1L, "email@email.com", "pw", "nick", "bio");
        Post post = new Post(1L, "title", "content", writer);
        PostRequestDto postRequestDto = mock(PostRequestDto.class);

        when(accountRepository.findById(any())).thenReturn(Optional.of(writer));
        when(postRepository.save(any())).thenReturn(post);

        Post returnedPost = postService.addPost(postRequestDto);

        Assertions.assertEquals(1L, returnedPost.getPostId());
        Assertions.assertEquals("title", returnedPost.getTitle());
        Assertions.assertEquals("content", returnedPost.getContent());
        Assertions.assertEquals(writer, returnedPost.getWriter());
    }

    @Test
    public void addPostTest_존재하지않는계정_예외(){
        PostRequestDto postRequestDto = mock(PostRequestDto.class);

        when(accountRepository.findById(any())).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            postService.addPost(postRequestDto);
        });
    }

    @Test
    public void removePost_성공(){
        Account writer = new Account(1L, "email@email.com", "pw", "nick", "bio");
        Post post = new Post(1L, "title", "content", writer);

        when(postRepository.findByPostIdAndWriter_AccountId(any(), any())).thenReturn(Optional.of(post));
        doNothing().when(postRepository).delete(any());

        postService.removePost(1L, 1L);
    }

    @Test
    public void removePost_존재하지않는글_예외(){
        when(postRepository.findByPostIdAndWriter_AccountId(any(), any())).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            postService.removePost(any(), any());
        });
    }

    @Test
    public void modifyPostTest_성공(){
        Account writer = new Account(1L, "email@email.com", "pw", "nick", "bio");
        Post modifiedPost = new Post(1L, "mod", "mod", writer);
        Post post = mock(Post.class);
        PostModifyRequestDto postModifyRequestDto = mock(PostModifyRequestDto.class);

        when(postRepository.findByPostIdAndWriter_AccountId(any(), any())).thenReturn(Optional.of(post));
        when(post.updatePost(postModifyRequestDto)).thenReturn(modifiedPost);

        Post returnedPost = postService.modifyPost(1L, postModifyRequestDto);

        Assertions.assertEquals(1L, returnedPost.getPostId());
        Assertions.assertEquals("mod", returnedPost.getTitle());
        Assertions.assertEquals("mod", returnedPost.getContent());
        Assertions.assertEquals(writer, returnedPost.getWriter());
    }

    @Test
    public void modifyPostTest_존재하지않는글_예외(){
        when(postRepository.findByPostIdAndWriter_AccountId(any(), any())).thenThrow(IllegalArgumentException.class);
        PostModifyRequestDto postModifyRequestDto = mock(PostModifyRequestDto.class);

        assertThrows(IllegalArgumentException.class, () -> {
            postService.modifyPost(1L, postModifyRequestDto);
        });
    }
}
package com.inforush.service;

import com.inforush.dto.PostDTO;
import com.inforush.model.Post;
import com.inforush.model.User;
import com.inforush.repository.PostRepository;
import com.inforush.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private PostService postService;

    private User author;
    private Post post;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId("user-1");
        author.setName("Admin");
        author.setEmail("admin@inforush.com");
        author.setRole(User.Role.ADMIN);

        post = new Post();
        post.setId("post-1");
        post.setTitle("Titulo Teste");
        post.setSlug("titulo-teste");
        post.setContent("Conteudo do post");
        post.setPublished(true);
        post.setAuthor(author);
    }

    @Test
    void findAll_shouldReturnPublishedPosts() {
        when(postRepository.findByPublishedTrue(any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(post)));

        Page<PostDTO.Response> result = postService.listPublished(null, 0, 10);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Titulo Teste");
    }

    @Test
    void findById_shouldReturnPost() {
        when(postRepository.findById("post-1")).thenReturn(Optional.of(post));

        PostDTO.Response result = postService.getPost("post-1");

        assertThat(result.getId()).isEqualTo("post-1");
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        when(postRepository.findById("missing")).thenReturn(Optional.empty());
        when(postRepository.findBySlug("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getPost("missing"))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("Post não encontrado");
    }

    @Test
    void create_shouldPersistPost() {
        PostDTO.Request request = new PostDTO.Request();
        request.setTitle("Novo Post");
        request.setContent("Conteudo novo");
        request.setPublished(true);

        when(postRepository.existsBySlug(any())).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post saved = invocation.getArgument(0);
            saved.setId("post-2");
            return saved;
        });

        PostDTO.Response result = postService.create(request, author);

        assertThat(result.getTitle()).isEqualTo("Novo Post");
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void update_shouldModifyExistingPost() {
        PostDTO.UpdateRequest request = new PostDTO.UpdateRequest();
        request.setTitle("Titulo Atualizado");

        when(postRepository.findById("post-1")).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PostDTO.Response result = postService.update("post-1", request, author);

        assertThat(result.getTitle()).isEqualTo("Titulo Atualizado");
    }

    @Test
    void delete_shouldRemovePost() {
        when(postRepository.findById("post-1")).thenReturn(Optional.of(post));

        postService.delete("post-1", author);

        verify(postRepository).delete(post);
    }

    @Test
    void delete_shouldDenyUnauthorizedUser() {
        User other = new User();
        other.setId("user-2");
        other.setRole(User.Role.USER);

        when(postRepository.findById("post-1")).thenReturn(Optional.of(post));

        assertThatThrownBy(() -> postService.delete("post-1", other))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("Sem permissão");
    }
}

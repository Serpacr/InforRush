package com.inforush.service;

import com.inforush.dto.PostDTO;
import com.inforush.model.Post;
import com.inforush.model.Tag;
import com.inforush.model.User;
import com.inforush.repository.PostRepository;
import com.inforush.repository.TagRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public PostService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    private String slugify(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
            .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
            .toLowerCase().trim()
            .replaceAll("[^a-z0-9\\s-]", "")
            .replaceAll("\\s+", "-");
    }

    public Page<PostDTO.Response> listPublished(String tagSlug, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = tagSlug != null
            ? postRepository.findByTagSlug(tagSlug, pageable)
            : postRepository.findByPublishedTrue(pageable);
        return posts.map(PostDTO.Response::from);
    }

    public PostDTO.Response getPost(String idOrSlug) {
        Post post = postRepository.findById(idOrSlug)
            .or(() -> postRepository.findBySlug(idOrSlug))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));
        return PostDTO.Response.from(post);
    }

    @Transactional
    public PostDTO.Response create(PostDTO.Request req, User author) {
        String slug = slugify(req.getTitle());
        if (postRepository.existsBySlug(slug)) slug = slug + "-" + System.currentTimeMillis();

        Set<Tag> tags = new HashSet<>();
        if (req.getTagIds() != null) {
            for (String id : req.getTagIds()) {
                tagRepository.findById(id).ifPresent(tags::add);
            }
        }

        Post post = new Post();
        post.setTitle(req.getTitle());
        post.setSlug(slug);
        post.setContent(req.getContent());
        post.setExcerpt(req.getExcerpt());
        post.setCoverImage(req.getCoverImage());
        post.setPublished(req.isPublished());
        post.setAuthor(author);
        post.setTags(tags);

        return PostDTO.Response.from(postRepository.save(post));
    }

    @Transactional
    public PostDTO.Response update(String id, PostDTO.UpdateRequest req, User currentUser) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));

        boolean isOwner = post.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;
        if (!isOwner && !isAdmin)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão para editar este post");

        if (req.getTitle() != null) { post.setTitle(req.getTitle()); post.setSlug(slugify(req.getTitle())); }
        if (req.getContent() != null) post.setContent(req.getContent());
        if (req.getExcerpt() != null) post.setExcerpt(req.getExcerpt());
        if (req.getCoverImage() != null) post.setCoverImage(req.getCoverImage());
        if (req.getPublished() != null) post.setPublished(req.getPublished());
        if (req.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>();
            for (String tagId : req.getTagIds()) {
                tagRepository.findById(tagId).ifPresent(tags::add);
            }
            post.setTags(tags);
        }
        return PostDTO.Response.from(postRepository.save(post));
    }

    @Transactional
    public void delete(String id, User currentUser) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));
        boolean isOwner = post.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;
        if (!isOwner && !isAdmin)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão para excluir este post");
        postRepository.delete(post);
    }
}

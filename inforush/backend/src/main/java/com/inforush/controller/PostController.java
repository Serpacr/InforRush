package com.inforush.controller;

import com.inforush.dto.PostDTO;
import com.inforush.model.User;
import com.inforush.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @Operation(summary = "Listar posts publicados")
    public Page<PostDTO.Response> list(
        @RequestParam(required = false) String tag,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return postService.listPublished(tag, page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar post por ID ou slug")
    public PostDTO.Response get(@PathVariable String id) {
        return postService.getPost(id);
    }

    @PostMapping
    @Operation(summary = "Criar novo post", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<PostDTO.Response> create(
        @Valid @RequestBody PostDTO.Request req,
        @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.status(201).body(postService.create(req, user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar post", security = @SecurityRequirement(name = "Bearer Token"))
    public PostDTO.Response update(
        @PathVariable String id,
        @RequestBody PostDTO.UpdateRequest req,
        @AuthenticationPrincipal User user
    ) {
        return postService.update(id, req, user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir post", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<?> delete(
        @PathVariable String id,
        @AuthenticationPrincipal User user
    ) {
        postService.delete(id, user);
        return ResponseEntity.ok(java.util.Map.of("message", "Post excluído com sucesso"));
    }
}

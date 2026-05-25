package com.inforush.controller;

import com.inforush.model.Tag;
import com.inforush.repository.TagRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tags")
public class TagController {

    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    @Operation(summary = "Listar todas as tags")
    public List<Tag> list() {
        return tagRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Criar tag (admin)", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<Tag> create(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String slug = Normalizer.normalize(name, Normalizer.Form.NFD)
            .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
            .toLowerCase().trim()
            .replaceAll("[^a-z0-9\\s-]", "")
            .replaceAll("\\s+", "-");
        Tag tag = new Tag();
        tag.setName(name);
        tag.setSlug(slug);
        return ResponseEntity.status(201).body(tagRepository.save(tag));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tag (admin)", security = @SecurityRequirement(name = "Bearer Token"))
    public ResponseEntity<?> delete(@PathVariable String id) {
        tagRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Tag excluída"));
    }
}
